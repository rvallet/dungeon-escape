package com.rva.dungeon.model;

import com.rva.dungeon.enumerated.PotionType;
import com.rva.dungeon.service.ContentService;

public class Potion extends Item {

    private final PotionType potionType;
    private int health;
    private int strength;
    private int defense;

    public Potion(PotionType potionType, ContentService contentService) {
        super(potionType.getTypeName(contentService));
        this.potionType = potionType;
        this.health = potionType.getHealth();
        this.strength = potionType.getStrength();
        this.defense = potionType.getDefense();
    }

    public PotionType getPotionType() {
        return potionType;
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

}
