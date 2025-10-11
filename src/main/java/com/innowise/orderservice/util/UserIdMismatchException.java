package com.innowise.orderservice.util;

public class UserIdMismatchException extends RuntimeException {
    private static final String MESSAGE = "Provide correct userId";

    public UserIdMismatchException() {
        super(MESSAGE);
    }
}
