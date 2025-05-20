package com.rva.dungeon.enumerated;

public enum Action {

    QUIT(1, "quitter"),
    EXPLORE(2, "explorer"),
    HELP(3, "aide");

    private final int number;
    private final String code;

    Action(int number, String code) {
        this.number = number;
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public String getCode() {
        return code;
    }

    public static Action fromInput(String input) {
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
                if (action.getCode().equalsIgnoreCase(input)) {
                    return action;
                }
            }
        }
        return null; // Aucune action correspondante trouvée
    }

    public static String getActionList() {
        StringBuilder actionList = new StringBuilder();
        for (Action action : Action.values()) {
            actionList.append(action.getNumber()).append(" - ").append(action.getCode()).append("\n");
        }
        return actionList.toString();
    }

}
