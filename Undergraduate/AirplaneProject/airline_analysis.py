import pandas as pd
import numpy as np
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt


class Airline_Analysis():
    # 初始化
    def __init__(self):
        self.centerPoint = None
        self.df = None
        self.k = None

    def fit(self, df, k):
        self.df = df
        self.k = k
        self.cleanData()
        self.convData()
        self.standData()
        self.kmeansData()
        self.drawModel()
        return self.centerPoint

    # 数据清洗
    def cleanData(self):
        print('开始进行数据清理...')
        # 第一年和第二年的总票价都不是空
        boolIndex = self.df['SUM_YR_1'].notnull() & self.df['SUM_YR_2'].notnull()
        self.df = self.df[boolIndex]  # df第一次清洗之后
        index = (self.df['SUM_YR_1'] != 0)
        index2 = (self.df['SUM_YR_2'] != 0)
        index3 = ((self.df['SEG_KM_SUM'] == 0) & (self.df['avg_discount'] == 0))
        self.df = self.df[index | index2 | index3]
        #生成数据清洗后文件
        self.df.to_excel('./cleanData.xls')
        print('数据清理结束，生成文件'+'cleanData.xls')
        pass

    # 属性规约
    def convData(self):
        print('开始进行属性规约...')
        self.df = self.df[['FFP_DATE', 'LOAD_TIME', 'FLIGHT_COUNT', 'avg_discount', 'SEG_KM_SUM', 'LAST_TO_END']]
        self.df['LAST_TO_END'] = self.df['LAST_TO_END'] / 30
        self.df['L'] = (pd.to_datetime(self.df['LOAD_TIME']) - pd.to_datetime(self.df['FFP_DATE'])) / 30
        self.df['R'] = self.df['LAST_TO_END']
        self.df['F'] = self.df['FLIGHT_COUNT']
        self.df['M'] = self.df['SEG_KM_SUM']
        self.df['C'] = self.df['avg_discount']
        self.df = self.df[['L', 'R', 'F', 'M', 'C']]
        #生成规约后文件
        self.df.to_excel('./reData.xls')
        print('属性规约结束，生成文件' + 'reData.xls')
        pass

    # 数据的归一化
    def standData(self):
        print('开始进行数据标准化...')
        #这里会出现mean函数运算时越界的情况
        dm = self.df / 1000
        self.df = (self.df - dm.mean(axis=0) * 1000) / self.df.std(axis=0)
        #生成归一化后文件
        self.df.to_excel('./stdData.xls', index=False)
        print('数据标准化结束，生成文件' + 'stdData.xls')
        pass

    # 调用KMeans来进行建模
    def kmeansData(self):
        print('开始进行KMEANS建模...')
        model = KMeans(self.k)
        model.fit(self.df)
        r1 = pd.Series(model.labels_).value_counts()
        r2 = pd.DataFrame(model.cluster_centers_)
        r = pd.concat([r1, r2], axis=1)
        r.columns = [u'聚类个数'] + list(self.df.columns)
        r.to_excel('./count_result.xls')
        print('建模结束，生成中间文件' + 'count_res.xls')
        r = pd.concat([self.df, pd.Series(model.labels_, index=self.df.index)], axis=1)
        r.columns = list(self.df.columns) + [u'聚类类别']
        r.to_excel('./final_result.xls')
        print('生成结果文件' + 'final_result.xls')
        self.centerPoint = model.cluster_centers_
        pass

    # 绘制可视化雷达图
    def drawModel(self):
        print('开始绘制中心点雷达图...')
        # 1.构造x轴轴值
        xData = np.linspace(0, 2 * np.pi, self.k, endpoint=False)
        # 首尾相接
        xData = np.concatenate((xData, [xData[0]]))
        yData1 = np.concatenate((self.centerPoint[0], [self.centerPoint[0][0]]))
        yData2 = np.concatenate((self.centerPoint[1], [self.centerPoint[1][0]]))
        yData3 = np.concatenate((self.centerPoint[2], [self.centerPoint[2][0]]))
        yData4 = np.concatenate((self.centerPoint[3], [self.centerPoint[3][0]]))
        yData5 = np.concatenate((self.centerPoint[4], [self.centerPoint[4][0]]))

        fig = plt.figure()
        ax = fig.add_subplot(111, polar=True)

        ax.plot(xData, yData1, 'bo--', linewidth=1, label='A-Customer')
        ax.plot(xData, yData2, 'ro--', linewidth=1, label='B-Customer')
        ax.plot(xData, yData3, 'go--', linewidth=1, label='C-Customer')
        ax.plot(xData, yData4, 'mo--', linewidth=1, label='D-Customer')
        ax.plot(xData, yData5, 'yo--', linewidth=1, label='E-Customer')

        ax.set_thetagrids(xData * 180 / np.pi, ['L', 'R', 'F', 'M', 'C'])

        ax.set_rlim(-3, 3)
        plt.legend(loc='best')
        plt.show()
        pass
    pass

def main():
    # 读取文件设置k值
    df = pd.read_csv('./air_data.csv')
    k = 5
    # 转excel
    df.to_excel('./air_data.xls')

    # 统计数分析数据的特点
    explore = df.describe(percentiles=[], include='all').T
    explore.to_excel('./exploreData.xls')

    # 调用类函数
    aa = Airline_Analysis()
    aa.fit(df, k)
    pass


if __name__ == '__main__':
    main()
    pass


