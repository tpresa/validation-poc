package com.example.validation;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

public class Validator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    @SuppressWarnings("unchecked")
    public <T> Set<ConstraintViolation<T>> validate(T object) {
        Set<ConstraintViolation<T>> violations = new HashSet<>();
        
        // Process annotations using reflection first
        processAnnotations(object, violations);
        
        return violations;
    }
    
    @SuppressWarnings("unchecked")
    private <T> void processAnnotations(T object, Set<ConstraintViolation<T>> violations) {
        try {
            // Get all declared fields from the object's class
            Field[] fields = object.getClass().getDeclaredFields();
            
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(object);
                
                // Process @NotNull annotation
                if (field.isAnnotationPresent(NotNull.class)) {
                    NotNull notNull = field.getAnnotation(NotNull.class);
                    if (fieldValue == null) {
                        String message = notNull.message();
                        violations.add((ConstraintViolation<T>) 
                            new ConstraintViolation<>(fieldName, message, object));
                    }
                }
                
                // Process @Size annotation
                if (field.isAnnotationPresent(Size.class)) {
                    Size size = field.getAnnotation(Size.class);
                    if (fieldValue instanceof String) {
                        String stringValue = (String) fieldValue;
                        if (stringValue.length() < size.min()) {
                            String message = size.message();
                            violations.add((ConstraintViolation<T>) 
                                new ConstraintViolation<>(fieldName, message, object));
                        }
                        // Add max size check if needed: else if (stringValue.length() > size.max()) { ... }
                    }
                }
                
                // Process @Email annotation
                if (field.isAnnotationPresent(Email.class)) {
                    Email emailAnnotation = field.getAnnotation(Email.class);
                    if (fieldValue instanceof String) {
                        String stringValue = (String) fieldValue;
                        if (!EMAIL_PATTERN.matcher(stringValue).matches()) {
                            String message = emailAnnotation.message();
                            violations.add((ConstraintViolation<T>) 
                                new ConstraintViolation<>(fieldName, message, object));
                        }
                    } else if (fieldValue == null) {
                        // Handle null email if necessary, though @NotNull should cover this
                        // violations.add(...);
                    }
                }

                // Process @Min annotation
                if (field.isAnnotationPresent(Min.class)) {
                    Min min = field.getAnnotation(Min.class);
                    if (fieldValue instanceof Number) {
                        Number numValue = (Number) fieldValue;
                        if (numValue.longValue() < min.value()) {
                            String message = min.message();
                            violations.add((ConstraintViolation<T>) 
                                new ConstraintViolation<>(fieldName, message, object));
                        }
                    }
                }

                // Process @Max annotation
                if (field.isAnnotationPresent(Max.class)) {
                    Max max = field.getAnnotation(Max.class);
                    if (fieldValue instanceof Number) {
                        Number numValue = (Number) fieldValue;
                        if (numValue.longValue() > max.value()) {
                            String message = max.message();
                            violations.add((ConstraintViolation<T>) 
                                new ConstraintViolation<>(fieldName, message, object));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing field values", e);
        }
    }
} 