package dev.pdsf.eaglai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DescriptionNotFoundException extends RuntimeException {
    public DescriptionNotFoundException(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Unable to find description with id " + id, this);
    }
}
