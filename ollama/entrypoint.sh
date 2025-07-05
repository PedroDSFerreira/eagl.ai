#!/bin/bash

# Start Ollama in the background.
/bin/ollama serve &
# Record Process ID.
pid=$!

# Pause for Ollama to start.
sleep 5

echo "🔴 Retrieve ${OLLAMA_MODEL} model..."
ollama pull $OLLAMA_MODEL
echo "🟢 Done!"

# Wait for Ollama process to finish.
wait $pid
