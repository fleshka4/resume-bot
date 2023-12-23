package com.resume.bot.service;

import com.resume.bot.model.entity.User;
import com.resume.bot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User saveUser(User user) {
        return repository.save(user);
    }

    public User getUser(int tgUid) {
        return repository.findById(tgUid).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }
}
