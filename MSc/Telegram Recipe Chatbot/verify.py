from newspaper import Article
from sympy import print_glsl
import load_database
import urllib.request
import re
import random

def verifyURL(url,text):
    article = Article(url)
    article.download()
    article.parse()
    article.nlp()
    key_list = article.keywords
    text = text.lower()
    for keyword in key_list:
        if keyword.lower() == text:
            return True
    return False

def verifyExists(url,document_name):
    docs = load_database.getCookingdata(document_name)
    if docs == False:
        return False
    else:
        for doc in docs:
            if doc.to_dict()['url']==url:
                return True
        return False

def searchURL(search_keyword):
    html = urllib.request.urlopen("https://www.youtube.com/results?search_query=" + search_keyword+'+recipe')
    video_ids = re.findall(r"watch\?v=(\S{11})", html.read().decode())
    index = random.randint(0,len(video_ids)-1)
    url = "https://www.youtube.com/watch?v=" + video_ids[index] 
    return url

# res = verifyURL("https://www.youtube.com/watch?v=TPKqIScLKmk",'CUpcake')
# print(res)
# print(searchURL('coffee'))