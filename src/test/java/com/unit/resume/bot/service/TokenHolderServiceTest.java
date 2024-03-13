package com.unit.resume.bot.service;

import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.TokenHolderRepository;
import com.resume.bot.service.TokenHolderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenHolderServiceTest {

    @Mock
    private TokenHolderRepository repository;

    @InjectMocks
    private TokenHolderService tokenHolderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserTokenExists() {
        User user = new User();
        TokenHolder tokenHolder = new TokenHolder();

        when(repository.findByUser(user)).thenReturn(Optional.of(tokenHolder));
        TokenHolder retrievedTokenHolder = tokenHolderService.getUserToken(user);

        assertNotNull(retrievedTokenHolder);
        assertEquals(tokenHolder, retrievedTokenHolder);
    }

    @Test
    public void testGetUserTokenNotFound() {
        User user = new User();

        when(repository.findByUser(user)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tokenHolderService.getUserToken(user));
    }

    @Test
    public void testCheckTokenHolderExists() {
        User user = new User();

        when(repository.existsTokenHolderByUser(user)).thenReturn(true);
        boolean exists = tokenHolderService.checkTokenHolderExists(user);

        assertTrue(exists);
    }

    @Test
    public void testCheckTokenHolderNotExists() {
        User user = new User();

        when(repository.existsTokenHolderByUser(user)).thenReturn(false);
        boolean exists = tokenHolderService.checkTokenHolderExists(user);

        assertFalse(exists);
    }

    @Test
    public void testSaveNewTokenHolder() {
        TokenHolder newTokenHolder = new TokenHolder();

        when(repository.existsTokenHolderByUser(newTokenHolder.getUser())).thenReturn(false);

        tokenHolderService.save(newTokenHolder);
        verify(repository, times(1)).save(newTokenHolder);
    }

    @Test
    public void testSaveExistingTokenHolder() {
        TokenHolder existingTokenHolder = new TokenHolder();

        when(repository.existsTokenHolderByUser(existingTokenHolder.getUser())).thenReturn(true);

        tokenHolderService.save(existingTokenHolder);

        verify(repository, times(1)).updateAccessTokenAndExpiresInAndRefreshTokenByUser(
                existingTokenHolder.getAccessToken(),
                existingTokenHolder.getExpiresIn(),
                existingTokenHolder.getRefreshToken(),
                existingTokenHolder.getUser()
        );
        verify(repository, never()).save(existingTokenHolder);
    }
}
