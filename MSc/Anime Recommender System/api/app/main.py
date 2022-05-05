from fastapi import Request, FastAPI, status, Body
from fastapi.middleware.cors import CORSMiddleware
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse, FileResponse
import logging
import json

from typing import List
from .genre import Genre
from .rating import Rating, User, AnimeRating
from .recommend import Recommend, AnimeFeedback
from .evaluation import Evaluation
from .constants import PATH


app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# DEBUG use
@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError):
	exc_str = f'{exc}'.replace('\n', ' ').replace('   ', ' ')
	logging.error(f"{request}: {exc_str}")
	content = {'status_code': 10422, 'message': exc_str, 'data': None}
	return JSONResponse(content=content, status_code=status.HTTP_422_UNPROCESSABLE_ENTITY)


# == == == == == == == == == API == == == == == == == == == == =

@app.get("/")
def get_index():
    return { 'msg': 'API is on'}

@app.get("/api/genre")
def get_genre(reload: bool = False):    
    return Genre.options_in_json(reload)  

@app.get("/api/anime/rating")
def get_anime_rating_list(genre_list: str = ''):
    genre_list = genre_list.split(',')    
    return Rating.anime_list_rating(genre_list)

@app.post("/api/anime/rating")
async def add_user_anime_rating_list(user: User = None, anime_rating: List[AnimeRating] = None):
    Rating.store_user_anime_rating_list(user, anime_rating)    
    return {'succee':True}

@app.get("/api/anime/recommend")
def get_anime_recommend_list(username: str = '', genre_list: str = '', method: str = None):
    genre_list = genre_list.split(',')
    return Recommend.anime_list_recommend(username=username, genre_list=genre_list, method=method)

@app.post("/api/anime/recommend")
def get_anime_recommend_list_with_feedback(user: User = None, method: str = Body(...), anime_feedback: List[AnimeFeedback] = None, count: int = Body(...)):    

    username = user["username"]
    genre_list = user['category']

    feedback_df = Recommend.store_feedback_result(username, anime_feedback, method, count)
    if count == 2:
        return  {'sccess': True}   
    return Recommend.anime_list_recommend(username, genre_list, feedback_df, method=method)  
    
@app.get("/api/evaluation/")
def get_both_method_stat():
    try:
        return Evaluation.get_both_method_stat()
    except Exception as error:        
        error_string = repr(error)   
        return {'success': False, 'msg':error_string}

@app.get("/api/evaluation/{method}")
def get_method_stats(method):
    try:        
        return Evaluation.get_method_stat(method=method)
    except Exception as error:        
        error_string = repr(error)   
        return {'success': False, 'msg':error_string}


@app.get("/api/evaluation/file/feedback", response_class=FileResponse)
def get_user_feedback_file():
    return PATH.USER_FEEDBACK_CSV

@app.get("/api/evaluation/file/rating", response_class=FileResponse)
def get_user_feedback_file():
    return PATH.USER_RATING_CSV    