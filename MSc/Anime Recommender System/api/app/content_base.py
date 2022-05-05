from turtle import position
import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from .constants import PATH
import numpy as np

class content_based_recommender():
    def get_filter_df(username, user_feedback=None):
        # info_df is original dataframe drop the N/A value, all columns remain unchanged
        # anime_df is for one-hot encode and only keep anime_id, name and genre columns
        # split in two dataframe is neccessary, because the user_rating.csv have rating column also,
        # hence it will conflict when merge dataframe with user_profile
        info_df = pd.read_csv(PATH.INFO_CSV)
        info_df = info_df.dropna()
        info_df['genre'] = info_df.genre.str.split(', ')
        anime_df = info_df[['anime_id','name','genre']].copy()
        anime_df = anime_df.dropna()

        # one-hot encoding
        genre_list = []
        for index, row in anime_df.iterrows():    
            for genre in row['genre']:
                anime_df.at[index, genre] = 1
                if genre not in genre_list:
                    genre_list.append(genre)

        anime_df.fillna(0, inplace=True)
   
        genre_matrix = anime_df[genre_list].to_numpy()

        # load data from system generated user_rating.csv and filter with current user
        user_rating_df = pd.read_csv(PATH.USER_RATING_CSV)
        user_preference_df = user_rating_df[user_rating_df['username']==username].reset_index()
        user_preference_df = user_preference_df[['username','anime_id','rating']]

        if user_feedback is not None:
            user_rating_mean = user_preference_df['rating'].mean()
            # maybe use the mean rating from user_pref_df +-(* 1.3)
            # it should be closer to actual user rating habit
            user_feedback = user_feedback[['username','anime_id','feedback']]
            user_feedback.rename(columns = {'feedback':'rating'}, inplace = True)

            # wanna random list and assign to rating 1 or -1, but failed
            # positive_score = np.random.randint(6, 9, user_feedback.shape[0])
            # negative_score = np.random.randint(1, 3, user_feedback.shape[0])

            diff = user_rating_mean * 0.3
            positive_score = user_rating_mean + diff if user_rating_mean - diff <= 10 else 10
            negative_score = user_rating_mean - diff if user_rating_mean - diff >= 0 else 0

            user_feedback.loc[user_feedback['rating'] == 1, "rating"] = positive_score
            user_feedback.loc[user_feedback['rating'] == -1, "rating"] = negative_score

            user_preference_df = pd.concat([user_preference_df, user_feedback])
            

        # merge dataframe and add weight and normalized
        unweighted_user_profile = pd.merge(user_preference_df, anime_df)
        weight = unweighted_user_profile.rating / unweighted_user_profile.rating.sum()
        unweighted_user_genre = unweighted_user_profile[genre_list]
        weigthed_user_profile = unweighted_user_genre.T.dot(weight)
        normalized_user_profile = weigthed_user_profile / weigthed_user_profile.sum()

        # comput cosine similarity and append the similarity to original dataframe
        u_v_matrix = [normalized_user_profile.values]
        recommendation = cosine_similarity(u_v_matrix, genre_matrix)
        info_df['similarity'] = recommendation[0]
        filter_df = info_df.sort_values('similarity',ascending=False).head() #By sorting descending order by simlarity

        return filter_df

        




