package com.resume.bot.json.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ValuePhoneOrEmail {
    String email;
    Phone phone;
}
