package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Certificate {
    @NonNull
    @JsonProperty("achieved_at")
    private String achievedAt;

    private String owner;

    @NonNull
    private String title;

    @NonNull
    private String type;

    private String url;

    @JsonCreator
    public static Certificate createCertificate(
            @JsonProperty("achieved_at") @NonNull String achievedAt,
            @JsonProperty("owner") String owner,
            @JsonProperty("title") @NonNull String title,
            @JsonProperty("type") @NonNull String type,
            @JsonProperty("url") String url) {
        return new Certificate(achievedAt, owner, title, type, url);
    }
}
