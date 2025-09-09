package ru.ci_trainee.authms.exception.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("Passwords do not match");
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
