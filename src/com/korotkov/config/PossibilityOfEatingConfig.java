package com.korotkov.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.korotkov.config.Constants.OBJECT_MAPPER_READ_ERROR;

public class PossibilityOfEatingConfig {
    private final Map<Map<Entity, Entity>, Long> possibilityOfEating = new HashMap<>();

    public Map<Map<Entity, Entity>, Long> getPossibilityOfEating() {
        return possibilityOfEating;
    }

    public PossibilityOfEatingConfig(ObjectMapper objectMapper, String pathToJson, Map<EntityType, Entity> entityMapConfig) {
        List<MyHelpToDeserializeObject> myHelpToDeserializeObjects;
        try {
            myHelpToDeserializeObjects = objectMapper.readValue(new File(pathToJson), new TypeReference<>() {
            });
            myHelpToDeserializeObjects.forEach(o -> {
                Entity from = entityMapConfig.get(Enum.valueOf(EntityType.class, o.getFrom().toUpperCase()));
                Entity to = entityMapConfig.get(Enum.valueOf(EntityType.class, o.getTo().toUpperCase()));
                possibilityOfEating.put(Map.of(from, to), o.getPercent());
            });
        } catch (IOException e) {
            System.out.printf(OBJECT_MAPPER_READ_ERROR, pathToJson, pathToJson);
            System.exit(0);
        }
    }

    private static class MyHelpToDeserializeObject {
        private String from;
        private String to;
        private Long percent;

        public MyHelpToDeserializeObject() {
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public Long getPercent() {
            return percent;
        }
    }
}

