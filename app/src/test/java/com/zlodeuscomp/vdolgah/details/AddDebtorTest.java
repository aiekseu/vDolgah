package com.zlodeuscomp.vdolgah.details;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Tests to EmailValidation methods
 */

public class AddDebtorTest {

    @Test
    public void put_invalid_email_to_isEmailValid() {
        assertFalse(AddDebtor.isEmailValid("qwerty"));
        assertFalse(AddDebtor.isEmailValid("qwerty@mail"));
        assertFalse(AddDebtor.isEmailValid("@mail.ru"));
        assertFalse(AddDebtor.isEmailValid("абв@mail.ru"));
    }

    @Test
    public void put_valid_email_to_isEmailValid() {
        assertTrue(AddDebtor.isEmailValid("abc@abc.ru"));
    }
}