package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends IntegrationBaseTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @Test
    public void testSaveUserNewUser() {
        User user = User.builder()
                .tgUid(1L)
                .build();

        userService.saveUser(user);

        User actualUser = repository.findById(user.getTgUid()).get();

        assertEquals(user.getTgUid(), actualUser.getTgUid());
    }

    @Test
    public void testSaveUserExistingUser() {
        User oldUser = User.builder()
                .tgUid(1L)
                .build();
        userService.saveUser(oldUser);

        User user = User.builder()
                .tgUid(2L)
                .build();

        userService.saveUser(user);

        User actualUser = repository.findById(user.getTgUid()).get();

        assertEquals(user.getTgUid(), actualUser.getTgUid());
    }

    @Test
    public void testGetUserExists() {
        User user = User.builder()
                .tgUid(1L)
                .build();

        repository.save(user);

        User actualUser = userService.getUser(user.getTgUid());

        assertEquals(user.getTgUid(), actualUser.getTgUid());
    }

    @Test
    public void testGetUserNotFound() {
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(123L));
    }
}
