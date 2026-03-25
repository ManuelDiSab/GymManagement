package org.example.gym_management.security.exception;

public class UserNotInstructorException extends RuntimeException {
    public UserNotInstructorException(String message) {
        super(message);
    }
}
