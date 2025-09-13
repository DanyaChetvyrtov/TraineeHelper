package ru.core.profilems.exception.exception;

public class PageNotFound extends RuntimeException {
    public PageNotFound() {}

    public PageNotFound(String message) {
        super(message);
    }
}
