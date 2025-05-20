package com.rva.dungeon.service.impl;

import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Override
    public void startGame() {

        boolean gameStarted = true;

        ConsoleUtils.afficher("Game started %s!", "TEST");
        while (gameStarted) {

            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE,"Entrez votre commande");
            switch (input.toLowerCase()) {
                case "quitter":
                    gameStarted = false;
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, "Au revoir !");
                    break;
                case "explorer":
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, "Vous explorez la salle...");
                    break;
                default:
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, "Commande inconnue.");
                    break;
            }

        }

    }

}
