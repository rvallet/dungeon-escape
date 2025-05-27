package com.rva.dungeon.entity;

import com.rva.dungeon.enumerated.EnemyType;
import com.rva.dungeon.service.ContentService;

public class Enemy extends Character {

    public final EnemyType enemyType;

    public Enemy(EnemyType enemyType, ContentService contentService) {
        super();
        this.enemyType = enemyType;
        setName(enemyType.getTypeName(contentService));
        setHealth(enemyType.getHealth());
        setAttackPower(enemyType.getStrength());
        setGold(enemyType.getGoldPieces());

    }

    @Override
    public String toString() {
        return getName() + " (" + getIsAlive() + ")";
    }

}
