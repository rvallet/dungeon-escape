package com.rva.dungeon.utils.content;

import java.util.ArrayList;
import java.util.List;

public enum ContentKey {

    INIT_SELECT_LANGUAGE("init.select.language"),
    INIT_SELECT_DIFFICULTY("init.select.difficulty"),
    INIT_SELECT_PROMPT("init.select.prompt"),

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
    ACTION_CODE_6("action.code.6"),
    ACTION_CODE_6_DESCRIPTION("action.code.6.description"),
    ACTION_CODE_7("action.code.7"),
    ACTION_CODE_7_DESCRIPTION("action.code.7.description"),
    ACTION_CODE_666("action.code.666"),
    ACTION_CODE_666_DESCRIPTION("action.code.666.description"),

    DIRECTION_NORTH("direction.north"),
    DIRECTION_SOUTH("direction.south"),
    DIRECTION_EAST("direction.east"),
    DIRECTION_WEST("direction.west"),

    PLAYER_NAME("player.name"),
    PLAYER_HEALTH("player.health"),
    PLAYER_DEFENSE("player.defense"),
    PLAYER_ATTACK("player.attack"),
    PLAYER_GOLD("player.gold"),
    PLAYER_INVENTORY("player.inventory"),
    PLAYER_INVENTORY_EMPTY("player.inventory.empty"),
    PLAYER_INVENTORY_PROMPT("player.inventory.prompt"),
    PLAYER_INVENTORY_UNKNOWN("player.inventory.unknown"),
    PLAYER_INVENTORY_USE_ITEM("player.inventory.use.item"),

    ENEMY_TYPE_1("enemy.type.1"),
    ENEMY_TYPE_2("enemy.type.2"),
    ENEMY_TYPE_3("enemy.type.3"),
    ENEMY_TYPE_4("enemy.type.4"),

    POTION_TYPE_1("potion.type.1"),
    POTION_TYPE_2("potion.type.2"),
    POTION_TYPE_3("potion.type.3"),
    POTION_TYPE_4("potion.type.4"),

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
    COMMON_ROOM_VISITED("common.room.visited"),
    COMMON_ROOM_ERROR("common.room.move.error"),
    COMMON_ROOM_ENEMIES_ALIVE("common.room.enemies.alive"),
    COMMON_ROOM_ENEMIES_DEAD("common.room.enemies.dead"),
    COMMON_ROOM_ENEMIES_ALIVE_LABEL("common.room.enemies.alive.label"),
    COMMON_ROOM_ENEMIES_DEAD_LABEL("common.room.enemies.dead.label"),
    COMMON_ROOM_ITEMS("common.room.items"),
    COMMON_ROOM_ITEMS_ERROR("common.room.items.error"),

    COMMON_ROOM_DESCRIPTION_1("common.room.description.1"),
    COMMON_ROOM_DESCRIPTION_2("common.room.description.2"),
    COMMON_ROOM_DESCRIPTION_3("common.room.description.3"),
    COMMON_ROOM_DESCRIPTION_4("common.room.description.4"),
    COMMON_ROOM_DESCRIPTION_5("common.room.description.5"),
    COMMON_ROOM_DESCRIPTION_6("common.room.description.6"),
    COMMON_ROOM_DESCRIPTION_7("common.room.description.7"),
    COMMON_ROOM_DESCRIPTION_8("common.room.description.8"),
    COMMON_ROOM_DESCRIPTION_9("common.room.description.9"),
    COMMON_ROOM_DESCRIPTION_10("common.room.description.10"),
    COMMON_ROOM_DESCRIPTION_11("common.room.description.11"),
    COMMON_ROOM_DESCRIPTION_12("common.room.description.12"),
    COMMON_ROOM_DESCRIPTION_13("common.room.description.13"),
    COMMON_ROOM_DESCRIPTION_14("common.room.description.14"),
    COMMON_ROOM_DESCRIPTION_15("common.room.description.15"),
    COMMON_ROOM_DESCRIPTION_16("common.room.description.16"),
    COMMON_ROOM_DESCRIPTION_17("common.room.description.17"),
    COMMON_ROOM_DESCRIPTION_18("common.room.description.18"),
    COMMON_ROOM_DESCRIPTION_19("common.room.description.19"),
    COMMON_ROOM_DESCRIPTION_20("common.room.description.20"),
    COMMON_ROOM_DESCRIPTION_21("common.room.description.21"),
    COMMON_ROOM_DESCRIPTION_22("common.room.description.22"),
    COMMON_ROOM_DESCRIPTION_23("common.room.description.23"),
    COMMON_ROOM_DESCRIPTION_24("common.room.description.24"),
    COMMON_ROOM_DESCRIPTION_25("common.room.description.25"),
    COMMON_ROOM_DESCRIPTION_26("common.room.description.26"),
    COMMON_ROOM_DESCRIPTION_27("common.room.description.27"),
    COMMON_ROOM_DESCRIPTION_28("common.room.description.28"),
    COMMON_ROOM_DESCRIPTION_29("common.room.description.29"),
    COMMON_ROOM_DESCRIPTION_30("common.room.description.30"),
    COMMON_ROOM_DESCRIPTION_31("common.room.description.31"),
    COMMON_ROOM_DESCRIPTION_32("common.room.description.32"),
    COMMON_ROOM_DESCRIPTION_33("common.room.description.33"),
    COMMON_ROOM_DESCRIPTION_34("common.room.description.34"),
    COMMON_ROOM_DESCRIPTION_35("common.room.description.35"),
    COMMON_ROOM_DESCRIPTION_36("common.room.description.36"),
    COMMON_ROOM_DESCRIPTION_37("common.room.description.37"),
    COMMON_ROOM_DESCRIPTION_38("common.room.description.38"),
    COMMON_ROOM_DESCRIPTION_39("common.room.description.39"),
    COMMON_ROOM_DESCRIPTION_40("common.room.description.40"),

