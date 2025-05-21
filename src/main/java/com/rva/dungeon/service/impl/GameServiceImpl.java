package com.rva.dungeon.service.impl;

import com.rva.dungeon.entity.Player;
import com.rva.dungeon.enumerated.Action;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GameServiceImpl implements GameService {

    private final ContentService contentService;

    private boolean gameStarted = true;
    public static Player player;

    public GameServiceImpl(ContentService contentService) {
        this.contentService = contentService;
    }

    @Override
    public void startGame() {
        choixLangue();

        ConsoleUtils.afficher(ConsoleUtils.BRIGHT_BLUE + contentService.getString(ContentKey.DUNGEON_INTRO) + ConsoleUtils.RESET);
        demanderNomJoueur();
        accueillirJoueur(player.getName());

        afficherActionsDisponibles();

        while (gameStarted) {

            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, contentService.getString(ContentKey.COMMON_PROMPT));
            Action action = Action.fromInput(input, contentService);
            switch (action) {
                case QUIT:
                    quitterJeu();
                    break;
                case EXPLORE:
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.DUNGEON_INTRO));
                    break;
                case HELP:
                    afficherActionsDisponibles();
                    break;
                case null:
                default:
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_COMMAND_UNKNOWN));
                    break;
            }

        }

    }

    private void choixLangue(){
        ConsoleUtils.afficher(contentService.getString(ContentKey.INIT_SELECT_LANGUAGE));
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.BRIGHT_BLUE, "Votre choix :");
        switch (choix){
            case "2":
                contentService.setLocale(Locale.forLanguageTag("en-US"));
                break;
            case "1":
            default:
                contentService.setLocale(Locale.forLanguageTag("fr-FR"));

        }
    }

    private void demanderNomJoueur() {
        String playerName = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, contentService.getString(ContentKey.COMMON_QUERY_PLAYER_NAME));
        player = new Player(playerName);
    }


    private void accueillirJoueur(String playerName) {
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_GREETING), playerName);
    }

    private void afficherActionsDisponibles() {
        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                     ConsoleUtils.RETOUR +
                     contentService.getString(ContentKey.COMMON_COMMAND_ACTIONS) +
                     ConsoleUtils.RESET
        );
        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                     Action.getActionList(contentService) +
                     ConsoleUtils.RESET
        );
    }

    private void quitterJeu() {
        gameStarted = false;
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_GOODBYE), player.getName());
    }

}
