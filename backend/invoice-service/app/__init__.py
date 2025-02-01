from flask import Flask
from dotenv import load_dotenv
from .kafka_client import listen
import os
import threading


load_dotenv()

def initialize():

    os.environ['STORAGE'] = os.path.abspath(os.path.join(os.path.dirname(__file__), "../storage/"))
    os.environ['LOGO_PATH'] = os.path.abspath(os.path.join(os.path.dirname(__file__), "../LaborLink.png"))

    if not os.path.exists(os.getenv('STORAGE')):
        os.makedirs(os.getenv('STORAGE'))
  
    app = Flask(__name__)

    from .routes import main_bp
    app.register_blueprint(main_bp) 


    return app


def start_kafka():
    listen()
    pass