package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employer {
    @NonNull String alternateUrl;
    @NonNull String id;
    LogoUrls logoUrls;
    @NonNull String name;
    @NonNull String url;
}
