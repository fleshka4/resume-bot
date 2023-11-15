package com.resume.bot.json.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact {
    private String comment;

    @JsonProperty("need_verification")
    private Boolean needVerification;

    private boolean preferred;

    @NonNull
    private Type type;

    @JsonUnwrapped
    private ValuePhoneOrEmail value;

    private Boolean verified;
}