    COMMON_ROOM_DESC_EXIT("common.room.description.exit"),
    COMMON_ROOM_DESC_EMPTY("common.room.description.empty"),

    COMMON_ITEM_LIFE_POTION_DESCRIPTION("common.item.life.potion.description"),
    COMMON_ITEM_HEALTH_POTION_DESCRIPTION("common.item.health.potion.description"),
    COMMON_ITEM_STRENGTH_POTION_DESCRIPTION("common.item.strength.potion.description"),
    COMMON_ITEM_DEFENSE_POTION_DESCRIPTION("common.item.defense.potion.description"),
    COMMON_ROOM_ITEMS_ADDED("common.room.items.added"),
    COMMON_ITEM_HEALTH_POTION_USED("common.item.health.potion.used"),
    COMMON_ITEM_STRENGTH_POTION_USED("common.item.strength.potion.used"),
    COMMON_ITEM_DEFENSE_POTION_USED("common.item.defense.potion.used"),
    COMMON_ITEM_LIFE_POTION_USED("common.item.life.potion.used"),

    COMMON_COMMAND_ACTIONS("common.command.actions"),
    COMMON_COMMAND_UNKNOWN("common.command.unknown"),
    COMMON_COMMAND_QUIT("common.command.quit"),
    COMMON_COMMAND_EXPLORE("common.command.explore"),
    COMMON_COMMAND_ATTACK("common.command.attack"),
    COMMON_COMMAND_DEFEND("common.command.defend"),
    COMMON_COMMAND_USE_ITEM("common.command.use_item"),
    COMMON_COMMAND_INVENTORY("common.command.inventory"),
    COMMON_COMMAND_HELP("common.command.help"),

    COMMON_FIGHT_PROMPT("common.fight.prompt"),
    COMMON_FIGHT_UNKNOWN("common.fight.unknown"),
    COMMON_FIGHT_LAUNCHED("common.fight.launched"),
    COMMON_FIGHT_NO_ENEMIES("common.fight.no.enemies"),
    COMMON_FIGHT_NO_ENEMIES_ALIVE("common.fight.no.enemies.alive"),
    COMMON_FIGHT_ENEMY_DEAD("common.fight.enemy.dead"),
    COMMON_FIGHT_ENEMY_LOOT("common.fight.enemy.loot"),
    COMMON_ATTACK_RESULT_1("common.attack.result.1"),
    COMMON_ATTACK_RESULT_2("common.attack.result.2"),
    COMMON_ATTACK_RESULT_3("common.attack.result.3"),
    COMMON_ATTACK_RESULT_4("common.attack.result.4"),
    COMMON_ATTACK_RESULT_5("common.attack.result.5"),
    COMMON_ATTACK_RESULT_6("common.attack.result.6"),
    COMMON_ATTACK_RESULT_7("common.attack.result.7"),
    COMMON_ATTACK_RESULT_8("common.attack.result.8"),
    COMMON_ATTACK_RESULT_9("common.attack.result.9"),
    COMMON_ATTACK_RESULT_10("common.attack.result.10"),
    COMMON_ATTACK_RESULT_11("common.attack.result.11"),
    COMMON_ATTACK_RESULT_12("common.attack.result.12"),
    COMMON_ATTACK_RESULT_13("common.attack.result.13"),
    COMMON_ATTACK_RESULT_14("common.attack.result.14"),
    COMMON_ATTACK_RESULT_15("common.attack.result.15"),
    COMMON_ATTACK_RESULT_16("common.attack.result.16"),
    COMMON_ATTACK_RESULT_17("common.attack.result.17"),
    COMMON_ATTACK_RESULT_18("common.attack.result.18"),
    COMMON_ATTACK_RESULT_19("common.attack.result.19"),
    COMMON_ATTACK_RESULT_20("common.attack.result.20"),

