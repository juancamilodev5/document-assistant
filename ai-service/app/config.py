from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    openai_api_key: str
    qdrant_host: str = "localhost"
    qdrant_port: int = 6333
    spring_boot_url: str = "http://backend:8080"
    collection_name: str = "documents"
    internal_api_key: str

    class Config:
        env_file = ".env"

settings = Settings()