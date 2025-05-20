package com.rva.dungeon.utils.console;

import java.util.Scanner;

public class ConsoleUtils {

    // Scanner pour la saisie utilisateur
    private static Scanner scanner;

    public static final String RETOUR = "\n";
    // Code de réinitialisation
    public static final String RESET = "\u001b[0m";

    // Couleurs standards
    public static final String BLACK = "\u001b[30m";
    public static final String RED = "\u001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String BLUE = "\u001b[34m";
    public static final String MAGENTA = "\u001b[35m";
    public static final String CYAN = "\u001b[36m";
    public static final String WHITE = "\u001b[37m";

    // Couleurs claires / brillantes
    public static final String BRIGHT_BLACK = "\u001b[30;1m";
    public static final String BRIGHT_RED = "\u001b[31;1m";
    public static final String BRIGHT_GREEN = "\u001b[32;1m";
    public static final String BRIGHT_YELLOW = "\u001b[33;1m";
    public static final String BRIGHT_BLUE = "\u001b[34;1m";
    public static final String BRIGHT_MAGENTA = "\u001b[35;1m";
    public static final String BRIGHT_CYAN = "\u001b[36;1m";
    public static final String BRIGHT_WHITE = "\u001b[37;1m";

    // Effets supplémentaires
    public static final String BOLD = "\u001b[1m";
    public static final String UNDERLINE = "\u001b[4m";
    public static final String INVERT = "\u001b[7m";

    /**
     * Méthode pour obtenir un scanner unique
     * @return - Scanner
     */
    private static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    public static void afficher(String reponse, Object... args) {
        String message = String.format(reponse, args);
        System.out.println(message + RETOUR);
    }

    public static void afficherCouleur(String colorCode, String reponse, Object... args) {
        String message = String.format(reponse, args);
        System.out.println("$> " + colorCode + message + RESET);
    }

    public static String demander(String question, Object... args) {
        String message = String.format(question, args);
        System.out.print(RETOUR + "¤> ");
        System.out.print(message);
        Scanner scanner = getScanner();
        System.out.print(RETOUR + "?> ");
        return scanner.nextLine();
    }

    public static String demanderCouleur(String colorCode, String question, Object... args) {
        String message = String.format(question, args);
        System.out.print(RETOUR + "¤> ");
        System.out.print(colorCode + message + RESET);
        Scanner scanner = getScanner();
        System.out.print(RETOUR + "?> ");
        return scanner.nextLine();
    }

    /**
     * Colorise un message avec le code couleur spécifié
     * @param message - Message à coloriser
     * @param colorCode - Code couleur ANSI
     * @return - Message colorisé
     */
    private static String colorMessage(String message, String colorCode) {
        return colorCode + message + RESET;
    }

    /**
     * Méthode générale pour coloriser un message
     * @param message - Message à coloriser
     * @return - Message colorisé
     */
    public static String format(String message, String colorCode) {
        return colorMessage(message, colorCode);
    }

}
