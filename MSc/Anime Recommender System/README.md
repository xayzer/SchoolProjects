# Known Bug
- [ ] Category page sometimes has ValueError. May due to missing value.
- [ ] Seems above issue happen on other page sometimes

#### Temp solution
set reload flag on that page
>http://localhost:8080/category?reload=True

# Tasks
- [X] Credential Logic
- [X] Project Setup
- [X] Project Structure
- [x] Login page
- [x] Genre/Category page
- [x] Rating page
- [X] Recommend page
- [X] Admin/Stat page (Optional)
- [X] UI Polish/Enhance interactive
- [X] Recommendation method 1
- [X] Recommendation method 2

# Data Set
Root folder
>./api/dataset/

- [anime_info.csv](https://www.kaggle.com/datasets/CooperUnion/anime-recommendations-database?select=anime.csv)
- [anime_rating.csv](https://www.kaggle.com/datasets/CooperUnion/anime-recommendations-database?select=rating.csv)
- [anime_thumbnail.csv](https://drive.google.com/file/d/1zcc1nQMKxnuhV1g7TdM9yWNObtxl7fLD/view?usp=sharing) **(Restricted)**
- user_rating.csv **(Generate by code)**
- user_feedback.csv **(Generate by code)**

Paths are stored at `./api/app/constants.py`

# Setup and run the web app
### Access locally
#### Method 1 (Simple method)
1. Install Docker
2. At root folder, the folder which has docker-compose.yaml. Run the following command
>docker compose up

#### Method 2
1. run `npm install` at **web** folder
2. run `pip install requirements.txt` at **api** folder
3. At **api** folder `uvicorn app.main:app --host 0.0.0.0 --port 3080 --reload `
4. At **web** folder run `npm run watch`


# Usage
Frontend
>http://localhost:8080


Backend
>http://localhost:3080
