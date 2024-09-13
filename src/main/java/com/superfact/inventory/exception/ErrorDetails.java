package com.superfact.inventory.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetails {
    private int statusCode;
    private String message;

    public ErrorDetails(int statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
    }
}
