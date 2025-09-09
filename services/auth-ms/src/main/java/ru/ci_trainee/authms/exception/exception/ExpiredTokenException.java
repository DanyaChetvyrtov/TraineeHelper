package ru.zuzex.practice.authms.exception.exception;

public class ExpiredTokenException extends RuntimeException{
    public ExpiredTokenException() {
        super("Token has been expired");
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
