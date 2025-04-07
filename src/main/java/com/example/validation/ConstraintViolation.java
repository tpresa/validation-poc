package com.example.validation;

public class ConstraintViolation<T> {
    private final String propertyPath;
    private final String message;
    private final T rootBean;

    public ConstraintViolation(String propertyPath, String message, T rootBean) {
        this.propertyPath = propertyPath;
        this.message = message;
        this.rootBean = rootBean;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public String getMessage() {
        return message;
    }

    public T getRootBean() {
        return rootBean;
    }

    @Override
    public String toString() {
        return propertyPath + ": " + message;
    }
} 