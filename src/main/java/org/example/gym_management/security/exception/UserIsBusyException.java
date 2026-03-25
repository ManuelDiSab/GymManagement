package org.example.gym_management.security.exception;

public class UserIsBusyException extends RuntimeException {
    public UserIsBusyException(String message) {
        super(message);
    }
}
