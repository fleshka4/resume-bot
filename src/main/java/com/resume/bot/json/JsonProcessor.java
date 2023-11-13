package com.resume.bot.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.bot.exception.json.JsonActionException;

public class JsonProcessor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String createJsonFromEntity(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonActionException("Failed creating JSON process!");
        }
    }
}
