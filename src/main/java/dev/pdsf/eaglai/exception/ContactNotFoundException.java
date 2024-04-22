package dev.pdsf.eaglai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Unable to find contact with id " + id, this);
    }
}
