package com.rva.dungeon.model;

import com.rva.dungeon.enumerated.PotionType;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;

public class Potion extends Item {

    private final PotionType potionType;
    private int life;
    private int health;
    private int strength;
    private int defense;

    public Potion(PotionType potionType, ContentService contentService) {
        super(potionType.getTypeName(contentService));
        this.potionType = potionType;
        // Adjust the potion attributes by a percentage to add variability
        this.life = RandomUtils.adjustByPercentage(potionType.getLife(), 25);
        this.health = RandomUtils.adjustByPercentage(potionType.getHealth(), 25);
        this.strength = RandomUtils.adjustByPercentage(potionType.getStrength(), 40);
        this.defense = RandomUtils.adjustByPercentage(potionType.getDefense(), 40);
        switch (potionType) {
            case HEALTH -> setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_HEALTH_POTION_DESCRIPTION, getHealth()));
            case STRENGTH -> setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_STRENGTH_POTION_DESCRIPTION, getStrength()));
            case DEFENSE -> setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_DEFENSE_POTION_DESCRIPTION, getDefense()));
            case LIFE -> setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_LIFE_POTION_DESCRIPTION, getLife()));
        }
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    @Override
    public String toString() {
        return switch (potionType) {
            case HEALTH -> getName() + " (" + health + ")";
            case STRENGTH -> getName() + " (" + strength + ")";
            case DEFENSE -> getName() + " (" + defense + ")";
            case LIFE -> getName() + " (" + life + ")";
        };
    }

}
