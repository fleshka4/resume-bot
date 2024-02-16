package com.resume.bot.service;

import com.resume.bot.model.entity.User;
import com.resume.bot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User saveUser(User user) {
        Optional<User> u = repository.findById(user.getTgUid());
        return u.orElseGet(() -> repository.save(user));
    }

    public User getUser(Long tgUid) {
        return repository.findById(tgUid).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }
}
