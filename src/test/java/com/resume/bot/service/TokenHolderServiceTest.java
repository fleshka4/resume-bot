package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.TokenHolderRepository;
import com.resume.bot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class TokenHolderServiceTest extends IntegrationBaseTest {

    @Autowired
    private TokenHolderRepository tokenHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenHolderService tokenHolderService;

    @Test
    public void testGetUserTokenExists() {
        User user = User.builder()
                .tgUid(1L)
                .build();

        TokenHolder tokenHolder = TokenHolder.builder()
                .hhTokensId(1)
                .user(user)
                .build();

        user.setTokenHolder(tokenHolder);

        userRepository.save(user);
        tokenHolderRepository.save(tokenHolder);

        TokenHolder retrievedTokenHolder = tokenHolderService.getUserToken(user);

        assertNotNull(retrievedTokenHolder);
        assertEquals(tokenHolder.getHhTokensId(), retrievedTokenHolder.getHhTokensId());

        tokenHolderRepository.delete(tokenHolder);
        userRepository.delete(user);
    }

    @Test
    public void testCheckTokenHolderExists() {
        User user = User.builder()
                .tgUid(1L)
                .build();

        TokenHolder tokenHolder = TokenHolder.builder()
                .hhTokensId(1)
                .user(user)
                .build();

        user.setTokenHolder(tokenHolder);

        userRepository.save(user);
        tokenHolderRepository.save(tokenHolder);
        boolean exists = tokenHolderService.checkTokenHolderExists(user);

        assertTrue(exists);

        tokenHolderRepository.delete(tokenHolder);
        userRepository.delete(user);
    }

    @Test
    public void testCheckTokenHolderNotExists() {
        User user = User.builder()
                .tgUid(1L)
                .build();

        userRepository.save(user);
        boolean exists = tokenHolderService.checkTokenHolderExists(user);

        assertFalse(exists);
    }

    @Test
    public void testSaveNewTokenHolder() {
        TokenHolder tokenHolder = TokenHolder.builder()
                .hhTokensId(1)
                .build();

        tokenHolderService.save(tokenHolder);
        TokenHolder retrievedTokenHolder = tokenHolderRepository.findById(tokenHolder.getHhTokensId()).get();
        assertEquals(tokenHolder, retrievedTokenHolder);
    }
}
