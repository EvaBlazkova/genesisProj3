package com.genesis.resources.exception;

public class UserNotFoundException extends RuntimeException
{

    public UserNotFoundException(long id)
    {
        super("User with ID " + id + " does not exist.");
    }
}