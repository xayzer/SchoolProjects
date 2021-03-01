安卓开发农药浓度APP分析报告<br>
A.使用说明<br>
初始界面为登录界面无账号状态可进行注册，之后进入功能界面，共有四种功能按钮：使用已有模型，手动输入模型数据，训练模型，输入函数参数。<br>
1.使用已有模型：根据用户使用过的模型来对图片进行分析，新用户会有一系统自带模型。<br>
2.手动输入模型数据：要求用户手动输入图片灰度值与浓度来进行录入，可根据数据进行拟合生成模型，该模型也会被存储到1的已有模型中。<br>
3.训练模型：与2功能类似，从相册选取图片并输入浓度来训练模型，模型同样会被存进1中。<br>
4.输入函数参数：用户已知模型参数k，b，直接输入模型信息。<br>
5.如上4种功能在执行完相应功能后跳转到选取图片功能中，该功能为从相机拍照或相册中得到要预测浓度的图片，并让用户对图片进行裁剪，得到想要预测的颜色部位。<br>
6.之后可根据先前导入的模型生成浓度分析图片。最后可用分享按钮来将图片分享给他人（如QQ，空间，朋友圈等）<br>
B.功能简介<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic7.png)<br>
C.实现思路<br>
1.登录注册：<br>
DatabaseHelper中添加链接AS自带数据库以及创建表的语句，用sql语句写登录注册功能的实现，user文件中声明要用到表列名的变量，并添加get方法。<br>
为注册，登陆功能添加activity。注册功能验证码的验证在code类中。<br>
2.使用已有模型：<br>
新用户的已有模型中含有自带训练过的模型数据如下：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic8.png)<br>
原始数据特征: (90,) 训练数据特征 (62,) 测试数据特征 (28,)<br>
最佳拟合线,截距 24.883634395270192 回归系数 [-0.28790104]<br>
该部分是用python来拟合完成的。<br>
新用户会生成自己的存放模型文件，其他功能下生成的模型也会被存放在该文件中。不需要的模型在这里也可以被删除。<br>
这里涉及到的内容大概有python机器学习方面+安卓的文件读取以及spinner的动态用法。<br>
3.输入模型数据：<br>
这一项和下一项都是基于最小二乘法来拟合曲线的。根据输入的灰度值和浓度来生成模型。<br>
4.训练模型：<br>
这里涉及到的内容大概是获取权限，读取相册，进行截图选取bitmap下要取灰度值getPixel（Bitmap bitmap）的位置。<br>
读取相册与截图功能在choiceFromAlbum() ，cropPhoto(Uri inputUri)中，后面的选取要预测的浓度的图片获取方式与此类似，就不多进行赘述了。<br>
只是那个会调用系统相机，函数在startCamera()中。<br>
5.输入函数参数：<br>
无须赘述。输入两个参数。然后录到模型文件中。<br>
6.生成浓度分析图片<br>
这个应该是比较麻烦的地方了。使用了大量的安卓的canvas功能。其代码都在drawResult类中。根据模型的数据来绘画其函数线，可靠结果范围，以及预测点位置。<br>
其中drawGrid(Canvas canvas, Point winSize, Paint paint)，gridPath(int step, Point winSize)，cooPath(Point coo, Point winSize)是来绘画原点坐标以及背景的。<br>
<br>
7.进行图片分享<br>
代码在shareMsg(String activityTitle, String imgPath)中，主要是用new Intent(Intent.ACTION_SEND);来实现的。<br>
<br>
D.结果展示<br>
演示视频在文件夹下<br>
下面是运行截图（运行环境是红米note8pro）<br>
<br>
登录与注册：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic9.png)
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic10.png)<br>
功能选择：                              已有模型：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic11.png)
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic12.png)<br>
输入模型参数：                          训练模型：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic13.png)
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic14.png)<br>
训练模型：                              输入函数参数：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic15.png)
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic16.png)<br>
选取测试浓度图片：                      获得浓度分析图：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic17.png)
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic18.png)<br>
分享生成结果：<br>
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic19.png)
![image](https://github.com/xayzer/SchoolProjects/blob/main/pics%20for%20readme/pic120.png)<br>
