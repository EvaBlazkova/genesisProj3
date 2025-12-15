package com.genesis.resources.dto;

import jakarta.validation.constraints.NotBlank;

public class UserCreateRequest
{

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String personID;

    public UserCreateRequest()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getPersonID()
    {
        return personID;
    }

    public void setPersonID(String personID)
    {
        this.personID = personID;
    }
}
