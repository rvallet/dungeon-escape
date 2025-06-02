package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

import java.util.ArrayList;
import java.util.List;

public enum PotionType {

    HEALTH(ContentKey.POTION_TYPE_1, 25, 0, 0, 0),
    STRENGTH(ContentKey.POTION_TYPE_2, 0, 5, 0, 0),
    DEFENSE(ContentKey.POTION_TYPE_3, 0, 0, 5, 0),
    LIFE(ContentKey.POTION_TYPE_4, 0, 0, 0, 10);

    private final ContentKey type;
    private final int health;
    private final int strength;
    private final int defense;
    private final int life;

    PotionType(ContentKey type, int health, int strength, int defense, int life) {
        this.type = type;
        this.health = health;
        this.strength = strength;
        this.defense = defense;
        this.life = life;
    }

    public ContentKey getType() {
        return type;
    }

    public String getTypeName(ContentService contentService) {
        return contentService.getString(this.type);
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getDefense() {
        return defense;
    }

    public int getLife() {
        return life;
    }

    public static PotionType getRandomPotionType() {
        PotionType[] values = PotionType.values();
        // Pondération : 2 fois moins de chance d'obtenir LIFE
        List<PotionType> weightedList = new ArrayList<>();
        for (PotionType potionType : values) {
            if (potionType != LIFE) {
                weightedList.add(potionType);
                weightedList.add(potionType);
            } else {
                weightedList.add(potionType);
            }
        }

        return weightedList.get((int) (Math.random() * weightedList.size()));
    }

}
