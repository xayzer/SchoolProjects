from .constants import PATH
import pandas as pd


# Should be dirty in this way, not sure a proper way
# Process Genre list during import/init
genre_df = pd.read_csv(PATH.INFO_CSV)
genre_df['genre'] = genre_df.genre.str.split(', ')

# df with anime_id and genre, remove those without genre
genre_df = genre_df[['anime_id', 'genre']].dropna()

# create list of genere
genre_list = [] # store the occurred genres

for index, row in genre_df.iterrows():
    for genre in row['genre']:
        # put each genre in each column and give 1 and 0
        # genre_df.at[index, genre] = 1
        if genre not in genre_list:            
            genre_list.append(genre)

try:
    genre_list.remove('Hentai')
    genre_list.remove('Yaoi')
    genre_list.remove('Yuri')
except ValueError:
    pass


genre_list.sort()

#thumbnail df
thumbnail_df = pd.read_csv(PATH.THUMBNAIL_CSV)
thumbnail_df = thumbnail_df.dropna()

# pre-render genre option json
return_json = []
for genre in genre_list:
    # randomly find 3 anime id with such genre
    tmp_list = list(filter(lambda x: genre in x, genre_df['genre'].values))        
    filter_genre_df = genre_df[genre_df['genre'].isin(tmp_list)].sample(n=3)
    filter_genre_df = filter_genre_df.fillna('test_value_err_bug')

    return_json.append({
        'title': genre,
            'covers': list(thumbnail_df[thumbnail_df['anime_id'].isin(filter_genre_df['anime_id'])]['thumbnail_link'].values),
    })

print('====Genre.py genre_df.info()====')
print(genre_df.info())

class Genre:
    def options_in_json(reload=False):
        tmp_return_json = []
        if reload:
            try:
                for genre in genre_list:
                    # randomly find 3 anime id with such genre
                    tmp_list = list(filter(lambda x: genre in x, genre_df['genre'].values))        
                    filter_genre_df = genre_df[genre_df['genre'].isin(tmp_list)].sample(n=3)
                    filter_genre_df = filter_genre_df.fillna('test_value_err_bug')

                    tmp_return_json.append({
                        'title': genre,
                            'covers': list(thumbnail_df[thumbnail_df['anime_id'].isin(filter_genre_df['anime_id'])]['thumbnail_link'].values),
                    })
                print('====Genre.py options_in_json genre_df.info()====')                    
                print(filter_genre_df.info())
            except ValueError:
                print('Genre - options_in_json Value Error')
                tmp_return_json = return_json

        return tmp_return_json if len(tmp_return_json)>0 else return_json