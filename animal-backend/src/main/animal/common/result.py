from typing import TypeVar, Generic, Optional
from pydantic import BaseModel

T = TypeVar('T')

class Result(BaseModel, Generic[T]):
    code: int
    message: str
    data: Optional[T] = None

    @classmethod
    def success(cls, data: Optional[T] = None, message: str = "success") -> "Result[T]":
        return cls(code=200, message=message, data=data)

    @classmethod
    def error(cls, message: str, code: int = 500) -> "Result":
        return cls(code=code, message=message)