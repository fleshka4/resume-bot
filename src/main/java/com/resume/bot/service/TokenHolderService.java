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
}
