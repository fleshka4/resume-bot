package com.resume.bot.json.entity.common;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {
    @Test
    public void gettersAndSettersTest() {
        String accessToken = "abcd1234";
        String tokenType = "Bearer";
        Integer expiresIn = 3600;
        String refreshToken = "efgh5678";

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setTokenType(tokenType);
        token.setExpiresIn(expiresIn);
        token.setRefreshToken(refreshToken);

        assertEquals(token.getAccessToken(), accessToken);
        assertEquals(token.getTokenType(), tokenType);
        assertEquals(token.getExpiresIn(), expiresIn);
        assertEquals(token.getRefreshToken(), refreshToken);
    }

    @Test
    public void constructorWithArgsTest() {
        String accessToken = "abcd1234";
        String tokenType = "Bearer";
        Integer expiresIn = 3600;
        String refreshToken = "efgh5678";

        Token token = new Token(accessToken, tokenType, expiresIn, refreshToken);

        assertEquals(token.getAccessToken(), accessToken);
        assertEquals(token.getTokenType(), tokenType);
        assertEquals(token.getExpiresIn(), expiresIn);
        assertEquals(token.getRefreshToken(), refreshToken);
    }

    @Test
    public void noArgsConstructorTest() {
        Token token = new Token();

        assertNull(token.getAccessToken());
        assertNull(token.getTokenType());
        assertNull(token.getExpiresIn());
        assertNull(token.getRefreshToken());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Token token1 = new Token("abcd1234", "Bearer", 3600, "efgh5678");
        Token token2 = new Token("abcd1234", "Bearer", 3600, "efgh5678");
        Token token3 = new Token("ijkl5678", "Bearer", 3600, "mnop9012");

        assertEquals(token1, token2);
        assertNotEquals(token1, token3);

        assertEquals(token1.hashCode(), token2.hashCode());
        assertNotEquals(token1.hashCode(), token3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Token token = new Token("abcd1234", "Bearer", 3600, "efgh5678");

        String expectedToString = "Token(accessToken=abcd1234, tokenType=Bearer, expiresIn=3600, refreshToken=efgh5678)";
        assertEquals(expectedToString, token.toString());
    }

    @Test
    public void createTokenFromJsonTest() {
        String json = """
                {
                  "access_token": "abcd1234",
                  "token_type": "Bearer",
                  "expires_in": 3600,
                  "refresh_token": "efgh5678"
                }
                """;

        Token token = JsonProcessor.createEntityFromJson(json, Token.class);

        assertEquals(token.getAccessToken(), "abcd1234");
        assertEquals(token.getTokenType(), "Bearer");
        assertEquals(token.getExpiresIn(), 3600);
        assertEquals(token.getRefreshToken(), "efgh5678");
    }

    @Test
    public void createTokenWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Token.class));
    }
}
