package com.rva.dungeon.enumerated;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

public enum Action {

    QUIT(1, ContentKey.ACTION_CODE_1),
    EXPLORE(2, ContentKey.ACTION_CODE_2),
    HELP(3, ContentKey.ACTION_CODE_3),
    CHARACTER(4, ContentKey.ACTION_CODE_4);

    private final int number;
    private final ContentKey code;

    Action(int number, ContentKey code) {
        this.number = number;
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public ContentKey getCode() {
        return code;
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
            actionList
                    .append(action.getNumber())
                    .append(" - ")
                    .append(contentService.getString(action.getCode()))
                    .append("\n");
        }
        actionList.deleteCharAt(actionList.length() - 1);
        return actionList.toString();
    }

    public String getContent(ContentService contentService) {
        return contentService.getString(this.code);
    }

}
