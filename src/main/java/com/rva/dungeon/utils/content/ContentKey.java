package com.rva.dungeon.utils.content;

public enum ContentKey {

    COMMON_GREETING("common.greeting"),
    COMMON_GOODBYE("common.goodbye"),
    COMMON_PROMPT("common.prompt"),
    COMMON_COMMAND_UNKNOWN("common.command.unknown"),
    COMMON_COMMAND_QUIT("common.command.quit"),
    COMMON_COMMAND_EXPLORE("common.command.explore"),
    COMMON_COMMAND_ATTACK("common.command.attack"),
    COMMON_COMMAND_DEFEND("common.command.defend"),
    COMMON_COMMAND_USE_ITEM("common.command.use_item"),
    COMMON_COMMAND_INVENTORY("common.command.inventory"),
    COMMON_COMMAND_HELP("common.command.help"),

    DUNGEON_EXPLORE("dungeon.explore"),
    DUNGEON_ENTER("dungeon.enter"),
    DUNGEON_EXIT("dungeon.exit"),
    DUNGEON_ATTACK("dungeon.attack"),
    DUNGEON_DEFEND("dungeon.defend"),
    DUNGEON_USE_ITEM("dungeon.use_item"),
    DUNGEON_INVENTORY("dungeon.inventory"),
    DUNGEON_HELP("dungeon.help"),
    DUNGEON_INTRO("dungeon.intro"),;

    private final String key;

    ContentKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

}
