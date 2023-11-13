package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact {
    String comment;
    Boolean needVerification;
    boolean preferred;
    @NonNull Type type;
    ValuePhoneOrEmail value;
    Boolean verified;
}
