import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

cred = credentials.Certificate('./serviceAccount.json')
firebase_admin.initialize_app(cred)
db = firestore.client()


def getCookingdata(document_name):
    collection_name = "Cooking"
    doc_ref = db.collection(collection_name).document(document_name).collection("URLS")
    docs = doc_ref.get()
    if(len(docs)!=0):
        # for doc in docs:
        #     print("文件內容：{}".format(doc.to_dict()))
        return docs
    else:
        return False

def setCookingdata(document_name, url):
    collection_name = "Cooking"
    add_doc_ref = db.collection(collection_name).document(document_name).collection("URLS")
    t = len(add_doc_ref.get()) + 1
    url_id = ""+"URL_"+ str(t)
    add_doc_ref = add_doc_ref.document(url_id)  
    print(url_id)
    doc = {
        "id":url_id,
        "type": document_name,
        "liked":0,
        "url":url
    }
    
    add_doc_ref.set(doc)
    return

def updateLiked(document_name, url_id):
    collection_name = "Cooking"
    update_doc_ref = db.collection(collection_name).document(document_name).collection("URLS").document(url_id)
    doc = update_doc_ref.get()
    temp_dict = doc.to_dict()
    temp_dict['liked'] += 1
    update_doc_ref.update(temp_dict)
    return

def sortUrl(docs):
    sorted_doc = sorted(docs,key=lambda e: e.__getitem__('liked'),reverse=True)
    return sorted_doc

document_name = "Cupcake"
#getCookingdata(document_name)
# setCookingdata("Cupcake", "https://www.youtube.com/watch?v=TPKqIScLKmk")
# setCookingdata("Cupcake", "https://www.youtube.com/watch?v=VCMCsSdtkqg")
# setCookingdata("Burrito", "https://www.youtube.com/watch?v=VCMCsSdtkqg")
# updateLiked(document_name,"URL_1")