package com.rva.dungeon.utils.console;

import com.rva.dungeon.utils.random.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Scanner;

public class ConsoleUtils {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(ConsoleUtils.class);

    // Scanner pour la saisie utilisateur
    private static Scanner scanner;

    // Constantes de formatage
    public static final String RETOUR = "\n";
    public static final String SPACE = " ";
    public static final String DOT = ".";

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

    // Prompts
    public static final String QUERY_PROMPT = BRIGHT_MAGENTA  + RETOUR + "¤> " + RESET;
    public static final String COMMAND_PROMPT = BRIGHT_MAGENTA  + "?> " + RESET;
    public static final String TEXT_PROMPT = BRIGHT_MAGENTA  + "$> " + RESET;

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

    /**
     * Méthode pour afficher un message dans la console
     * @param sansEffets - Si vrai, affiche le message sans effets supplémentaires
     * @param text - Message à afficher
     * @param args - Arguments pour formater le message
     */
    public static void afficher(boolean sansEffets, String text, Object... args) {
        String message = String.format(text, args);
        if (sansEffets) {
            System.out.println(message + RETOUR);
        } else {
            impressionManuelle(message + RETOUR);
        }
    }

    /**
     * Méthode pour afficher un message dans la console.
     * Par défaut, le message est affiché avec des effets supplémentaires.
     * @param text - Message à afficher
     * @param args - Arguments pour formater le message
     */
    public static void afficher(String text, Object... args) {
        String message = String.format(text, args);
            impressionManuelle(message + RETOUR);
    }

    /**
     * Méthode pour afficher un message colorisé dans la console
     * @param sansEffets - Si vrai, affiche le message sans effets supplémentaires
     * @param colorCode - Code couleur ANSI
     * @param text - Message à afficher
     * @param args - Arguments pour formater le message
     */
    public static void afficherCouleur(boolean sansEffets, String colorCode, String text, Object... args) {
        String message = String.format(text, args);
        if (sansEffets) {
            System.out.println(colorCode + message + RESET + RETOUR);
        } else {
            impressionManuelle(colorCode + message + RESET + RETOUR);
        }
    }

    /**
     * Méthode pour afficher un message colorisé dans la console.
     * Par défaut, le message est affiché avec des effets supplémentaires.
     * @param colorCode - Code couleur ANSI
     * @param text - Message à afficher
     * @param args - Arguments pour formater le message
     */
    public static void afficherCouleur(String colorCode, String text, Object... args) {
        String message = String.format(text, args);
        impressionManuelle(TEXT_PROMPT + colorCode + message + RESET);
    }

    public static String demander(String question, Object... args) {
        String message = String.format(question, args);
        System.out.print(RETOUR + QUERY_PROMPT);
        impressionManuelle(message);
        Scanner scanner = getScanner();
        System.out.print(RETOUR + COMMAND_PROMPT);
        return scanner.nextLine();
    }

    public static String demanderCouleur(String colorCode, String question, Object... args) {
        String message = String.format(question, args);
        System.out.print(RETOUR + QUERY_PROMPT);
        impressionManuelle(colorCode + message + RESET);
        Scanner scanner = getScanner();
        System.out.print(RETOUR + COMMAND_PROMPT);
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

    public static void impressionManuelle(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.print(str.charAt(i));
            try {
                Thread.sleep(10 + RandomUtils.randomMax(40));
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

}
