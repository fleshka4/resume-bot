package com.resume.bot.repository;

import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TokenHolderRepository extends JpaRepository<TokenHolder, Integer> {

    Optional<TokenHolder> findByUser(User user);

    boolean existsTokenHolderByUser(User user);

    @Transactional
    @Modifying
    @Query("update TokenHolder t set t.accessToken = ?1, t.expiresIn = ?2, t.refreshToken = ?3 where t.user = ?4")
    void updateAccessTokenAndExpiresInAndRefreshTokenByUser(String accessToken, int expiresIn, String refreshToken, User user);
}
