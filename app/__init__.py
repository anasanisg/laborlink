from flask import Flask
from dotenv import load_dotenv
import os


load_dotenv()

def initialize():


    os.environ['STORAGE'] = os.path.abspath(os.path.join(os.path.dirname(__file__), "../storage/"))

    if not os.path.exists(os.getenv('STORAGE')):
        os.makedirs(os.getenv('STORAGE'))

    app = Flask(__name__)


    from .routes import main_bp
    app.register_blueprint(main_bp) 

    return app