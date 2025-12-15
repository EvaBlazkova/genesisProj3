package com.genesis.resources.service;

import com.genesis.resources.dto.UserCreateRequest;
import com.genesis.resources.dto.UserUpdateRequest;
import com.genesis.resources.exception.InvalidPersonIDException;
import com.genesis.resources.exception.PersonIDAlreadyUsedException;
import com.genesis.resources.exception.UserNotFoundException;
import com.genesis.resources.model.User;
import com.genesis.resources.repository.UserRepository;
import com.genesis.resources.utils.PersonIDProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest
{

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @BeforeAll
    static void initPersonIDs()
    {
        new PersonIDProvider().load();
    }

    @BeforeEach
    void resetMocks()
    {
        reset(repository);
    }

    @Test
    void createThrowInvalidPersonIDPersonIDNotInlist()
    {
        UserCreateRequest req = new UserCreateRequest();
        req.setName("John");
        req.setSurname("Doe");
        req.setPersonID("XXXXXXXXXXXX");

        assertThrows(InvalidPersonIDException.class, () -> service.create(req));

        verify(repository, never()).existsByPersonID(anyString());
        verify(repository, never()).save(any(), any(), any(), any());
    }

    @Test
    void createThrowPersonIDAlreadyUsedPersonIDExistsInDb()
    {
        UserCreateRequest req = new UserCreateRequest();
        req.setName("John");
        req.setSurname("Doe");
        req.setPersonID("jXa4g3H7oPq2");

        when(repository.existsByPersonID(req.getPersonID())).thenReturn(true);

        assertThrows(PersonIDAlreadyUsedException.class,
                () -> service.create(req));

        verify(repository).existsByPersonID("jXa4g3H7oPq2");
        verify(repository, never()).save(any(), any(), any(), any());
    }

    @Test
    void createReturnDetailResponsePersonIDValidAndNotUsed()
    {
        UserCreateRequest req = new UserCreateRequest();
        req.setName("John");
        req.setSurname("Doe");
        req.setPersonID("jXa4g3H7oPq2");

        when(repository.existsByPersonID(req.getPersonID())).thenReturn(false);
        when(repository.save(eq("John"), eq("Doe"), eq("jXa4g3H7oPq2"), anyString()))
                .thenReturn(10L);

        var response = service.create(req);

        assertEquals(10L, response.getId());
        assertEquals("John", response.getName());
        assertEquals("Doe", response.getSurname());
        assertEquals("jXa4g3H7oPq2", response.getPersonID());
        assertNotNull(response.getUuid());
        assertFalse(response.getUuid().isBlank());

        verify(repository).existsByPersonID("jXa4g3H7oPq2");
        verify(repository).save(eq("John"), eq("Doe"), eq("jXa4g3H7oPq2"), anyString());
    }

    @Test
    void getReturnResponseUserExists()
    {
        User user = new User(1L, "Alice", "Smith", "jXa4g3H7oPq2", "uuid-123");
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        var response = service.get(1L);

        assertEquals(1L, response.getId());
        assertEquals("Alice", response.getName());
        assertEquals("Smith", response.getSurname());
    }

    @Test
    void getThrowUserNotFoundUserDoesNotExist()
    {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.get(999L));
    }

    @Test
    void getDetailReturnDetailResponseUserExists()
    {
        User user = new User(5L, "Bob", "Brown", "jXa4g3H7oPq2", "uuid-xyz");
        when(repository.findById(5L)).thenReturn(Optional.of(user));

        var response = service.getDetail(5L);

        assertEquals(5L, response.getId());
        assertEquals("Bob", response.getName());
        assertEquals("Brown", response.getSurname());
        assertEquals("jXa4g3H7oPq2", response.getPersonID());
        assertEquals("uuid-xyz", response.getUuid());
    }

    @Test
    void updateCallRepositoryUpdateWithMergedValues()
    {
        User existing = new User(3L, "OldName", "OldSurname", "jXa4g3H7oPq2", "uuid-123");
        when(repository.findById(3L)).thenReturn(Optional.of(existing));

        UserUpdateRequest req = new UserUpdateRequest();
        req.setName("NewName");
        req.setSurname(null);

        service.update(3L, req);

        verify(repository).update(3L, "NewName", "OldSurname");
    }

    @Test
    void updateThrowUserNotFoundUserDoesNotExist()
    {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        UserUpdateRequest req = new UserUpdateRequest();
        req.setName("NewName");

        assertThrows(UserNotFoundException.class,
                () -> service.update(3L, req));
    }

    @Test
    void deleteDeleteWhenUserExists()
    {
        User existing = new User(7L, "John", "Doe", "jXa4g3H7oPq2", "uuid-xyz");
        when(repository.findById(7L)).thenReturn(Optional.of(existing));

        service.delete(7L);

        verify(repository).delete(7L);
    }

    @Test
    void deleteThrowUserNotFoundWhenUserDoesNotExist()
    {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.delete(7L));

        verify(repository, never()).delete(anyLong());
    }

    @Test
    void getAllReturnListOfUsers()
    {
        User u1 = new User(1L, "A", "B", "jXa4g3H7oPq2", "uuid-1");
        User u2 = new User(2L, "C", "D", "yB9fR6tK0wLm", "uuid-2");
        when(repository.findAll()).thenReturn(java.util.List.of(u1, u2));

        var result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("C", result.get(1).getName());
    }

    @Test
    void getAllDetailReturnDetailList()
    {
        User u1 = new User(1L, "A", "B", "jXa4g3H7oPq2", "uuid-1");
        User u2 = new User(2L, "C", "D", "yB9fR6tK0wLm", "uuid-2");
        when(repository.findAll()).thenReturn(java.util.List.of(u1, u2));

        var result = service.getAllDetail();

        assertEquals(2, result.size());
        assertEquals("jXa4g3H7oPq2", result.get(0).getPersonID());
        assertEquals("uuid-2", result.get(1).getUuid());
    }
}
