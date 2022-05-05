from .constants import PATH
from numpy import rec
from surprise import Dataset
from surprise import Reader
from surprise import KNNWithMeans
from surprise import accuracy
from surprise.model_selection import train_test_split
from surprise import dump
import random
import pandas as pd


RATING_THRESHOLD = 8
MAX_RATING = 10

# Read files from CSV


def getDatasets(rating_file_path, animation_file_path, user_file_path):
    rating_df = pd.read_csv(rating_file_path, names=[
                            'uid', 'aid', 'usr_rating'], sep=',', dtype={'usr_rating': float}, skiprows=1)
    animation_df = pd.read_csv(animation_file_path, names=['aid', 'aname', 'genre', 'atype', 'episode', 'anime_rating', 'members'], sep=',', dtype={
                               'anime_rating': float, 'members': int}, skiprows=1)
    newu_df = pd.read_csv(user_file_path, names=[
                          'id', 'uid', 'aid', 'usr_rating', 'ts'], sep=',', dtype={'usr_rating': float}, skiprows=1)
    return rating_df, animation_df, newu_df


def finding_neighbors(rating_df, newu_df, uname, k):
    clean_data = rating_df.drop(rating_df[rating_df['usr_rating'] == -1].index)
    user_data = newu_df.loc[newu_df['uid'] == uname]
    user_data = pd.DataFrame(user_data, columns=['uid', 'aid', 'usr_rating'])
    user_watched = user_data['aid'].values.tolist()

    # Split the data for calculation
    temp = int(len(clean_data)/20)
    block_i = random.randint(0, 19)
    data_block = pd.concat(
        [clean_data.iloc[block_i*temp:(block_i+1)*temp], user_data])

    reader = Reader(rating_scale=(1, 10))
    sim_options = {'name': 'cosine',
                   'user_based': True,  # compute  similarities between users
                   'min_support': 1  # minimum number of common items for two users
                   }
    algo = KNNWithMeans(k=50, sim_options=sim_options)

    data = Dataset.load_from_df(
        data_block[["uid", "aid", "usr_rating"]], reader)
    train_set = data.build_full_trainset()
    # fit training data
    algo.fit(train_set)
    
    inner_id = algo.trainset.to_inner_uid(uname)
    user_neighbors = algo.get_neighbors(inner_id, k)
    true_neighbor_id = []
    for inner in user_neighbors:
        true_neighbor_id.append(algo.trainset.to_raw_uid(inner))
    print(true_neighbor_id)
    return true_neighbor_id, user_watched


def rec_anime(usr_neighbors, rating_df, user_watched, k):
    res = {}
    for usr in usr_neighbors:
        usr_rating = rating_df[rating_df['uid'] == usr]
        anime_rec = usr_rating[usr_rating['usr_rating'] >= RATING_THRESHOLD]
        for anime in anime_rec['aid']:
            if anime in res:
                res[anime] += 1
            else:
                res[anime] = 1

    for aid in user_watched:
        res[aid] = 0

    sort_res = sorted(res.items(), key=lambda x: x[1], reverse=True)
    result_list = []
    for atuple in sort_res[:k]:
        result_list.append(atuple[0])
    print(result_list)
    return result_list

def getAnimedf(animation_df, rec_aid):
    anime_df = animation_df.loc[animation_df['aid'].isin(rec_aid)]
    anime_df = anime_df.rename(columns={'aid':'anime_id','aname':'name','atype':'type','episode':'episodes','anime_rating':'rating'})
    anime_df['genre'] = anime_df.genre.str.split(', ')
    print(anime_df)
    return anime_df
# Testing Part

def useKNNwithmeans(user_name, user_feedback=None):
    animation_file_path = PATH.INFO_CSV
    rating_file_path = PATH.RATING_CSV
    user_file_path = PATH.USER_RATING_CSV

    rating_df, animation_df, newu_df = getDatasets(rating_file_path, animation_file_path, user_file_path)

    # Add the user feedback to newu_df
    # check the fields in newu_df, understand what's it
    # and see if  it fit the new layout of user rating    
    if user_feedback is not None:
        positive_score = RATING_THRESHOLD
        negative_score = MAX_RATING - positive_score

        user_feedback.drop('method', axis=1, inplace=True)
        user_feedback.rename(columns = {'user_id':'id', 'username':'uid', 'anime_id':'aid', 'feedback':'usr_rating', 'ts':'timestamp'}, inplace = True)
        user_feedback.loc[user_feedback['usr_rating'] == 1, "usr_rating"] = positive_score
        user_feedback.loc[user_feedback['usr_rating'] == -1, "usr_rating"] = negative_score

        newu_df = pd.concat([newu_df, user_feedback])

    neighbor, watched = finding_neighbors(rating_df, newu_df, user_name, 20)
    rec_id = rec_anime(neighbor, rating_df, watched, 5)
    anime_df = getAnimedf(animation_df,rec_id)

    return anime_df

