package com.pds.localpos.userservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserServiceApplicationTest {

    @Test
    void main_shouldStartApplication() {
        assertDoesNotThrow(() -> UserServiceApplication.main(new String[]{}));
    }
}

