package com.resume.bot.service;

import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.TokenHolderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenHolderService {
    private final TokenHolderRepository repository;

    public TokenHolder getUserToken(User user) {
        return repository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Token not found")
        );
    }

    public boolean checkTokenHolderExists(User user) {
        return repository.existsTokenHolderByUser(user);
    }

    public void save(TokenHolder tokenHolder) {
        if (repository.existsTokenHolderByUser(tokenHolder.getUser())) {
            repository.updateAccessTokenAndExpiresInAndRefreshTokenByUser(tokenHolder.getAccessToken(), tokenHolder.getExpiresIn(),
                    tokenHolder.getRefreshToken(), tokenHolder.getUser());
        } else {
            repository.save(tokenHolder);
        }
    }
}
