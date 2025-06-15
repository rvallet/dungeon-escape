package com.rva.dungeon.model;

import com.rva.dungeon.enumerated.PotionType;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;

public class Potion extends Item {

    private final PotionType potionType;
    private int amount;

    private static final int HEALTH_ADJUST_PERCENTAGE = 25;
    private static final int STRENGTH_ADJUST_PERCENTAGE = 40;
    private static final int DEFENSE_ADJUST_PERCENTAGE = 40;
    private static final int LIFE_ADJUST_PERCENTAGE = 25;


    public Potion(PotionType potionType, ContentService contentService) {
        super(potionType.getTypeName(contentService));
        this.potionType = potionType;
        // Adjust the potion attributes by a percentage to add variability
        switch (potionType) {
            case HEALTH :
                this.amount = RandomUtils.adjustByPercentage(potionType.getAmount(), HEALTH_ADJUST_PERCENTAGE);
                setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_HEALTH_POTION_DESCRIPTION, getAmount()));
                break;
            case STRENGTH :
                this.amount = RandomUtils.adjustByPercentage(potionType.getAmount(), STRENGTH_ADJUST_PERCENTAGE);
                setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_STRENGTH_POTION_DESCRIPTION, getAmount()));
                break;
            case DEFENSE :
                this.amount = RandomUtils.adjustByPercentage(potionType.getAmount(), DEFENSE_ADJUST_PERCENTAGE);
                setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_DEFENSE_POTION_DESCRIPTION, getAmount()));
                break;
            case LIFE :
                this.amount = RandomUtils.adjustByPercentage(potionType.getAmount(), LIFE_ADJUST_PERCENTAGE);
                setDescription(contentService.getFormattedString(ContentKey.COMMON_ITEM_LIFE_POTION_DESCRIPTION, getAmount()));
                break;
        }
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return getName() + " (" + amount + ")";
    }

}
