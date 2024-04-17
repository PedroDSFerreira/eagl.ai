package dev.pdsf.eagleai.exception;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(Long id) {
        super("Unable to find contact " + id);
    }
}
