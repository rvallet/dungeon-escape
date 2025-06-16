package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

import java.util.ArrayList;
import java.util.List;

public enum PotionType {

    HEALTH(ContentKey.POTION_TYPE_1, 25),
    STRENGTH(ContentKey.POTION_TYPE_2, 5),
    DEFENSE(ContentKey.POTION_TYPE_3, 3),
    LIFE(ContentKey.POTION_TYPE_4, 10);

    private final ContentKey type;
    private final int amount;

    PotionType(ContentKey type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ContentKey getType() {
        return type;
    }

    public String getTypeName(ContentService contentService) {
        return contentService.getString(this.type);
    }

    public int getAmount() {
        return amount;
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
