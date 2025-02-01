from kafka import KafkaConsumer
from .driver import storeInStorage
from .mongo_client import sv_in_collection
import sys,json,os



def listen():

    consumer = KafkaConsumer(
    'activity.complete.events',
    bootstrap_servers=[os.getenv('KAFKA_BOOTSTRAP_SERVER')],  
    group_id='invoices',
    auto_offset_reset='latest',  
    enable_auto_commit=True,
    value_deserializer=lambda x: x.decode('utf-8'),  
)


    for message in consumer:
        print(f"CONSUMED MESSAGE : {message.value}",file=sys.stderr)
        ret = storeInStorage(json.loads(message.value))
        sv_in_collection(ret)
    pass



