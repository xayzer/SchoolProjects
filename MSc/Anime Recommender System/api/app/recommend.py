from .constants import PATH
import pandas as pd
from .cfmethod import useKNNwithmeans
from .content_base import content_based_recommender
from os.path import exists
from .rating import Rating
import time
from pydantic import BaseModel
from typing import List, Optional, Set




class AnimeFeedback(dict):
    anime_id: int
    feedback: int 

class Recommend:
    def anime_list_recommend(username, genre_list, user_feedback=None, method=None):
        return_json = []

        anime_df = pd.read_csv(PATH.INFO_CSV)
        anime_df['genre'] = anime_df.genre.str.split(', ')
        anime_df = anime_df.dropna()        
        thumbnail_df = pd.read_csv(PATH.THUMBNAIL_CSV)

        user_rating_df = pd.read_csv(PATH.USER_RATING_CSV)
        count_user = user_rating_df['username'].nunique()

        # If the user number is odd number than use centered KNN method;
        # if the user numebr is even number than use content base method

        if count_user%2 != 0 or method=='knn' or method=='centered_KNN_CF':
            print('>>> Going to use Method KNN <<<<<')
            filter_df = useKNNwithmeans(username, user_feedback=user_feedback)
            method_name = 'centered_KNN_CF'
            display_method_name = 'Centered KNN CF Algorithm'
            msg = 'user simliar with you also liked'
        else:
            print('>>> Going to use Method Content base <<<')
            filter_df = content_based_recommender.get_filter_df(username, user_feedback=user_feedback)
            method_name = 'content_based'
            display_method_name = 'Content Base Algorithm'
            msg = 'genres you liked'

        filter_df['thumbnail_link'] = thumbnail_df[thumbnail_df['anime_id'].isin(filter_df['anime_id'])]['thumbnail_link']

        for index, row in filter_df.iterrows():
            return_json.append(
                {
                    'anime_id': row['anime_id'],
                    'name': row['name'],
                    'genre': row['genre'],
                    'type': row['type'],
                    'episodes': row['episodes'],
                    'rating': row['rating'],
                    'members': row['members'],
                    'thumbnail_link': row['thumbnail_link']
                }
            )
        return {'method':{'name':method_name, 'display':display_method_name}, 'msg': msg,'animes': return_json}

    def store_feedback_result(username='', feedback=None, method=None, count=0):
        user_id = Rating.generate_user_id(username=username)
        feedback_df = pd.read_csv(PATH.USER_FEEDBACK_CSV, index_col=[0]) if exists(PATH.USER_FEEDBACK_CSV) else pd.DataFrame(columns=['user_id', 'username', 'anime_id','method','feedback', 'count','timestamp'])
        for fb in feedback:
            tmp_df = pd.DataFrame([[user_id, username, fb['anime_id'], method, fb['feedback'], count, time.time()]], columns=['user_id', 'username', 'anime_id', 'method','feedback', 'count', 'timestamp'])                
            feedback_df = pd.concat([feedback_df, tmp_df])
        feedback_df.to_csv(PATH.USER_FEEDBACK_CSV) 

        return feedback_df[feedback_df['method']==method]  