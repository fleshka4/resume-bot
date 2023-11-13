package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Certificate {
    @NonNull String achievedAt;
    String owner;
    @NonNull String title;
    @NonNull String type;
    String url;
}
