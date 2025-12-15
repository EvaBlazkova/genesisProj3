package com.genesis.resources.exception;

public class PersonIDAlreadyUsedException extends RuntimeException
{

    public PersonIDAlreadyUsedException(String personID)
    {
        super("PersonID '" + personID + "' is already in use.");
    }
}
