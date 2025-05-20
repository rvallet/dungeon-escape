package com.rva.dungeon.service.impl;

import com.rva.dungeon.entity.Player;
import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class GameServiceImpl implements GameService {

    private final ResourceBundle bundle;
    private boolean gameStarted = true;
    public static Player player;

    public GameServiceImpl() {
        this.bundle = ResourceBundle.getBundle("content", Locale.forLanguageTag("fr-FR"));
    }

    @Override
    public void startGame() {

        ConsoleUtils.afficher(getString(ContentKey.DUNGEON_INTRO));
        demanderNomJoueur();
        acceuillirJoueur(player.getName());

        while (gameStarted) {

            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, getString(ContentKey.COMMON_PROMPT));
            switch (input.toLowerCase()) {
                case "quitter":
                    quitterJeu();
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

    /**
     * Récupère la chaîne de caractères associée à la clé donnée du fichier de ressources.
     * @param key - la clé de la chaîne de caractères à récupérer
     * @return - la chaîne de caractères associée à la clé
     */
    private String getString(ContentKey key) {
        return bundle.getString(key.toString());
    }

    private void demanderNomJoueur() {
        String playerName = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, getString(ContentKey.COMMON_QUERY_PLAYER_NAME));
        player = new Player(playerName);
    }


    private void acceuillirJoueur(String playerName) {
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, getString(ContentKey.COMMON_GREETING), player.getName());
    }

    private void quitterJeu() {
        gameStarted = false;
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, getString(ContentKey.COMMON_GOODBYE), player.getName());
    }

}
