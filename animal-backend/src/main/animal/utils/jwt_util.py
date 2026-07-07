import jwt
from datetime import datetime, timedelta

SECRET = "your_super_secret_jwt_key_change_in_production"
EXPIRATION_MS = 86400000

def generate_token(user_id: int, username: str) -> str:
    payload = {
        "sub": str(user_id),
        "username": username,
        "iat": datetime.utcnow(),
        "exp": datetime.utcnow() + timedelta(milliseconds=EXPIRATION_MS)
    }
    return jwt.encode(payload, SECRET, algorithm="HS256")

def get_user_id_from_token(token: str) -> int:
    try:
        payload = jwt.decode(token, SECRET, algorithms=["HS256"])
        return int(payload["sub"])
    except Exception:
        raise ValueError("Invalid token")