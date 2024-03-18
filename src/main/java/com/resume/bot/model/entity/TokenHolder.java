package com.resume.bot.model.entity;

import com.resume.bot.json.entity.common.Token;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hh_tokens")
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class TokenHolder {

    @Id
    @Column(name = "hh_tokens_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hhTokensId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expires_in")
    private int expiresIn;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "tg_uid")
    private User user;

    public TokenHolder(Token token, User user) {
        accessToken = token.getAccessToken();
        expiresIn = token.getExpiresIn();
        refreshToken = token.getRefreshToken();
        this.user = user;
    }
}
