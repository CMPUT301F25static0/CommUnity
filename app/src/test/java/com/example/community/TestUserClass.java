package com.example.community;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestUserClass {

    private User user;
    private static final String TEST_DEVICE_TOKEN = "test_device_token_123";
    private static final String TEST_USER_ID = "user_123";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PHONE = "123-456-7890";

    @Before
    public void setUp() {
        // Create a fresh user before each test
        user = new User(TEST_DEVICE_TOKEN, TEST_USER_ID, TEST_USERNAME, TEST_EMAIL);
    }

    @Test
    public void testEnableNotifications() {
        user.setReceiveNotifications(false);
        user.enableNotifications();
        assertTrue(user.getReceiveNotifications());
    }

}
