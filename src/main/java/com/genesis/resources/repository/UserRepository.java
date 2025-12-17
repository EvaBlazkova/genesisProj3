package com.genesis.resources.repository;

import com.genesis.resources.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository
{
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public long save(String name, String surname, String personID, String uuid)
    {
        logger.debug("Inserting user into DB: name='{}', surname='{}', personID={}, uuid={}",
                name, surname, personID, uuid);

        String sql = "INSERT INTO Users (Name, Surname, PersonID, Uuid) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, name, surname, personID, uuid);

        Long id = jdbcTemplate.queryForObject("SELECT CAST(SCOPE_IDENTITY() as bigint)", Long.class);

        if (id == null)
        {
            throw new IllegalStateException("Failed to retrieve generated ID after insert");
        }

        logger.debug("User inserted, generated ID={}", id);

        return id;
    }

    public Optional<User> findById(long id)
    {
        logger.debug("Querying user by ID={}", id);

        List<User> result = jdbcTemplate.query("SELECT ID, Name, Surname, PersonID, Uuid FROM Users WHERE ID = ?", this::mapRow, id);

        return result.stream().findFirst();
    }

    public List<User> findAll()
    {
        logger.debug("Querying all users");

        return jdbcTemplate.query("SELECT ID, Name, Surname, PersonID, Uuid FROM Users", this::mapRow);
    }

    public void update(long id, String name, String surname)
    {
        logger.debug("Updating user in DB id={}, name='{}', surname='{}'", id, name, surname);
        jdbcTemplate.update("UPDATE Users SET Name = ?, Surname = ? WHERE ID = ?", name, surname, id);
    }

    public void delete(long id)
    {
        logger.debug("Deleting user from DB id={}", id);
        jdbcTemplate.update("DELETE FROM Users WHERE ID = ?", id);
    }

    public boolean existsByPersonID(String personID)
    {
        logger.debug("Checking existence of personID={} in DB", personID);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Users WHERE PersonID = ?", Integer.class, personID);

        boolean exists = count != null && count > 0;
        logger.debug("PersonID={} exists={}", personID, exists);

        return exists;
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return new User(
                rs.getLong("ID"),
                rs.getString("Name"),
                rs.getString("Surname"),
                rs.getString("PersonID"),
                rs.getString("Uuid"));
    }
}
