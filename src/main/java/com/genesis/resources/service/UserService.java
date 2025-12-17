package com.genesis.resources.service;

import com.genesis.resources.dto.UserCreateRequest;
import com.genesis.resources.dto.UserDetailResponse;
import com.genesis.resources.dto.UserResponse;
import com.genesis.resources.dto.UserUpdateRequest;
import com.genesis.resources.exception.InvalidPersonIDException;
import com.genesis.resources.exception.PersonIDAlreadyUsedException;
import com.genesis.resources.exception.UserNotFoundException;
import com.genesis.resources.model.User;
import com.genesis.resources.repository.UserRepository;
import com.genesis.resources.utils.PersonIDProvider;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService
{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(UserRepository repository)
    {
        this.repository = repository;
    }

    public UserDetailResponse create(UserCreateRequest req)
    {
        logger.info("Creating user: name='{}', surname='{}', personID={}",
                req.getName(), req.getSurname(), req.getPersonID());

        if (!PersonIDProvider.isValid(req.getPersonID()))
        {
            logger.warn("Create rejected – invalid personID={}", req.getPersonID());
            throw new InvalidPersonIDException(req.getPersonID());
        }

        if (repository.existsByPersonID(req.getPersonID()))
        {
            logger.warn("Create rejected – personID already used={}", req.getPersonID());
            throw new PersonIDAlreadyUsedException(req.getPersonID());
        }

        String uuid = UUID.randomUUID().toString();
        long id = repository.save(req.getName(), req.getSurname(), req.getPersonID(), uuid);

        logger.info("User created: id={}, uuid={}", id, uuid);

        return new UserDetailResponse(id, req.getName(), req.getSurname(), req.getPersonID(), uuid);
    }

    public UserResponse get(long id)
    {
        logger.debug("Fetching user (basic) id={}", id);

        User u = repository.findById(id).orElseThrow(() ->
        {
            logger.warn("User not found id={} (basic)", id);

            return new UserNotFoundException(id);
        });

        return new UserResponse(u.getId(), u.getName(), u.getSurname());
    }

    public UserDetailResponse getDetail(long id)
    {
        logger.debug("Fetching user (detail) id={}", id);

        User u = repository.findById(id).orElseThrow(() ->
        {
            logger.warn("User not found id={} (detail)", id);

            return new UserNotFoundException(id);
        });

        return new UserDetailResponse(u.getId(), u.getName(), u.getSurname(), u.getPersonID(), u.getUuid());
    }

    public List<UserResponse> getAll()
    {
        logger.debug("Fetching all users (basic)");

        return repository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getSurname()))
                .toList();
    }

    public List<UserDetailResponse> getAllDetail()
    {
        logger.debug("Fetching all users (detail)");

        return repository.findAll().stream()
                .map(u -> new UserDetailResponse(u.getId(), u.getName(), u.getSurname(),
                        u.getPersonID(), u.getUuid()))
                .toList();
    }

    public void update(long id, UserUpdateRequest req)
    {
        logger.info("Updating user id={}", id);

        User u = repository.findById(id).orElseThrow(() ->
        {
            logger.warn("User not found id={} (update)", id);

            return new UserNotFoundException(id);
        });

        String newName = !Strings.isNullOrEmpty(req.getName()) ? req.getName() : u.getName();
        String newSurname = !Strings.isNullOrEmpty(req.getSurname()) ? req.getSurname() : u.getSurname();

        repository.update(id, newName, newSurname);
        logger.info("User updated id={}", id);
    }

    public void delete(long id)
    {
        logger.warn("Deleting user id={}", id);

        if (repository.findById(id).isEmpty())
        {
            logger.warn("User not found id={} (delete)", id);
            throw new UserNotFoundException(id);
        }

        repository.delete(id);
        logger.info("User deleted id={}", id);
    }
}
