package com.example.validation;

import java.util.HashMap;
import java.util.Map;

public class Validation {
    private static final Map<Class<?>, Validator> validators = new HashMap<>();
    
    static {
        // Register the Validator (previously UserValidator)
        validators.put(com.example.User.class, new Validator());
    }
    
    public static Validator getValidator(Class<?> clazz) {
        Validator validator = validators.get(clazz);
        if (validator == null) {
            throw new IllegalArgumentException("No validator found for class: " + clazz.getName());
        }
        return validator;
    }
    
    public static Validator getDefaultValidator() {
        return validators.values().iterator().next();
    }
} 