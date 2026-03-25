package org.example.gym_management.security.exception;

public class ClassIsFullException extends RuntimeException {
    public ClassIsFullException(String message) {
        super(message);
    }
}
