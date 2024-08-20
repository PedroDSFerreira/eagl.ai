package dev.pdsf.eaglai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OllamaParseResponseException extends RuntimeException {
    public OllamaParseResponseException(String ollamaResponse) {
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Unable to parse ollama response: '" + ollamaResponse.strip() + "'", this);
    }
}
