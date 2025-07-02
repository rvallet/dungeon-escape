package com.rva.dungeon.service.impl;

import com.rva.dungeon.entity.Enemy;
import com.rva.dungeon.entity.Player;
import com.rva.dungeon.enumerated.Action;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.enumerated.PotionType;
import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.EncounterCharacter;
import com.rva.dungeon.model.Item;
import com.rva.dungeon.model.Potion;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.GameService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.INIT_SELECT_PROMPT));
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
     * 1 : Facile (11-20 salles)
     * 2 : Moyen (21-50 salles)
     * 3 : Difficile (51-100 salles)
     */
    private void choixDifficulte() {
        ConsoleUtils.afficher(ConsoleUtils.RETOUR + contentService.getString(ContentKey.INIT_SELECT_DIFFICULTY));
        String choix = ConsoleUtils.demanderCouleur(ConsoleUtils.BRIGHT_MAGENTA, contentService.getString(ContentKey.INIT_SELECT_PROMPT));
        switch (choix){
            case "2":
                dungeon = dungeonService.generate(RandomUtils.randomBetween(21, 50), contentService);
                break;
            case "3":
                dungeon = dungeonService.generate(RandomUtils.randomBetween(51, 100), contentService);
                break;
            case "1":
            default:
                dungeon = dungeonService.generate(RandomUtils.randomBetween(11, 20), contentService);
        }

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

        // On initialise le joueur avec un donjon et 2 potions de santé
        List<Item> items = Arrays.asList(
                new Potion(PotionType.HEALTH, contentService),
                new Potion(PotionType.HEALTH, contentService)
        );
        player.addPotionsToInventory(items);

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
                true,
                ConsoleUtils.YELLOW +
                     Action.getActionList(contentService) +
                     ConsoleUtils.RESET
        );
    }

    /**
     * Affiche les informations du joueur
     *
     */
    private void afficherInformationJoueur() {
        ConsoleUtils.afficher(
                true,
                ConsoleUtils.YELLOW +
                    player.toFormatedString(contentService) +
                    ConsoleUtils.RESET
        );
    }

    /**
     * Affiche le détail de la salle actuelle
     */
    private void explorerSalle() {
        List<Enemy> enemiesInCurrentRoom = player.getCurrentRoom().getEnemies();
        List<Item> itemsInCurrentRoom = player.getCurrentRoom().getItems();

        // Si la salle est vide, on affiche un message d'information
        if (CollectionUtils.isEmpty(enemiesInCurrentRoom) && CollectionUtils.isEmpty(itemsInCurrentRoom)) {
            ConsoleUtils.afficher(
                    ConsoleUtils.YELLOW +
                            contentService.getString(ContentKey.COMMON_ROOM_DESC_EMPTY) +
                            ConsoleUtils.RETOUR +
                            player.getCurrentRoom().getDescription() +
                            ConsoleUtils.RESET
            );
        }

        // Si la salle contient des items :
        if (!CollectionUtils.isEmpty(itemsInCurrentRoom)) {
            boolean hasPotionItem = itemsInCurrentRoom.stream()
                    .anyMatch(item -> item instanceof Potion);
            boolean hasEncounterCharacterItem = itemsInCurrentRoom.stream()
                    .anyMatch(item -> item instanceof EncounterCharacter);

            // Si la salle ne contient pas d'ennemis vivants, on affiche les items
            if (!player.getCurrentRoom().hasAnyEnemyAlive()) {

                // Si la salle contient des personnages, on lance l'interaction en premier lieu
                if (hasEncounterCharacterItem) {
                    List<Item> itemsToRemove = new ArrayList<>();
                    for (Item item : itemsInCurrentRoom) {
                        if (item instanceof EncounterCharacter encounterCharacter) {
                            encounterCharacter.interact(player, contentService);
                            itemsToRemove.add(item);
                        }
                    }
                    // On retire l'item de la salle après l'interaction
                    player.getCurrentRoom().getItems().removeAll(itemsToRemove);
                }

                // Si la salle contient des potions, on les affiche
                if (hasPotionItem) {
                    ConsoleUtils.afficher(
                            ConsoleUtils.YELLOW +
                                    contentService.getString(ContentKey.COMMON_ROOM_ITEMS) +
                                    ConsoleUtils.RESET
                    );

                    for (Item item : itemsInCurrentRoom) {
                        if (item instanceof Potion potion) {
                            int index = itemsInCurrentRoom.indexOf(item) + 1;
                            ConsoleUtils.afficher(
                                    ConsoleUtils.YELLOW +
                                            index + " - " +
                                            item.getName() + ConsoleUtils.SPACE +
                                            ConsoleUtils.OPEN_PARENTHESIS + item.getDescription() + ConsoleUtils.CLOSE_PARENTHESIS +
                                            ConsoleUtils.RESET
                            );
                        }
                    }

                    // On ajoute les items à l'inventaire du joueur
                    ConsoleUtils.afficher(
                            ConsoleUtils.YELLOW +
                                    contentService.getString(ContentKey.COMMON_ROOM_ITEMS_ADDED) +
                                    ConsoleUtils.RETOUR
                    );
                    player.addPotionsToInventory(itemsInCurrentRoom);

                    // On vide la liste des items de la salle
                    player.getCurrentRoom().setItems(null);

                }

            } else {
                // Si des ennemis sont présents, on n'affiche pas les items
                ConsoleUtils.afficher(
                        ConsoleUtils.RED +
                                contentService.getString(ContentKey.COMMON_ROOM_ITEMS_ERROR) +
                                ConsoleUtils.RESET
                );
            }
        }

        // Si la salle contient des ennemis, on les affiche
        if (!CollectionUtils.isEmpty(enemiesInCurrentRoom)) {
            ConsoleUtils.afficher(
                    ConsoleUtils.YELLOW +
                            contentService.getString(player.getCurrentRoom().hasAnyEnemyAlive() ?
                                    ContentKey.COMMON_ROOM_ENEMIES_ALIVE : ContentKey.COMMON_ROOM_ENEMIES_DEAD) +
                            ConsoleUtils.RESET
            );
            enemiesInCurrentRoom.forEach(enemy -> {
                int index = enemiesInCurrentRoom.indexOf(enemy) + 1;
                String enemyStatus = enemy.getIsAliveFormatedString(contentService);
                ConsoleUtils.afficher(
                        ConsoleUtils.YELLOW +
                                index + " - " +
                                enemy.getName() + ConsoleUtils.SPACE +
                                ConsoleUtils.OPEN_PARENTHESIS + enemyStatus + ConsoleUtils.CLOSE_PARENTHESIS +
                                ConsoleUtils.RESET
                );
            });
        }

    }

    /**
     * Affiche les salles du donjon, leur position, la liste des adversaires et potions ainsi que les passages.
     * Permet de visualiser rapidement la structure du donjon pour le débogage, particulière pour équilibrer la génération aléatoire.
     */
    private void explorerDonjon() {
        dungeon.getRooms().forEach(room -> {
            // Crée une liste de descriptions pour chaque passage
            String passagesListe = room.getPassages().stream()
                    .map(p -> p.getDirection().getContent(contentService))
                    .collect(Collectors.joining(", "));
            String passages = contentService.getFormattedString(ContentKey.DUNGEON_DEBUG_PASSAGES, ConsoleUtils.SPACE + passagesListe);
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
                            passages + ConsoleUtils.RESET
            );
        });

    }

    /**
     * Choisit une direction pour se déplacer dans le donjon
     * Affiche les directions disponibles dans la salle actuelle
     * Si la direction est valide, déplace le joueur dans la salle correspondante
     * Si la salle est une sortie, termine le jeu
     */
    private void choisirDirection() {
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

        if (!tentativeFuite()) {
            return; // Si la tentative de fuite échoue, on ne continue pas
        }

        // Si le joueur n'est pas vivant après la tentative de fuite, on quitte le jeu
        if (!player.getIsAlive()) {
            exitGame();
            return;
        }

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

    private boolean tentativeFuite() {
        // Vérifie s'il y a des ennemis vivants dans la salle actuelle
        long nbAdversairesVivants = player.getCurrentRoom().getEnemies().stream()
                .filter(Enemy::getIsAlive)
                .count();
        // Si des ennemis sont présents
        if (nbAdversairesVivants > 0) {
            // On demande au joueur s'il veut tenter de fuir la salle
            ConsoleUtils.afficher(
                    ConsoleUtils.RED +
                            contentService.getString(ContentKey.COMMON_ESCAPE_QUERY) +
                            ConsoleUtils.RESET
            );

            // Chances proportionnelles au nombre de sorties et d'ennemis vivants de déclencher un combat.
            double ratio = (double) nbAdversairesVivants / (double) player.getCurrentRoom().getPassages().size();
            double escapeChance;
            if (ratio < 1.0) {
                escapeChance = 0.75; // Si le ratio est inférieur à 1, on a 75% de chances de fuir
            } else if (ratio == 1.0) {
                escapeChance = 0.50; // Si le ratio est égal à 1, on a 50% de chances de fuir
            } else {
                escapeChance = 0.25; // Si le ratio est supérieur à 1, on a 25% de chances de fuir
            }

            while (true) {
                // On demande au joueur s'il veut tenter de fuir et on affiche le pourcentage de chances de réussite
                String confirmation = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.COMMON_ESCAPE_CONFIRMATION), (int) Math.round(escapeChance * 100));

                if (confirmation.equalsIgnoreCase(contentService.getString(ContentKey.COMMON_ANSWER_YES))) {
                    // Si le joueur décide de tenter de sortir de la salle avec des ennemis vivants,
                    if (Math.random() < escapeChance) {
                        // Si le joueur réussit à fuir, on affiche un message de succès
                        ConsoleUtils.afficherCouleur(ConsoleUtils.GREEN, contentService.getString(ContentKey.COMMON_ESCAPE_SUCCESS) + ConsoleUtils.RETOUR);
                    } else {
                        // Si le joueur échoue à fuir, on lance un combat avec les ennemis présents
                        ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_ESCAPE_FAILURE) + ConsoleUtils.RETOUR);
                        player.getCurrentRoom().getEnemies().stream()
                                .filter(Enemy::getIsAlive)
                                .findFirst()
                                .ifPresent(enemy -> {
                                    // On lance le combat avec l'ennemi vivant
                                    player.launchFight(enemy, contentService);
                                    // Si l'ennemi est mort et qu'il a de l'or, on affiche un message de loot
                                    if (player.getIsAlive() && !enemy.getIsAlive() && enemy.getGold() > 0) {
                                        ConsoleUtils.afficher(
                                                true,
                                                ConsoleUtils.BRIGHT_YELLOW +
                                                contentService.getFormattedString(ContentKey.COMMON_FIGHT_ENEMY_LOOT, player.getName(), enemy.getGold(), enemy.getName()));
                                        player.setGold(player.getGold() + enemy.getGold());
                                        enemy.setGold(0);
                                    }
                                });
                    }
                    return true; // On retourne true pour indiquer que le joueur a tenté de fuir. Sortie de la boucle

                } else if (confirmation.equalsIgnoreCase(contentService.getString(ContentKey.COMMON_ANSWER_NO))) {
                    return false; // On retourne false pour indiquer que le joueur n'a pas tenté de fuir, et on continue le jeu. Sortie de la boucle
                } else {
                    ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_ANSWER_ERROR) + ConsoleUtils.RETOUR);
                    // Appel récursif tant que l'utilisateur ne répond pas correctement (on reste dans la boucle while)
                }
            }

        }
        return true; // Si aucun ennemi n'est présent, on retourne true pour continuer le jeu
    }

    private void combattreEnnemis() {
        // Vérifier s'il y a des ennemis dans la salle actuelle
        List<Enemy> enemiesInCurrentRoom = player.getCurrentRoom().getEnemies();

        if (CollectionUtils.isEmpty(enemiesInCurrentRoom)) {
            ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_FIGHT_NO_ENEMIES));
            return;
        }

        // Vérifier s'il y a des ennemis vivants dans la salle actuelle
        if (!player.getCurrentRoom().hasAnyEnemyAlive()) {
            ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_FIGHT_NO_ENEMIES_ALIVE));
            return;
        }

        // Filtrer les ennemis vivants
        List<Enemy> aliveEnemies = enemiesInCurrentRoom.stream()
                .filter(Enemy::getIsAlive)
                .toList();
        Enemy selectedEnemy = null;

        // Si plusieurs ennemis vivants sont présents, demander au joueur de choisir un ennemi
        if (aliveEnemies.size() > 1) {
            // Demander au joueur de choisir un adversaire à combattre
            ConsoleUtils.afficherCouleur(ConsoleUtils.YELLOW, contentService.getString(ContentKey.COMMON_FIGHT_PROMPT) + ConsoleUtils.RETOUR);
            for (int i = 0; i < enemiesInCurrentRoom.size(); i++) {
                Enemy enemy = enemiesInCurrentRoom.get(i);
                ConsoleUtils.afficher(
                        ConsoleUtils.YELLOW +
                                (i + 1) + " - " +
                                enemy.getName() + ConsoleUtils.SPACE +
                                ConsoleUtils.OPEN_PARENTHESIS + enemy.getIsAliveFormatedString(contentService) + ConsoleUtils.CLOSE_PARENTHESIS +
                                ConsoleUtils.RESET
                );
            }
            String input = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.INIT_SELECT_PROMPT));

            // Vérifier si l'entrée est un nombre valide
            try {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > enemiesInCurrentRoom.size()) {
                    ConsoleUtils.afficherCouleur(false, ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_FIGHT_UNKNOWN));
                    return;
                }

                // Sélectionner l'ennemi choisi
                selectedEnemy = enemiesInCurrentRoom.get(choice - 1);

                if (!selectedEnemy.getIsAlive()) {
                    ConsoleUtils.afficherCouleur(false, ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_FIGHT_ENEMY_DEAD));
                    return;
                }
            } catch (NumberFormatException e) {
                ConsoleUtils.afficherCouleur(false, ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_FIGHT_UNKNOWN));
            }
        } else {
            // Si un seul ennemi, on le combat directement
            selectedEnemy = aliveEnemies.getFirst();
        }

        if (selectedEnemy != null) {
            player.launchFight(selectedEnemy, contentService);

            if (!player.getIsAlive()) {
                exitGame();
                return;
            }

            if (!selectedEnemy.getIsAlive() && selectedEnemy.getGold() > 0) {
                    ConsoleUtils.afficher(true, ConsoleUtils.BRIGHT_YELLOW +
                            contentService.getFormattedString(ContentKey.COMMON_FIGHT_ENEMY_LOOT, player.getName(), selectedEnemy.getGold(), selectedEnemy.getName()));
                    player.setGold(player.getGold() + selectedEnemy.getGold());
                    selectedEnemy.setGold(0);
            }

        } else {
            ConsoleUtils.afficherCouleur(false, ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_FIGHT_NO_ENEMIES_ALIVE));
        }

    }

    private void utiliserObjetInventaire() {
        if (CollectionUtils.isEmpty(player.getInventory())) {
            ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.PLAYER_INVENTORY_EMPTY));
            return;
        }

        // Demander à l'utilisateur de choisir un objet dans l'inventaire
        ConsoleUtils.afficherCouleur(false, ConsoleUtils.YELLOW, contentService.getString(ContentKey.PLAYER_INVENTORY_PROMPT) + ConsoleUtils.RETOUR);
        for (int i = 0; i < player.getInventory().size(); i++) {
            Item item = player.getInventory().get(i);
            ConsoleUtils.afficher(
                    true,
                    ConsoleUtils.YELLOW +
                            (i + 1) + " - " +
                            item.getName() + ConsoleUtils.SPACE +
                            ConsoleUtils.OPEN_PARENTHESIS + item.getDescription() + ConsoleUtils.CLOSE_PARENTHESIS +
                            ConsoleUtils.RESET
            );
        }

        String input = ConsoleUtils.demanderCouleur(ConsoleUtils.MAGENTA, contentService.getString(ContentKey.INIT_SELECT_PROMPT));

        // Vérifier si l'entrée est un nombre valide
        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > player.getInventory().size()) {
                ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.PLAYER_INVENTORY_UNKNOWN));
                return;
            }

            // Sélectionner l'objet choisi
            Item selectedItem = player.getInventory().get(choice - 1);

            // Utiliser l'objet
            player.useItemFromInventory(selectedItem, contentService);


        } catch (NumberFormatException e) {
            ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.PLAYER_INVENTORY_UNKNOWN));
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
                case FIGHT:
                    combattreEnnemis();
                    break;
                case INVENTORY:
                    utiliserObjetInventaire();
                    break;
                case DEBUG:
                    explorerDonjon();
                    break;
                case null, default:
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
        // On fait une pause pour que le joueur puisse lire le message avant de quitter
        ConsoleUtils.pause(3000);
    }

}
