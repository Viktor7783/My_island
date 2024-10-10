package com.korotkov.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.korotkov.config.Constants.OBJECT_READ_ERROR;

public class ImagesOfEntitiesConfig {
    private Map<String, String> imagesMapConfig;

    public Map<String, String> getImagesMapConfig() {
        return imagesMapConfig;
    }

    public ImagesOfEntitiesConfig(ObjectMapper objectMapper, String pathToJson) {
        try {
            imagesMapConfig = objectMapper.readValue(new File(pathToJson), new TypeReference<HashMap<String, String>>() {
            });
        } catch (IOException e) {
            System.out.printf(OBJECT_READ_ERROR, pathToJson, pathToJson);
            System.exit(0);
        }
    }
}
