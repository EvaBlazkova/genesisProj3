package com.genesis.resources.exception;

public class InvalidPersonIDException extends RuntimeException
{

    public InvalidPersonIDException(String personID)
    {
        super("PersonID '" + personID + "' is not in the list of ids.");
    }
}