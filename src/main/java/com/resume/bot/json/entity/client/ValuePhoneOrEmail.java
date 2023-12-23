package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ValuePhoneOrEmail {
    @JsonProperty("emailForContactValue")
    private String email;

    @JsonProperty("phoneForContactValue")
    private Phone phone;
}
