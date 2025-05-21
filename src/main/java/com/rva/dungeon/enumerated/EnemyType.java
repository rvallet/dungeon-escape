package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

public enum EnemyType {

    ZOMBIE(ContentKey.ENEMY_TYPE_1, 50, 6, 5),
    SKELETON(ContentKey.ENEMY_TYPE_2, 60, 12, 20),
    VAMPIRE(ContentKey.ENEMY_TYPE_3, 80, 25, 50),
    LICH(ContentKey.ENEMY_TYPE_4, 120, 35, 100);

    private final ContentKey type;
    private final int health;
    private final int strength;
    private final int goldPieces;

    EnemyType(ContentKey type, int health, int strength, int goldPieces) {
        this.type = type;
        this.health = health;
        this.strength = strength;
        this.goldPieces = goldPieces;
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

    public int getGoldPieces() {
        return goldPieces;
    }

}
