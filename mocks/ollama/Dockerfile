# Dockerfile for Ollama mock server
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY main.py ./

EXPOSE 11434

CMD ["python", "main.py"]
