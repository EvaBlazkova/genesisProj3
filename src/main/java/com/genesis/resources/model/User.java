package com.genesis.resources.model;

public class User
{
    private long id;
    private String name;
    private String surname;
    private String personID;
    private String uuid;

    public User(long id, String name, String surname, String personID, String uuid)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = uuid;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
}