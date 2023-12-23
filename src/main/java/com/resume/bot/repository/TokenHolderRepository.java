package com.resume.bot.repository;

import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenHolderRepository extends JpaRepository<TokenHolder, Integer> {

    Optional<TokenHolder> findByUser(User user);
}
