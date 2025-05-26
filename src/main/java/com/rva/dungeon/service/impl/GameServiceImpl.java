package com.rva.dungeon.service.impl;

import com.rva.dungeon.entity.Enemy;
import com.rva.dungeon.entity.Player;
import com.rva.dungeon.enumerated.Action;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.Item;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final ContentService contentService;
    private final DungeonServiceImpl dungeonService;

    private boolean gameStarted;
    private Dungeon dungeon;
    public static Player player;

    public GameServiceImpl(ContentService contentService) {

        this.contentService = contentService;
        this.dungeonService = new DungeonServiceImpl();
    }

    @Override
    public void startGame() {
        this.gameStarted = true;
        choixLangue();
        choixDifficulte();
        afficherIntroduction();
        demanderNomJoueur();
        placerJoueurDansSalleDepart();
        accueillirJoueur();
        afficherActionsDisponibles();
        lancerBoucleDuJeux();
    }

    /**
     * Choisit la langue du jeu :
     * 1 : Français
     * 2 : Anglais
     */
    private void choixLangue(){
        ConsoleUtils.afficher(contentService.getString(ContentKey.INIT_SELECT_LANGUAGE));
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.INIT_SELECT_LANGUAGE_PROMPT));
        switch (choix){
            case "2":
                contentService.setLocale(Locale.forLanguageTag("en-US"));
                break;
            case "1":
            default:
                contentService.setLocale(Locale.forLanguageTag("fr-FR"));
        }
    }

    /**
     * Choisit la difficulté du jeu :
     * 1 : Facile (10-20 salles)
     * 2 : Moyen (21-50 salles)
     * 3 : Difficile (51-100 salles)
     */
    private void choixDifficulte() {
        ConsoleUtils.afficher(contentService.getString(ContentKey.INIT_SELECT_DIFFICULTY));
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.BRIGHT_MAGENTA, contentService.getString(ContentKey.INIT_SELECT_DIFFICULTY_PROMPT));
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
        // Calcul des positions des salles dans le donjon
        dungeon.dungeonRoomPositionCompute();
    }

    /**
     * Affiche l'introduction du jeu
     */
    private void afficherIntroduction(){
        ConsoleUtils.afficher(ConsoleUtils.BRIGHT_YELLOW + contentService.getString(ContentKey.DUNGEON_INTRO) + ConsoleUtils.RESET);
    }

    /**
     * Demande le nom du joueur
     */
    private void demanderNomJoueur() {
        String playerName = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.COMMON_QUERY_PLAYER_NAME));
        player = new Player(playerName);
    }

    /**
     * Place le joueur dans la salle de départ
     */
    private void placerJoueurDansSalleDepart() {
        Room firstRoom = dungeon.getRooms().getFirst();
        firstRoom.setIsVisited(true);
        player.setCurrentRoom(firstRoom);
    }

    /**
     * Accueille le joueur dans le jeu
     */
    private void accueillirJoueur() {
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_GREETING) + ConsoleUtils.RETOUR, player.getName());
        ConsoleUtils.afficherCouleur(false, ConsoleUtils.YELLOW, player.getCurrentRoom().getDescription());
    }

    /**
     * Affiche la liste des actions disponibles
     */
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

    /**
     * Affiche les informations du joueur
     */
    private void afficherInformationJoueur() {
        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                    player.toFormatedString(contentService) +
                    ConsoleUtils.RESET
        );
    }

    /**
     * Affiche le détail de la salle actuelle
     */
    private void explorerSalle() {
        // TODO : Afficher les enemis de la salle actuelle et les objets
        List<Enemy> enemiesInCurrentRoom = player.getCurrentRoom().getEnemies();
        List<Item> itemsInCurrentRoom = player.getCurrentRoom().getItems();

        ConsoleUtils.afficher(
                ConsoleUtils.YELLOW +
                        player.getCurrentRoom().getDescription() +
                        ConsoleUtils.RETOUR +
                        ConsoleUtils.RESET
        );

        if (player.getCurrentRoom().isVisited()) {
            ConsoleUtils.afficher(
                    ConsoleUtils.YELLOW +
                            contentService.getString(ContentKey.COMMON_ROOM_VISITED) +
                            ConsoleUtils.RESET
            );
        }

        if (!CollectionUtils.isEmpty(enemiesInCurrentRoom)) {
            ConsoleUtils.afficher(
                    ConsoleUtils.YELLOW +
                            contentService.getString(player.getCurrentRoom().hasAnyEnemyAlive() ?
                                    ContentKey.COMMON_ROOM_ENEMIES_ALIVE : ContentKey.COMMON_ROOM_ENEMIES_DEAD) +
                            ConsoleUtils.RESET
            );
            enemiesInCurrentRoom.forEach(enemy -> {
                String enemyStatus = enemy.getIsAlive() ?
                        contentService.getString(ContentKey.COMMON_ROOM_ENEMIES_ALIVE_LABEL) : contentService.getString(ContentKey.COMMON_ROOM_ENEMIES_DEAD_LABEL);
                ConsoleUtils.afficher(
                        ConsoleUtils.YELLOW +
                                enemiesInCurrentRoom.indexOf(enemy) + 1 + " - " +
                                enemy.getName() + ConsoleUtils.SPACE + "(" + enemyStatus + ")" + ConsoleUtils.RETOUR +
                                ConsoleUtils.RESET
                );
            });
        }

        if (!CollectionUtils.isEmpty(itemsInCurrentRoom)) {
            if (!player.getCurrentRoom().hasAnyEnemyAlive()) {
                ConsoleUtils.afficher(
                        ConsoleUtils.YELLOW +
                                contentService.getString(ContentKey.COMMON_ROOM_ITEMS) +
                                ConsoleUtils.RETOUR +
                                ConsoleUtils.RESET
                );
                itemsInCurrentRoom.forEach(item -> {
                    ConsoleUtils.afficher(
                            ConsoleUtils.YELLOW +
                                    itemsInCurrentRoom.indexOf(item) + 1 + " - " +
                                    item.getName() + ConsoleUtils.SPACE + "(" + item.getName() + ")" + ConsoleUtils.RETOUR +
                                    ConsoleUtils.RESET
                    );
                });
            } else {
                // TODO : Afficher un message d'erreur si le joueur tente d'explorer une salle avec des ennemis vivants
                ConsoleUtils.afficher(
                        ConsoleUtils.RED +
                                contentService.getString(ContentKey.COMMON_ROOM_ITEMS_ERROR) +
                                ConsoleUtils.RETOUR +
                                ConsoleUtils.RESET
                );
            }
        }
    }

    /**
     * Affiche les salles du donjon, leur position et les passages
     * Pour le debug du donjon
     */
    private void explorerDonjon() {
       // TODO : Afficher les salles du donjon (DEBUG Dungeon)
        dungeon.getRooms().forEach(room -> {
            // Crée une liste de descriptions pour chaque passage
            String passagesListe = room.getPassages().stream()
                    .map(p -> p.getDirection().getContent(contentService))
                    .collect(Collectors.joining(", "));
            String position = contentService.getFormattedString(ContentKey.DUNGEON_DEBUG_POSITION, ConsoleUtils.SPACE + room.getDungeonPosition());
            String exit = contentService.getFormattedString(ContentKey.DUNGEON_DEBUG_EXIT, ConsoleUtils.SPACE + room.getIsExit());
            String enemies = contentService.getFormattedString(ContentKey.DUNGEON_DEBUG_ENEMIES, ConsoleUtils.SPACE + room.getEnemies());
            String items = contentService.getFormattedString(ContentKey.DUNGEON_DEBUG_ITEMS, ConsoleUtils.SPACE + room.getItems());

            // Affiche la salle + passages
            ConsoleUtils.afficher(true,
                    ConsoleUtils.YELLOW +
                            room.getName() + ConsoleUtils.RETOUR +
                            position + ConsoleUtils.RETOUR +
                            exit + ConsoleUtils.RETOUR +
                            enemies + ConsoleUtils.RETOUR +
                            items + ConsoleUtils.RETOUR +
                            passagesListe + ConsoleUtils.RESET
            );
        });

    }

    /**
     * Choisit une direction pour se déplacer dans le donjon
     */
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
        String input = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.COMMON_QUERY_DIRECTION));
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
            if (nextRoom.getIsExit()) {
                // TODO : Afficher message de victoire
                exitGame();
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

            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.COMMON_PROMPT));
            Action action = Action.fromInput(input, contentService);
            switch (action) {
                case QUIT:
                    exitGame();
                    break;
                case EXPLORE:
                    explorerSalle();
                    //TODO: uncomment to DEBUG dungeon
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

    /**
     * Quitte le jeu
     * Affiche un message de départ
     */
    @Override
    public void exitGame() {
        this.gameStarted = false;
        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_GOODBYE), player.getName());
    }

}
