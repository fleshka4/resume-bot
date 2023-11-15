package com.resume.bot.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.bot.exception.json.JsonActionException;
import com.resume.bot.json.entity.Client;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonProcessor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String EMAIL_REPLACEMENT = "\"emailForContactValue\":";
    private static final String PHONE_REPLACEMENT = "\"phoneForContactValue\":\\{";
    private static final String VALUE_TO_REPLACE_EMAIL = "\"value\":";
    private static final String VALUE_TO_REPLACE_PHONE = "\"value\":\\{";

    public static <T> T createEntityFromJson(String jsonString, Class<T> clazz) {
        try {
            if (clazz.equals(Client.class)) {
                jsonString = jsonString
                        .replaceAll(VALUE_TO_REPLACE_PHONE, PHONE_REPLACEMENT)
                        .replaceAll(VALUE_TO_REPLACE_EMAIL, EMAIL_REPLACEMENT);
            }
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String createJsonFromEntity(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj)
                    .replaceAll(EMAIL_REPLACEMENT, VALUE_TO_REPLACE_EMAIL)
                    .replaceAll(PHONE_REPLACEMENT, VALUE_TO_REPLACE_PHONE);
        } catch (JsonProcessingException e) {
            throw new JsonActionException("Failed creating JSON process!");
        }
    }
}
