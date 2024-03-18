package com.resume.unit.bot.service;

import com.resume.bot.model.entity.User;
import com.resume.bot.repository.UserRepository;
import com.resume.bot.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveUserNewUser() {
        User newUser = new User();
        when(repository.findById(newUser.getTgUid())).thenReturn(Optional.empty());

        when(repository.save(newUser)).thenReturn(newUser);
        User savedUser = userService.saveUser(newUser);

        assertNotNull(savedUser);
        assertEquals(newUser, savedUser);
        verify(repository, times(1)).findById(newUser.getTgUid());
        verify(repository, times(1)).save(newUser);
    }

    @Test
    public void testSaveUserExistingUser() {
        User existingUser = new User();
        when(repository.findById(existingUser.getTgUid())).thenReturn(Optional.of(existingUser));

        User savedUser = userService.saveUser(existingUser);

        assertNotNull(savedUser);
        assertEquals(existingUser, savedUser);
        verify(repository, times(1)).findById(existingUser.getTgUid());
        verify(repository, never()).save(existingUser);
    }

    @Test
    public void testGetUserExists() {
        User existingUser = new User();

        when(repository.findById(existingUser.getTgUid())).thenReturn(Optional.of(existingUser));
        User retrievedUser = userService.getUser(existingUser.getTgUid());

        assertNotNull(retrievedUser);
        assertEquals(existingUser, retrievedUser);
    }

    @Test
    public void testGetUserNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(123L));
    }
}
