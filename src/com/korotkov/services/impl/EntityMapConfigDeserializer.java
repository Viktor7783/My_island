package com.korotkov.services.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EntityMapConfigDeserializer extends JsonDeserializer<Map<EntityType, Entity>> {
    @Override
    public Map<EntityType, Entity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        Map<EntityType, Entity> map = new HashMap<>();
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(jsonParser, "Expected START_OBJECT token");
        }

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String keyString = jsonParser.getValueAsString().toUpperCase();
            EntityType entityType = Enum.valueOf(EntityType.class, keyString);
            jsonParser.nextToken();
            @SuppressWarnings({"unchecked", "cast"})
            Entity entity = (Entity) deserializationContext.readValue(jsonParser, entityType.getClazz());
            map.put(entityType, entity);
        }
        return map;
    }
}