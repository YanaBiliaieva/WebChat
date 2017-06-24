package com.course.mvc.exceptions;

/**
 * Created by Admin on 27.05.2017.
 */
public class UserSaveException extends RuntimeException {
    public UserSaveException(String message) {
        super(message);
    }
}