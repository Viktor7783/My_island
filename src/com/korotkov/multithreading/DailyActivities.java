package com.korotkov.multithreading;

public class DailyActivities {
    private boolean isIslandInitialized;
    private boolean isPressPause;
    private boolean isGrassPlanted;
    private boolean isAnimalActionsCompleted;
    private boolean isRemoveAndRestoreAnimals;
    private boolean isCollectStatistics;
    private boolean isShownDailyStatistics = true;

    public boolean isPressPause() {
        return isPressPause;
    }

    public void setPressPause(boolean pressPause) {
        isPressPause = pressPause;
    }

    public boolean isIslandInitialized() {
        return isIslandInitialized;
    }

    public void setIslandInitialized(boolean islandInitialized) {
        isIslandInitialized = islandInitialized;
    }

    public boolean isTimeToCollectStatistics() {
        return isGrassPlanted && isRemoveAndRestoreAnimals && isAnimalActionsCompleted && !isCollectStatistics;
    }

    public void setCollectStatistics(boolean collectStatistics) {
        isCollectStatistics = collectStatistics;
    }

    public boolean isTimeToShowStatistics() {
        return isGrassPlanted && isRemoveAndRestoreAnimals && isAnimalActionsCompleted && isCollectStatistics;
    }

    public boolean isTimeToAnimalActions() {
        return isGrassPlanted && isRemoveAndRestoreAnimals && !isAnimalActionsCompleted;
    }

    public boolean isGrassPlanted() {
        return isGrassPlanted;
    }

    public boolean isAnimalActionsCompleted() {
        return isAnimalActionsCompleted;
    }

    public boolean isRemoveAndRestoreAnimals() {
        return isRemoveAndRestoreAnimals;
    }

    public boolean isShownDailyStatistics() {
        return isShownDailyStatistics;
    }

    public void setGrassPlanted(boolean grassPlanted) {
        isGrassPlanted = grassPlanted;
    }

    public void setAnimalActionsCompleted(boolean animalActionsCompleted) {
        isAnimalActionsCompleted = animalActionsCompleted;
    }

    public void setRemoveAndRestoreAnimals(boolean removeAndRestoreAnimals) {
        isRemoveAndRestoreAnimals = removeAndRestoreAnimals;
    }

    public void setShownDailyStatistics(boolean shownDailyStatistics) {
        isShownDailyStatistics = shownDailyStatistics;
    }
}

