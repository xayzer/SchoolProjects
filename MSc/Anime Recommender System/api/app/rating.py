from .constants import PATH
import pandas as pd
from typing import List, Optional, Set
import time
from os.path import exists




class User(dict):
    username: str
    category: Set[str] = set()

class AnimeRating(dict):
    anime_id: int
    rating: int

# load the anime.csv
anime_df = pd.read_csv(PATH.INFO_CSV)
anime_df['genre'] = anime_df.genre.str.split(', ')
anime_df = anime_df.dropna()

genre_list = []
for index, row in anime_df.iterrows():    
    for genre in row['genre']:
        # put each genre in each column and give 1 and 0
        anime_df.at[index, genre] = 1
        if genre not in genre_list:
            genre_list.append(genre)

anime_df.fillna(0, inplace=True)

thumbnail_df = pd.read_csv(PATH.THUMBNAIL_CSV)

class Rating:
    def anime_list_rating(genre_list):

        # init a empty df
        filter_df = pd.DataFrame()
        return_json = []        
        
        # create condition
        cond = False
        for genre in genre_list:
            cond |= (anime_df[genre]==1)
        
        filter_df = anime_df[cond]
        tmp_df = filter_df[genre_list].copy(deep=False)
        tmp_df['match_count'] = tmp_df.sum(axis=1)
        filter_df['match_count'] = tmp_df['match_count'].copy(deep=False)
        filter_df = filter_df.sort_values(by=['match_count'], axis=0, ascending=False, inplace=False, kind='quicksort', na_position='last').head(10)         
        # Get thumbnail
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
        return return_json

    def generate_user_id(user=None, username=None):
        if not exists(PATH.USER_RATING_CSV):
            return 999901
        else:
            # read the csv
            user_rating_df = pd.read_csv(PATH.USER_RATING_CSV, index_col=[0])
            if user is not None:
                username = user['username']
            target_user_df = user_rating_df[user_rating_df['username']==username]
            if not target_user_df.empty:                
                return target_user_df.iloc[0]['user_id']
            else:
                user_rating_df = user_rating_df.sort_values(by='user_id', ascending=False)
                last_user = user_rating_df.iloc[0]                 
                return int(last_user['user_id']) + 1
            

    def store_user_anime_rating_list(user='', anime_rating=[]):
        user_id = Rating.generate_user_id(user)
        user_rating_df = pd.read_csv(PATH.USER_RATING_CSV, index_col=[0]) if exists(PATH.USER_RATING_CSV) else pd.DataFrame(columns=['user_id','username', 'anime_id', 'rating', 'timestamp'])

        for rating in anime_rating:
            # update exist record
            if (((user_rating_df['username']==user['username']) &
                (user_rating_df['anime_id']==rating['anime_id'])).any()) :
                user_rating_df.loc[user_rating_df['anime_id']==rating['anime_id'], 'rating'] = rating['rating']                
                user_rating_df.loc[user_rating_df['anime_id']==rating['anime_id'], 'timestamp'] = time.time()
            else :
                tmp_df = pd.DataFrame([[user_id, user['username'], rating['anime_id'], rating['rating'], time.time()]], columns=['user_id', 'username', 'anime_id', 'rating', 'timestamp'])                
                user_rating_df = pd.concat([user_rating_df, tmp_df])
        
        user_rating_df.to_csv(PATH.USER_RATING_CSV)       