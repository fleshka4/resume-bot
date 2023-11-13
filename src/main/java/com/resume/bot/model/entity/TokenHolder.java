package com.resume.bot.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hh_tokens")
@NoArgsConstructor
@Getter
@Setter
public class TokenHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hhTokensId;

    private String accessToken;

    private int expiresIn;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "tgUid")
    private User user;
}
