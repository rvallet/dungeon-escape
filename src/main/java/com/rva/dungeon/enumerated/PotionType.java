package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

public enum PotionType {

    HEALTH(ContentKey.POTION_TYPE_1, 10, 0, 0),
    STRENGTH(ContentKey.POTION_TYPE_2, 0, 5, 0),
    DEFENSE(ContentKey.POTION_TYPE_3, 0, 0, 5);

    private final ContentKey type;
    private final int health;
    private final int strength;
    private final int defense;

    PotionType(ContentKey type, int health, int strength, int defense) {
        this.type = type;
        this.health = health;
        this.strength = strength;
        this.defense = defense;
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

}
