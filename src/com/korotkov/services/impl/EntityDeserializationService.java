package com.korotkov.services.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;

import java.io.IOException;

public class EntityDeserializationService extends StdDeserializer<Entity> {
    public EntityDeserializationService(Class<?> vc) {
        super(vc);
    }

    @Override
    public Entity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Double weight = node.get("weight").doubleValue();
        Integer maxCountOnField = node.get("maxCountOnField").intValue();
        Integer speed = node.get("speed").intValue();
        Double kgToGetFull = node.get("kgToGetFull").doubleValue();

        return null;// new Animal(weight,maxCountOnField,speed,kgToGetFull);
    }
}
