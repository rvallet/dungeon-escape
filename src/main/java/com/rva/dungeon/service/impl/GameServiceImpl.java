package com.rva.dungeon.service.impl;

import com.rva.dungeon.entity.Player;
import com.rva.dungeon.enumerated.Action;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final ContentService contentService;
    private final DungeonServiceImpl dungeonService;

    private boolean gameStarted = true;
    private Dungeon dungeon;
    public static Player player;

    public GameServiceImpl(ContentService contentService) {

        this.contentService = contentService;
        this.dungeonService = new DungeonServiceImpl();
    }

    @Override
    public void startGame() {
        choixLangue();
        choixDifficulte();
        afficherIntroduction();
        demanderNomJoueur();
        placerJoueurDansSalleDepart();
        accueillirJoueur();
        afficherActionsDisponibles();
        lancerBoucleDuJeux();
    }

    private void choixLangue(){
        ConsoleUtils.afficher(contentService.getString(ContentKey.INIT_SELECT_LANGUAGE));
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.BRIGHT_BLUE, contentService.getString(ContentKey.INIT_SELECT_LANGUAGE_PROMPT));
        switch (choix){
            case "2":
                contentService.setLocale(Locale.forLanguageTag("en-US"));
                break;
            case "1":
            default:
                contentService.setLocale(Locale.forLanguageTag("fr-FR"));
        }
    }

    private void choixDifficulte() {
        ConsoleUtils.afficher(contentService.getString(ContentKey.INIT_SELECT_DIFFICULTY));
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.BRIGHT_BLUE, contentService.getString(ContentKey.INIT_SELECT_DIFFICULTY_PROMPT));
        switch (choix){
            case "2":
                dungeon = dungeonService.generate(RandomUtils.randomBetween(21, 50), contentService);
                break;
            case "3":
                dungeon = dungeonService.generate(RandomUtils.randomBetween(51, 100), contentService);
                break;
            case "1":
            default:
                dungeon = dungeonService.generate(RandomUtils.randomBetween(10, 20), contentService);
        }
    }

    private void afficherIntroduction(){
        ConsoleUtils.afficher(ConsoleUtils.BRIGHT_BLUE + contentService.getString(ContentKey.DUNGEON_INTRO) + ConsoleUtils.RESET);
    }

    private void demanderNomJoueur() {
        String playerName = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, contentService.getString(ContentKey.COMMON_QUERY_PLAYER_NAME));
        player = new Player(playerName);
    }

    private void placerJoueurDansSalleDepart() {
        Room firstRoom = dungeon.getRooms().getFirst();
        firstRoom.setIsVisited(true);
        player.setCurrentRoom(firstRoom);
    }


    private void accueillirJoueur() {
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_GREETING) + ConsoleUtils.RETOUR, player.getName());
        ConsoleUtils.afficherCouleur(false, ConsoleUtils.YELLOW, player.getCurrentRoom().getDescription());
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

    private void afficherInformationJoueur() {
        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                    player.toFormatedString(contentService) +
                    ConsoleUtils.RESET
        );
    }

    private void explorerDonjon() {

/*        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                     dungeon.getRooms().get(0).getName() + ConsoleUtils.RETOUR +
                     dungeon.getRooms().get(0).getDescription() + ConsoleUtils.RETOUR +
                     dungeon.getRooms().get(0).getPassages().getFirst().getDirection().getContent(contentService) +
                     ConsoleUtils.RESET
        );*/

        // TODO : Afficher les salles du donjon (DEBUG Dungeon)
        dungeon.getRooms().forEach(room -> {
            // Crée une liste de descriptions pour chaque passage
            String passagesListe = room.getPassages().stream()
                    .map(p -> p.getDirection().getContent(contentService))
                    .collect(Collectors.joining(", "));

            // Affiche la salle + passages
            ConsoleUtils.afficher(
                    ConsoleUtils.YELLOW +
                            room.getName() + ConsoleUtils.RETOUR +
                            room.getDescription() + ConsoleUtils.RETOUR +
                            "Passages : " + passagesListe + ConsoleUtils.RESET
            );
        });

    }

    private void choisirDirection() {
        // TODO : Choisir une direction - Optimiser code et affichage des directions
        String directions = Room.displayFormatedAvailableDirections(
                player.getCurrentRoom(),
                contentService
        );
        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                        contentService.getString(ContentKey.COMMON_DISPLAY_DIRECTION) +
                        ConsoleUtils.RETOUR +
                        directions +
                        ConsoleUtils.RESET
        );
        String input = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, contentService.getString(ContentKey.COMMON_QUERY_DIRECTION));
        Direction direction = Direction.fromInput(input, contentService);

        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = Room.moveToRoomInDirection(currentRoom, direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            ConsoleUtils.afficher(
                    ConsoleUtils.YELLOW +
                            contentService.getString(ContentKey.COMMON_ROOM_MOVE_OUT).toLowerCase() +
                            ConsoleUtils.SPACE +
                            currentRoom.getName() + ConsoleUtils.SPACE +
                            contentService.getString(ContentKey.COMMON_ROOM_MOVE_INTO).toLowerCase() +
                            ConsoleUtils.SPACE +
                            nextRoom.getName() + ConsoleUtils.DOT + ConsoleUtils.RETOUR +
                            nextRoom.getDescription() +
                            ConsoleUtils.RESET
            );
            if (nextRoom.isVisited()) {
                ConsoleUtils.afficher(
                        ConsoleUtils.YELLOW +
                                contentService.getString(ContentKey.COMMON_ROOM_VISITED) +
                                ConsoleUtils.RESET
                );
            } else {
                nextRoom.setIsVisited(true);
            }
            if (nextRoom.isExit()) {
                // TODO : Afficher message de victoire
                gameStarted = false;
            }
        } else {
            ConsoleUtils.afficher(
                    ConsoleUtils.RED +
                            contentService.getString(ContentKey.COMMON_ROOM_ERROR) +
                            ConsoleUtils.RESET
            );
        }
    }

    /**
     * Boucle principale du jeu
     */
    private void lancerBoucleDuJeux(){
        while (gameStarted) {

            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.BLUE, contentService.getString(ContentKey.COMMON_PROMPT));
            Action action = Action.fromInput(input, contentService);
            switch (action) {
                case QUIT:
                    quitterJeu();
                    break;
                case EXPLORE:
                    explorerDonjon();
                    break;
                case HELP:
                    afficherActionsDisponibles();
                    break;
                case CHARACTER:
                    afficherInformationJoueur();
                    break;
                case DIRECTION:
                    choisirDirection();
                    break;
                case null:
                default:
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_COMMAND_UNKNOWN));
                    break;
            }

        }
    }

    private void quitterJeu() {
        gameStarted = false;
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_GOODBYE), player.getName());
    }

}
