package com.rva.dungeon.enumerated;

import com.rva.dungeon.utils.content.ContentKey;

public enum EncounterCharacterType {

    BEGGAR(ContentKey.ENCOUNTER_CHARACTER_TYPE_BEGGAR, ContentKey.ENCOUNTER_CHARACTER_DESCRIPTION_BEGGAR),
    MERCHANT(ContentKey.ENCOUNTER_CHARACTER_TYPE_MERCHANT, ContentKey.ENCOUNTER_CHARACTER_DESCRIPTION_MERCHANT),
    HEALER(ContentKey.ENCOUNTER_CHARACTER_TYPE_HEALER, ContentKey.ENCOUNTER_CHARACTER_DESCRIPTION_HEALER),
    WIZARD(ContentKey.ENCOUNTER_CHARACTER_TYPE_WIZARD, ContentKey.ENCOUNTER_CHARACTER_DESCRIPTION_WIZARD);

    private final ContentKey type;
    private final ContentKey description;

    EncounterCharacterType (ContentKey type, ContentKey description) {
        this.type = type;
        this.description = description;
    }

    public ContentKey getType() {
        return type;
    }

    public ContentKey getDescription() {
        return description;
    }

    public static EncounterCharacterType getRandomEncounterCharacterType() {
        EncounterCharacterType[] values = EncounterCharacterType.values();
        return values[(int) (Math.random() * values.length)];
    }

}
