package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.validation.ConstraintViolation;
import com.example.validation.Validator;
import com.example.validation.Validation;
import java.util.Set;

public class SimpleUserValidatorTest {
    
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.getValidator(User.class);
    }
    
    @Test
    public void testValidUserExample() {
        User user = new User("test-user", "test@example.com", 24);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "User should be valid");
    }

    @Test
    public void testInvalidUserExample() {
        User user = new User("jo", "invalid-email", 10);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "User should be invalid");
        assertEquals(3, violations.size(), "Should have 3 validation errors");
        
        // Check if we have the expected validation errors
        boolean hasUsernameError = violations.stream()
            .anyMatch(v -> v.getPropertyPath().equals("username"));
        boolean hasEmailError = violations.stream()
            .anyMatch(v -> v.getPropertyPath().equals("email"));
        boolean hasAgeError = violations.stream()
            .anyMatch(v -> v.getPropertyPath().equals("age"));
            
        assertTrue(hasUsernameError, "Should have username validation error");
        assertTrue(hasEmailError, "Should have email validation error");
        assertTrue(hasAgeError, "Should have age validation error");
    }

    @Test
    public void testNullUsername() {
        // Arrange
        User user = new User(null, "valid@example.com", 30);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty(), "User with null username should have constraint violations");

        boolean hasUsernameViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().equals("username"));

        assertTrue(hasUsernameViolation, "Should have username violation for null value");
        assertEquals(1, violations.size(), "Should have exactly 1 validation error");

        // No need for System.out.println in tests unless debugging
    }
} 