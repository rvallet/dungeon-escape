package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

public enum Action {

    EXPLORE(1, ContentKey.ACTION_CODE_1, ContentKey.ACTION_CODE_1_DESCRIPTION),
    DIRECTION(2, ContentKey.ACTION_CODE_2, ContentKey.ACTION_CODE_2_DESCRIPTION),
    CHARACTER(3, ContentKey.ACTION_CODE_3, ContentKey.ACTION_CODE_3_DESCRIPTION),
    FIGHT(4, ContentKey.ACTION_CODE_4, ContentKey.ACTION_CODE_4_DESCRIPTION),
    INVENTORY(5, ContentKey.ACTION_CODE_5, ContentKey.ACTION_CODE_5_DESCRIPTION),
    HELP(6, ContentKey.ACTION_CODE_6, ContentKey.ACTION_CODE_6_DESCRIPTION),
    QUIT(7, ContentKey.ACTION_CODE_7, ContentKey.ACTION_CODE_7_DESCRIPTION),
    DEBUG(666, ContentKey.ACTION_CODE_666, ContentKey.ACTION_CODE_666_DESCRIPTION); // DEBUG action for testing purposes

    private final int number;
    private final ContentKey code;
    private final ContentKey description;

    Action(int number, ContentKey code, ContentKey description) {
        this.number = number;
        this.code = code;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public ContentKey getCode() {
        return code;
    }

    public ContentKey getDescription() {
        return description;
    }

    public static Action fromInput(String input, ContentService contentService) {
        try {
            int number = Integer.parseInt(input);
            for (Action action : Action.values()) {
                if (action.getNumber() == number) {
                    return action;
                }
            }
        } catch (NumberFormatException e) {
            // S'il ne s'agit pas d'un nombre, l'action est saisie sous forme de texte
            for (Action action : Action.values()) {
                String content = contentService.getString(action.getCode());
                if (content.equalsIgnoreCase(input.trim())) {
                    return action;
                }
            }
        }
        return null;
    }

    /**
     * Retourne la liste des actions sous forme de liste afin de pouvoir
     * soit taper la commande soit son index
     * @return - Liste des actions
     */
    public static String getActionList(ContentService contentService) {
        StringBuilder actionList = new StringBuilder();
        for (Action action : Action.values()) {
            if (action == DEBUG) {
                continue; // Skip DEBUG action in the list
            }
            actionList
                    .append(action.getNumber())
                    .append(" - '")
                    .append(contentService.getString(action.getCode()))
                    .append("' (")
                    .append(contentService.getString(action.getDescription()))
                    .append(")")
                    .append("\n");
        }
        actionList.deleteCharAt(actionList.length() - 1);
        return actionList.toString();
    }

    public String getContent(ContentService contentService) {
        return contentService.getString(this.code);
    }

}
