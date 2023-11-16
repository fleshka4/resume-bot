package com.resume.bot.exception.json;

public class JsonValidationException extends RuntimeException {
    public JsonValidationException() {
        super();
    }

    public JsonValidationException(String message) {
        super(message);
    }
}
