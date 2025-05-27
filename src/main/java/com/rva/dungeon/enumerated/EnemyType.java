package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

public enum EnemyType {

    ZOMBIE(1, ContentKey.ENEMY_TYPE_1, 50, 6, 0,5),
    SKELETON(2, ContentKey.ENEMY_TYPE_2, 60, 12, 2, 20),
    VAMPIRE(3, ContentKey.ENEMY_TYPE_3, 80, 25, 5, 50),
    LICH(4, ContentKey.ENEMY_TYPE_4, 120, 35, 10,100);

    private final int number;
    private final ContentKey type;
    private final int health;
    private final int strength;
    private final int defense;
    private final int goldPieces;

    EnemyType(int number, ContentKey type, int health, int strength, int defense, int goldPieces) {
        this.number = number;
        this.type = type;
        this.health = health;
        this.strength = strength;
        this.defense = defense;
        this.goldPieces = goldPieces;
    }

    public int getNumber() {
        return number;
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

    public int getGoldPieces() {
        return goldPieces;
    }

    public static EnemyType fromInput(String input, ContentService contentService) {
        try {
            int number = Integer.parseInt(input);
            for (EnemyType type : EnemyType.values()) {
                if (type.getNumber() == number) {
                    return type;
                }
            }
        } catch (NumberFormatException e) {
            // If not a number, check by name
            for (EnemyType type : EnemyType.values()) {
                String content = contentService.getString(type.getType());
                if (content.equalsIgnoreCase(input.trim())) {
                    return type;
                }
            }
        }
        return null;
    }

}
