package com.korotkov.models.island;

import com.korotkov.models.abstracts.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Island {
    private Map<Field, List<Entity>> island;

    public Map<Field, List<Entity>> getIsland() {
        return island;
    }

    public Island(Map<Field, List<Entity>> island) {
        this.island = island;
    }
}
