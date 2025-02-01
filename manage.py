from flask.cli import FlaskGroup
from app import initialize,start_kafka



app = initialize()
start_kafka()


cli = FlaskGroup(app)


if __name__ == "__main__":
    cli()
    

