package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.TokenHolderRepository;
import com.resume.bot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    }

//    @Test
//    public void testGetUserTokenNotFound() {
//        User user = User.builder()
//                .tgUid(1L)
//                .build();
//
//        userRepository.save(user);
//        assertThrows(EntityNotFoundException.class, () -> tokenHolderService.getUserToken(user));
//    }

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

//    @Test
//    public void testSaveExistingTokenHolder() {
//        User user = User.builder()
//                .tgUid(1L)
//                .build();
//        TokenHolder tokenHolder1 = TokenHolder.builder()
//                .hhTokensId(1)
//                .user(user)
//                .build();
//        TokenHolder tokenHolder2 = TokenHolder.builder()
//                .hhTokensId(2)
//                .user(user)
//                .build();
//
//        tokenHolderRepository.save(tokenHolder1);
//        tokenHolderService.save(tokenHolder2);
//
//        TokenHolder retrievedTokenHolder = tokenHolderRepository.findById(tokenHolder2.getHhTokensId()).get();
//        assertEquals(tokenHolder2, retrievedTokenHolder);
//    }
}
