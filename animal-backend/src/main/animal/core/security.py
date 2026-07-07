from fastapi import Request, HTTPException, Security
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from utils.jwt_util import get_user_id_from_token

security = HTTPBearer(auto_error=False)

async def jwt_interceptor(credentials: HTTPAuthorizationCredentials = Security(security)) -> int:
    if not credentials:
        raise HTTPException(status_code=401, detail={"code": 401, "message": "Missing token"})
    try:
        return get_user_id_from_token(credentials.credentials)
    except Exception:
        raise HTTPException(status_code=401, detail={"code": 401, "message": "Invalid token"})

async def optional_jwt_interceptor(credentials: HTTPAuthorizationCredentials = Security(security)) -> int | None:
    if credentials:
        try:
            return get_user_id_from_token(credentials.credentials)
        except Exception:
            pass
    return None