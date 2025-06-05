package com.rva.dungeon.entity;

import com.rva.dungeon.enumerated.EnemyType;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.random.RandomUtils;

public class Enemy extends Character {

    public final EnemyType enemyType;

    public Enemy(EnemyType enemyType, ContentService contentService) {
        super();
        this.enemyType = enemyType;
        setName(enemyType.getTypeName(contentService));
        // Intialise un enemis avec un ajustement random de + ou - 10% de chaque attribut
        setHealth(RandomUtils.adjustByPercentage(enemyType.getHealth(), 10));
        setAttackPower(RandomUtils.adjustByPercentage(enemyType.getStrength(), 10));
        setDefensePower(RandomUtils.adjustByPercentage(enemyType.getDefense(), 10));
        setGold(RandomUtils.adjustByPercentage(enemyType.getGoldPieces(), 10));
    }

    @Override
    public String toString() {
        return getName() + " (" + getIsAlive() + ")";
    }

}
