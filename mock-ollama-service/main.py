import time
from datetime import datetime, timezone

from flask import Flask, jsonify, request

DELAY = 1  # Simulate processing delay (seconds)

RESPONSE = """
age: CHILD
sex: MALE
skinTone: DARK
eyeColor: BROWN
hairType: LONG
hairColor: BLACK
facialHair: TRUE
"""
app = Flask(__name__)


@app.route("/api/chat", methods=["POST"])
def generate():
    data = request.get_json()
    model = data.get("model", "default-model")
    time.sleep(DELAY)
    response = {
        "model": model,
        "created_at": datetime.now(timezone.utc).isoformat(),
        "message": {"role": "assistant", "content": RESPONSE},
        "done_reason": "load",
        "done": True,
    }
    return jsonify(response)


if __name__ == "__main__":
    app.run(port=11434, host="0.0.0.0")
