package com.resume.bot.repository;

import com.resume.bot.model.entity.TokenHolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenHolderRepository extends JpaRepository<TokenHolder, Integer> {
}
