package com.innowise.orderservice.util;

public class ItemNotFoundException extends RuntimeException{
    private static final String MESSAGE = "Item not found";

    public ItemNotFoundException() {
        super(MESSAGE);
    }
}
