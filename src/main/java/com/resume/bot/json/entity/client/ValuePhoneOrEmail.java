package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ValuePhoneOrEmail {
//    @JsonProperty("emailForContactValue")
    private String email;

//    @JsonProperty("phoneForContactValue")
    private Phone phone;
}
