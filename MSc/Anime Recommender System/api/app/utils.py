from surprise import Dataset
from surprise import Reader
from surprise import KNNWithMeans
from surprise import accuracy
from surprise.model_selection import train_test_split
import random
import pandas as pd
import os

# Read files from CSV
def getDatasets(rating_file_path, animation_file_path):
    rating_df = pd.read_csv(rating_file_path, names = ['uid','aid','usr_rating'], sep=',',dtype={'uid':int,'usr_rating':int},skiprows=1)
    animation_df = pd.read_csv(animation_file_path, names=['aid','aname','genre','atype','episode','anime_rating','members'],sep=',',dtype={'anime_rating':float,'members':int},skiprows=1)
    return rating_df, animation_df

# Collaborative Filtering Part-----This function have severe logic problems cost too much running time and can not guarantee 100% correctness in finding the best neighbors
def KNNwithmeans_neighbors(rating_df, animation_df, uid, k):
    clean_data = rating_df.drop(rating_df[rating_df['usr_rating']==-1].index)
    user_data = clean_data.loc[clean_data['uid']==uid]
    clean_data = clean_data.drop(user_data.index)
    print(user_data)
    print(clean_data)
    # Split the data for calculation 
    clean_data = clean_data.sample(frac=1)
    temp = int(len(clean_data)/200)
    splited_data = []
    for i in range(200):
        splited_data.append(pd.concat([clean_data.iloc[i*temp:(i+1)*temp],user_data]))

    reader = Reader(rating_scale=(1, 10))
    sim_options = {'name': 'cosine',
               'user_based': True,  # compute  similarities between users
               'min_support': 1  # minimum number of common items for two users
            }
    algo = KNNWithMeans(k=50, sim_options=sim_options)

    neighbors_list = {}
    for data_block in splited_data:
        data = Dataset.load_from_df(data_block[["uid", "aid", "usr_rating"]], reader)
        train_set = data.build_full_trainset()
        # fit training data
        algo.fit(train_set)
        inner_id = algo.trainset.to_inner_uid(uid)
        user_neighbors = algo.get_neighbors(inner_id,k)
        
        for inner in user_neighbors:
            temp = algo.trainset.to_raw_uid(inner)
            if temp in neighbors_list:
                neighbors_list[temp] += 1
            else:
                neighbors_list[temp] = 1

    result = sorted(neighbors_list)
    return result[:k]

def Finding_neighbors(rating_df, animation_df, uid, k):
    clean_data = rating_df.drop(rating_df[rating_df['usr_rating']==-1].index)
    user_data = clean_data.loc[clean_data['uid']==uid]
    clean_data = clean_data.drop(user_data.index)
    print(user_data)
    print(clean_data)
    # Split the data for calculation 
    temp = int(len(clean_data)/20)
    block_i = random.randint(0,19)
    data_block = pd.concat([clean_data.iloc[block_i*temp:(block_i+1)*temp],user_data])

    reader = Reader(rating_scale=(1, 10))
    sim_options = {'name': 'cosine',
               'user_based': True,  # compute  similarities between users
               'min_support': 1  # minimum number of common items for two users
            }
    algo = KNNWithMeans(k=50, sim_options=sim_options)

    data = Dataset.load_from_df(data_block[["uid", "aid", "usr_rating"]], reader)
    train_set = data.build_full_trainset()
    # fit training data
    algo.fit(train_set)
    inner_id = algo.trainset.to_inner_uid(uid)
    user_neighbors = algo.get_neighbors(inner_id,k)
    true_neighbor_id = []
    for inner in user_neighbors:
        true_neighbor_id.append(algo.trainset.to_raw_uid(inner)) 
    print(true_neighbor_id)
    return true_neighbor_id

def Rec_anime(usr_neighbors, rating_df, k):
    res = {}
    for usr in usr_neighbors:
        usr_rating = rating_df[rating_df['uid']==usr]
        anime_rec = usr_rating[usr_rating['usr_rating']>=8]
        for anime in anime_rec['aid']:
            if anime in res:
                res[anime] += 1
            else:
                res[anime] = 1

    sort_res = sorted(res.items(), key=lambda x: x[1], reverse=True)
    print(res)
    print(sort_res[:k])
    return sort_res[:k]

### Testing Part 

str1= './data/anime.csv'
str2 = './data/rating.csv'

a,b = getDatasets(str2,str1)   
neighbor = Finding_neighbors(a,b,1,20)
Rec_anime(neighbor,a,5)

