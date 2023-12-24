package com.resume.bot.json.entity.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Photo {
    @NonNull
    private String medium;

    @NonNull
    private String small;

    @NonNull
    private String id;
}
