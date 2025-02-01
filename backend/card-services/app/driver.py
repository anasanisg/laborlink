import jwt,os
import qrcode
from datetime import datetime, timedelta

SECRET_KEY = "LaborLink"  
ALGORITHM = "HS256"  

def generate_jwt(user_name, expiration_minutes=60):

    payload = {'username':user_name}

    payload['exp'] = datetime.utcnow() + timedelta(minutes=expiration_minutes)

    token = jwt.encode(payload, SECRET_KEY, algorithm=ALGORITHM)
    
    return token

def save_qr_code(data, filename):
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(data)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white")
    img.save(os.path.join(os.getenv('STORAGE'), f"card_{filename}"))

