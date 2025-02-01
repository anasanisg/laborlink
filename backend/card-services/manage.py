from flask.cli import FlaskGroup
from app import initialize



app = initialize()

cli = FlaskGroup(app)


if __name__ == "__main__":
    cli()
