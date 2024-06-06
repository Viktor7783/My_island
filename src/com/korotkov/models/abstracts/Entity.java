package com.korotkov.models.abstracts;


import java.util.Objects;

public abstract class Entity {
    protected Double weight;
    protected Integer maxCountOnField;
    protected Integer speed;
    protected Double kgToGetFull;

    protected Entity(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        this.weight = weight;
        this.maxCountOnField = maxCountOnField;
        this.speed = speed;
        this.kgToGetFull = kgToGetFull;
    }

    protected Entity(Entity entity) {
        this.weight = entity.getWeight();
        this.maxCountOnField = entity.getMaxCountOnField();
        this.speed = entity.getSpeed();
        this.kgToGetFull = entity.getKgToGetFull();
    }

    private Entity() {
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getMaxCountOnField() {
        return maxCountOnField;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Double getKgToGetFull() {
        return kgToGetFull;
    }


    public void setWeight(Double weight) {
        if (weight > 0) this.weight = weight;
    }

    public void setMaxCountOnField(Integer maxCountOnField) {
        if (maxCountOnField > 0) this.maxCountOnField = maxCountOnField;
    }

    public void setSpeed(Integer speed) {
        if (speed >= 0) this.speed = speed;
    }

    public void setKgToGetFull(Double kgToGetFull) {
        if (kgToGetFull > 0) this.kgToGetFull = kgToGetFull;
    }

   /* @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj.getClass() != this.getClass()) return false;
        Entity entity = (Entity) obj;
        return Objects.equals(weight, entity.getWeight()) && Objects.equals(maxCountOnField, entity.getMaxCountOnField()) && Objects.equals(speed, entity.getSpeed()) && Objects.equals(kgToGetFull, entity.getKgToGetFull());
    }*/

    /*@Override
    public int hashCode() {
        return Objects.hash(weight, maxCountOnField, speed, kgToGetFull);
    }*/
}

