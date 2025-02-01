from pymongo import MongoClient
import os 


client = MongoClient(os.getenv("DB_CLIENT"))  
db = client.invoice_db
collection = db.invoices

def sv_in_collection(json):
    ret = collection.insert_one(json)
    return {"inserted_id": str(ret.inserted_id)}


def get_all_invoices_related_to_owner(id):

   invoices = collection.find({"ownerId": id})
    
   ret = []

   for invoice in invoices:
        invoice["_id"] = str(invoice["_id"])
        ret.append(invoice)
    
   return ret