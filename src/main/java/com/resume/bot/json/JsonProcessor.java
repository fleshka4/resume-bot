package com.resume.bot.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.bot.exception.json.JsonActionException;
import com.resume.bot.json.entity.client.Resume;

public class JsonProcessor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T createEntityFromJson(String jsonString, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String createJsonFromEntity(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonActionException("Failed creating JSON process!");
        }
    }
}
