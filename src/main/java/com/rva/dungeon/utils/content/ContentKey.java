package com.rva.dungeon.utils.content;

public enum ContentKey {

    INIT_SELECT_LANGUAGE("init.select.language"),
    INIT_SELECT_LANGUAGE_PROMPT("init.select.language.prompt"),

    ACTION_CODE_1("action.code.1"),
    ACTION_CODE_1_DESCRIPTION("action.code.1.description"),
    ACTION_CODE_2("action.code.2"),
    ACTION_CODE_2_DESCRIPTION("action.code.2.description"),
    ACTION_CODE_3("action.code.3"),
    ACTION_CODE_3_DESCRIPTION("action.code.3.description"),
    ACTION_CODE_4("action.code.4"),
    ACTION_CODE_4_DESCRIPTION("action.code.4.description"),
    ACTION_CODE_5("action.code.5"),
    ACTION_CODE_5_DESCRIPTION("action.code.5.description"),

    DIRECTION_NORTH("direction.north"),
    DIRECTION_SOUTH("direction.south"),
    DIRECTION_EAST("direction.east"),
    DIRECTION_WEST("direction.west"),

    PLAYER_NAME("player.name"),
    PLAYER_HEALTH("player.health"),
    PLAYER_DEFENSE("player.defense"),
    PLAYER_ATTACK("player.attack"),

    ENEMY_TYPE_1("enemy.type.1"),
    ENEMY_TYPE_2("enemy.type.2"),
    ENEMY_TYPE_3("enemy.type.3"),
    ENEMY_TYPE_4("enemy.type.4"),

    ROOM_NAME("room.name"),
    ROOM_DESCRIPTION("room.description"),

    COMMON_QUERY_PLAYER_NAME("common.query.player.name"),
    COMMON_QUERY_DIRECTION("common.query.direction"),
    COMMON_DISPLAY_DIRECTION("common.display.direction"),
    COMMON_GREETING("common.greeting"),
    COMMON_GOODBYE("common.goodbye"),
    COMMON_PROMPT("common.prompt"),

    COMMON_ROOM_MOVE_INTO("common.room.move.into"),
    COMMON_ROOM_MOVE_OUT("common.room.move.out"),
    COMMON_ROOM_ERROR("common.room.move.error"),

    COMMON_COMMAND_ACTIONS("common.command.actions"),
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
