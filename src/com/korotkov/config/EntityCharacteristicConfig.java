package com.korotkov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;
import com.korotkov.services.impl.EntityMapConfigDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.korotkov.config.Constants.OBJECT_MAPPER_READ_ERROR;

public class EntityCharacteristicConfig {

    private Map<EntityType, Entity> entityMapConfig;

    public Map<EntityType, Entity> getEntityMapConfig() {
        return entityMapConfig;
    }

    public EntityCharacteristicConfig(ObjectMapper objectMapper, String pathToJson) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Map.class, new EntityMapConfigDeserializer());
        objectMapper.registerModule(simpleModule);
        try {
            entityMapConfig = objectMapper.readValue(new File(pathToJson), Map.class);
        } catch (IOException e) {
            System.out.printf(OBJECT_MAPPER_READ_ERROR, pathToJson, pathToJson);
            System.exit(0);
        }
    }
}