    COMMON_CHARACTER_DEAD_1("common.character.dead.1"),
    COMMON_CHARACTER_DEAD_2("common.character.dead.2"),
    COMMON_CHARACTER_DEAD_3("common.character.dead.3"),
    COMMON_CHARACTER_DEAD_4("common.character.dead.4"),
    COMMON_CHARACTER_DEAD_5("common.character.dead.5"),
    COMMON_CHARACTER_DEAD_6("common.character.dead.6"),
    COMMON_CHARACTER_DEAD_7("common.character.dead.7"),
    COMMON_CHARACTER_DEAD_8("common.character.dead.8"),
    COMMON_CHARACTER_DEAD_9("common.character.dead.9"),
    COMMON_CHARACTER_DEAD_10("common.character.dead.10"),
    COMMON_CHARACTER_DEAD_11("common.character.dead.11"),
    COMMON_CHARACTER_DEAD_12("common.character.dead.12"),
    COMMON_CHARACTER_DEAD_13("common.character.dead.13"),
    COMMON_CHARACTER_DEAD_14("common.character.dead.14"),
    COMMON_CHARACTER_DEAD_15("common.character.dead.15"),

    COMMON_CHARACTER_DEFEAT("common.character.defeat"),

    DUNGEON_EXPLORE("dungeon.explore"),
    DUNGEON_ENTER("dungeon.enter"),
    DUNGEON_EXIT("dungeon.exit"),
    DUNGEON_ATTACK("dungeon.attack"),
    DUNGEON_DEFEND("dungeon.defend"),
    DUNGEON_USE_ITEM("dungeon.use_item"),
    DUNGEON_INVENTORY("dungeon.inventory"),
    DUNGEON_HELP("dungeon.help"),
    DUNGEON_INTRO("dungeon.intro"),
    DUNGEON_DEBUG_POSITION("dungeon.debug.position"),
    DUNGEON_DEBUG_ENEMIES("dungeon.debug.enemies"),
    DUNGEON_DEBUG_ITEMS("dungeon.debug.items"),
    DUNGEON_DEBUG_EXIT("dungeon.debug.exit"),
    DUNGEON_DEBUG_PASSAGES("dungeon.debug.passages"),;

    private final String key;

    ContentKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    public static List<ContentKey> getCommonRoomDescriptionList() {
        List<ContentKey> roomDescriptions = new ArrayList<>();
        for (ContentKey contentKey : ContentKey.values()) {
            if (contentKey.name().startsWith("COMMON_ROOM_DESCRIPTION_")) {
                roomDescriptions.add(contentKey);
            }
        }
        return roomDescriptions;
    }

    public static List<ContentKey> getCommonCharacterDeadList() {
        List<ContentKey> commonCharacterDead = new ArrayList<>();
        for (ContentKey contentKey : ContentKey.values()) {
            if (contentKey.name().startsWith("COMMON_CHARACTER_DEAD_")) {
                commonCharacterDead.add(contentKey);
            }
        }
        return commonCharacterDead;
    }

    public static List<ContentKey> getCommonAttackResultList() {
        List<ContentKey> commonAttackResults = new ArrayList<>();
        for (ContentKey contentKey : ContentKey.values()) {
            if (contentKey.name().startsWith("COMMON_ATTACK_RESULT_")) {
                commonAttackResults.add(contentKey);
            }
        }
        return commonAttackResults;
    }

}
