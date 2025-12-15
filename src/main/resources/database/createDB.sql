
IF NOT EXISTS (SELECT name
               FROM sys.databases
               WHERE name = N'tst')
    BEGIN
        CREATE DATABASE tst;
    END;
GO

USE tst;
GO

IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL
    BEGIN
        DROP TABLE dbo.Users;
    END;
GO

CREATE TABLE dbo.Users
(
    ID       BIGINT IDENTITY (1,1) NOT NULL,
    Name     VARCHAR(255)          NOT NULL,
    Surname  VARCHAR(255)          NOT NULL,
    PersonId VARCHAR(12)           NOT NULL,
    Uuid     VARCHAR(36)           NOT NULL,

    CONSTRAINT PK_Users PRIMARY KEY CLUSTERED (ID),
    CONSTRAINT UQ_Users_PersonId UNIQUE (PersonId),
    CONSTRAINT UQ_Users_Uuid UNIQUE (Uuid)
);
GO
