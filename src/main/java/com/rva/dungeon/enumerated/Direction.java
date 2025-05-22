package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;
import java.util.List;

public enum Direction {

    NORTH(1, ContentKey.DIRECTION_NORTH),
    SOUTH(2, ContentKey.DIRECTION_SOUTH),
    EAST(3, ContentKey.DIRECTION_EAST),
    WEST(4, ContentKey.DIRECTION_WEST);

    private final int number;
    private final ContentKey code;

    Direction(int number, ContentKey code) {
        this.number = number;
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public ContentKey getCode() {
        return code;
    }

    public static List<Direction> getDirections() {
        return List.of(Direction.values());
    }

    public static Direction fromInput(String input, ContentService contentService) {
        try {
            int number = Integer.parseInt(input);
            for (Direction direction : Direction.values()) {
                if (direction.getNumber() == number) {
                    return direction;
                }
            }
        } catch (NumberFormatException e) {
            // Si l'entrée n'est pas un nombre, la direction est saisie sous forme de texte
            for (Direction direction : Direction.values()) {
                String content = contentService.getString(direction.getCode());
                if (content.equalsIgnoreCase(input.trim())) {
                    return direction;
                }
            }
        }
        return null;
    }

    /**
     * Retourne la liste des directions sous forme de liste afin de pouvoir
     * soit taper la commande soit son index
     *
     * @return - Liste des directions
     */
    public static String getFormatedDirectionList(ContentService contentService) {
        StringBuilder directionList = new StringBuilder();
        for (Direction direction : Direction.values()) {
            directionList
                    .append(direction.getNumber())
                    .append(" - ")
                    .append(contentService.getString(direction.getCode()))
                    .append("\n");
        }
        return directionList.toString();
    }

    public String getContent(ContentService contentService) {
        return contentService.getString(this.code);
    }

    /**
     * Retourne la direction opposée à celle donnée
     *
     * @param dir - Direction
     * @return - Direction opposée
     */
    public static Direction getOpposite(Direction dir) {
        return switch (dir) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

}