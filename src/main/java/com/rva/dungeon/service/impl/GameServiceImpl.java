package com.rva.dungeon.service.impl;

import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class GameServiceImpl implements GameService {

    private ResourceBundle bundle;

    public GameServiceImpl() {
        this.bundle = ResourceBundle.getBundle("content", Locale.forLanguageTag("fr-FR"));
    }

    /**
     * Récupère la chaîne de caractères associée à la clé donnée du fichier de ressources.
     * @param key - la clé de la chaîne de caractères à récupérer
     * @return - la chaîne de caractères associée à la clé
     */
    private String getString(ContentKey key) {
        return bundle.getString(key.toString());
    }

    @Override
    public void startGame() {

        boolean gameStarted = true;
        String playerName = "DefaultName";
        ConsoleUtils.afficher(getString(ContentKey.COMMON_GREETING), playerName);
        while (gameStarted) {

            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, getString(ContentKey.COMMON_PROMPT));
            switch (input.toLowerCase()) {
                case "quitter":
                    gameStarted = false;
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, getString(ContentKey.COMMON_GOODBYE));
                    break;
                case "explorer":
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, getString(ContentKey.DUNGEON_INTRO));
                    break;
                default:
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, getString(ContentKey.COMMON_COMMAND_UNKNOWN));
                    break;
            }

        }

    }

}
