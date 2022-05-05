from .constants import PATH
import pandas as pd
from scipy.stats import ttest_rel, ttest_ind

class Evaluation:
    def read_df():
        feedback_df = pd.read_csv(PATH.USER_FEEDBACK_CSV, parse_dates=['timestamp'])
        feedback_df['timestamp'] = pd.to_datetime(feedback_df['timestamp'], unit='s') + pd.Timedelta('08:00:00')

        feedback_df['timestamp'] = feedback_df['timestamp'].apply(lambda x: str(x.date()) + ' ' +('AM' if x.hour<12 else 'PM'))

        feedback_df = feedback_df[['user_id', 'method', 'feedback', 'count', 'timestamp']]
        feedback_df.loc[feedback_df['feedback']==-1, 'feedback'] = 0

        return feedback_df

    def get_method_stat(method):

        rec_method = 'centered_KNN_CF'

        if method == 'content':
            rec_method = 'content_based'

        feedback_df = Evaluation.read_df()

        # Change method name here
        feedback_df = feedback_df[feedback_df['method']==rec_method]



        first_feedback_df = feedback_df[feedback_df['count']==1]
        second_feedback_df = feedback_df[feedback_df['count']==2]

        first_total_mean = first_feedback_df['feedback'].mean()
        second_total_mean = second_feedback_df['feedback'].mean()


        first_date_list = first_feedback_df['timestamp'].unique()        
        first_count_list = first_feedback_df.groupby(['timestamp'])['user_id'].nunique()# / len(first_feedback_df['user_id'].unique())
        first_mean_list = first_feedback_df.groupby(['timestamp'])['feedback'].mean()


        second_date_list = second_feedback_df['timestamp'].unique()                
        second_count_list = second_feedback_df.groupby(['timestamp'])['user_id'].nunique() #/ len(second_feedback_df['user_id'].unique())                
        second_mean_list = second_feedback_df.groupby(['timestamp'])['feedback'].mean()    

        p_value = -1
        test_result = 'error'

        try:        
            t_value, p_value = ttest_rel(first_feedback_df['feedback'],second_feedback_df['feedback'])
        
            if p_value<0.05:
                test_result = 'The performance of 1st result has significant difference from 2nd result.'
            else:
                test_result = 'The performance of 1st result has no significant difference from 2nd result.'
        except ValueError:
            pass
        
        
        return {
            'success': True,
            'method': method,
            'pValue': p_value,
            'testResult': test_result,
            'data': {

                'first': {
                    'totalMean': "{:.2f}".format(first_total_mean),
                    'label': first_date_list.tolist(),
                    'count': first_count_list.to_list(),
                    'mean': first_mean_list.to_list()
                },
                'second': {
                    'totalMean': "{:.2f}".format(second_total_mean),
                    'label': second_date_list.tolist(),
                    'count': second_count_list.to_list(),
                    'mean': second_mean_list.to_list()
                }
            }
        }

    def get_both_method_stat():

        feedback_df = Evaluation.read_df()
        knn_df = feedback_df[feedback_df['method']=='centered_KNN_CF']
        content_df = feedback_df[feedback_df['method']=='content_based']
        
        knn_date_list = knn_df['timestamp'].unique()        
        knn_count_list = knn_df.groupby(['timestamp'])['user_id'].nunique()       
        knn_mean_list = knn_df.groupby(['timestamp'])['feedback'].mean()
        knn_total_mean = knn_df['feedback'].mean()

        content_date_list = content_df['timestamp'].unique()        
        content_count_list = content_df.groupby(['timestamp'])['user_id'].nunique()     
        content_mean_list = content_df.groupby(['timestamp'])['feedback'].mean()
        content_total_mean = content_df['feedback'].mean()  

        p_value = -1
        test_result = 'error'

        try:        
            t_value, p_value = ttest_ind(knn_df['feedback'],content_df['feedback'])
        
            if p_value<0.05:
                test_result = 'The performance of KNN result has significant difference from Content Base result.'
            else:
                test_result = 'The performance of KNN result has no significant difference from Content Base result.'
        except ValueError:
            pass              

        return  {
            'success': True,
            'method': 'both',
            'pValue': p_value,
            'testResult': test_result,
            'data': {

                'knn': {
                    'totalMean': "{:.2f}".format(knn_total_mean),
                    'label': knn_date_list.tolist(),
                    'count': knn_count_list.to_list(),
                    'mean': knn_mean_list.to_list()
                },
                'content': {
                    'totalMean': "{:.2f}".format(content_total_mean),
                    'label': content_date_list.tolist(),
                    'count': content_count_list.to_list(),
                    'mean': content_mean_list.to_list()
                }
            }
        }  