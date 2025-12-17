package com.genesis.resources.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonIDProviderTest
{

    @BeforeAll
    static void init()
    {
        new PersonIDProvider().load();
    }

    @Test
    void isValid_shouldReturnFalse()
    {
        assertFalse(PersonIDProvider.isValid("ID"));
    }

    @Test
    void isValid_shouldReturnTrue()
    {
        assertTrue(PersonIDProvider.isValid("tQdG2kP3mJfB"));
    }
}
