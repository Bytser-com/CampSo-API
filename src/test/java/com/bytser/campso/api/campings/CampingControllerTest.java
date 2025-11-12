package com.bytser.campso.api.campings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CampingControllerTest {

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
    void setUp() {
        // Prepare the needed objects or others before executing the tests
    }

    @Test
    @DisplayName("This is a example test, tests should be implemented correctly")
    void ExampleTest() {
        // Arrange
        //object.setDurationSeconds(60);

        // Act
        //String durationCategory = object.getDurationCategory();

        // Assert
        assertEquals("TEST", "EXAMPLE", "This is a example test and will not work.");
    }

}
