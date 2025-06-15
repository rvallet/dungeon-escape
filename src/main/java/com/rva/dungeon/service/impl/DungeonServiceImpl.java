package com.rva.dungeon.service.impl;

import com.rva.dungeon.entity.Enemy;
import com.rva.dungeon.enumerated.EncounterCharacterType;
import com.rva.dungeon.enumerated.EnemyType;
import com.rva.dungeon.enumerated.PotionType;
import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.EncounterCharacter;
import com.rva.dungeon.model.Item;
import com.rva.dungeon.model.Passage;
import com.rva.dungeon.model.Potion;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.DungeonService;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DungeonServiceImpl implements DungeonService {

    private Logger logger = LoggerFactory.getLogger(DungeonServiceImpl.class);

    /**
     * Génère un donjon avec un nombre de salles spécifié.
     * Cette méthode crée les salles, les passages entre elles, une sortie et génère des ennemis et des objets.
     *
     * @param numberOfRooms - Nombre de salles à générer dans le donjon
     * @param contentService - Service de contenu pour obtenir les descriptions des salles et des objets
     * @return - Un objet Dungeon contenant la liste des salles générées
     */
    @Override
    public Dungeon generate(int numberOfRooms, ContentService contentService) {
        // Création des salles
        List<Room> rooms = generateRooms(numberOfRooms, contentService);
        logger.info("Generating dungeon with {} rooms parameter", numberOfRooms);

        // Création des passages entre les salles
        generateRandomPassage(rooms);

        // Calcul de la position des salles dans le donjon
        dungeonRoomPositionCompute(rooms);

        // Génération d'une sortie et retrait des salles sans passages
        generateExit(rooms, contentService);

        // Génération des enemies
        generateEnemies(rooms, contentService);

        // Génération des objets
        generateItems(rooms, contentService);

        // Ajout des salles au donjon
        return new Dungeon(rooms);
    }

    /**
     * Génère une liste de salles pour le donjon.
     * Chaque salle est créée avec un nom, un index et une description aléatoire.
     * Les descriptions sont tirées d'une liste de clés de contenu prédéfinies.
     *
     * @param numberOfRooms - Nombre de salles à générer
     * @param contentService - Service de contenu pour obtenir les descriptions
     * @return - Liste des salles générées
     */
    private List<Room> generateRooms(int numberOfRooms, ContentService contentService) {
        List<Room> rooms = new ArrayList<>();
        List<ContentKey> roomDescriptions = ContentKey.getCommonRoomDescriptionList();

        for (int i = 0; i < numberOfRooms; i++) {
            // Obtention d'une description aléatoire
            int randomIndex = RandomUtils.randomMax(roomDescriptions.size() - 1);
            ContentKey randomDescriptionKey = roomDescriptions.get(randomIndex);
            // Suppression de la description pour éviter les doublons
            roomDescriptions.remove(randomIndex);
            // Si la liste est vide, on la réinitialise
            if (roomDescriptions.isEmpty()) {
                roomDescriptions = ContentKey.getCommonRoomDescriptionList();
            }

            Room room = new Room(
                    contentService.getString(ContentKey.ROOM_NAME) + (i + 1),
                    i + 1,
                    contentService.getString(randomDescriptionKey)
            );
            room.setPassages(new ArrayList<>());
            rooms.add(room);
        }
        return rooms;
    }

    /**
     * Génère des passages aléatoires entre les salles du donjon.
     * Cette méthode parcourt chaque salle et crée des passages vers d'autres salles
     * en respectant certaines conditions pour éviter les boucles infinies.
     *
     * @param rooms - Liste des salles du donjon
     */
    private void generateRandomPassage(List<Room> rooms) {
        int nbRooms = rooms.size();
        if (nbRooms < 2) return;

        for (int i = 0; i < nbRooms; i++) {
            Room current = rooms.get(i);

            // Vérifier si la salle actuelle a déjà le nombre maximum de connexions
            int connectionsMade = current.getPassages().size();

            // Limiter le nombre de connexions à 4.
            int nbConnections = RandomUtils.randomMax(4);

            // Limitation des essais pour éviter les boucles infinies
            int attempts = 0;
            int maxAttempts = 2 * nbConnections;

            while (nbConnections > connectionsMade && attempts < maxAttempts) {
                attempts++;

                // Choisir une salle cible aléatoire en excluant la salle actuelle
                List<Room> candidates = new ArrayList<>(rooms);
                candidates.remove(current);
                if (candidates.isEmpty()) break;

                int index = (int) (Math.random() * candidates.size());
                Room target = candidates.get(index);

                // Obtenir une direction aléatoire qui est libre selon les passages existants
                Direction availableRandomDirection = getAvailableRandomDirection(current);

               if (availableRandomDirection != null) {
                   // Vérifier si la salle cible n'est pas déjà reliée dans la direction opposée à une autre salle
                   boolean alreadyLinkedTarget = target.hasPassageInDirection(Direction.getOpposite(availableRandomDirection));
                   // Vérifier si la salle actuelle n'est pas déjà connectée à la salle cible
                   boolean aldreadyConnected = current.isConnectedTo(target);

                        if (!alreadyLinkedTarget && !aldreadyConnected) {
                            // Créer le passage entre les deux salles
                            current.getPassages().add(new Passage(availableRandomDirection, target, false));
                            // Créer le passage dans l'autre sens
                            Direction reverse = Direction.getOpposite(availableRandomDirection);
                            target.getPassages().add(new Passage(reverse, current, false));

                            connectionsMade++;
                        }
                }
            }
            // Si maxAttempts atteint et pas assez de connexions, on passe à la salle suivante.
            // S'il s'avère qu'elle n'a pas de connexions, on la supprimera par la suite lors de la génération de la sortie.
        }
    }

    /**
     * Retourne une direction libre dans la salle donnée.
     * @param room - Salle actuelle
     * @return - Direction libre ou null si aucune direction n'est disponible
     */
    private Direction getAvailableRandomDirection(Room room) {
        // Retourne une direction libre dans ce room, sinon null
        List<Direction> directions = new ArrayList<>(Direction.getDirections());
        for (Passage p : room.getPassages()) {
            directions.remove(p.getDirection());
        }
        if (directions.isEmpty()) return null;
        return directions.get(RandomUtils.randomMax(directions.size()-1));
    }

    /**
     * Génère une sortie dans le donjon.
     * La sortie est placée dans une salle aléatoire parmi les salles les plus éloignées du donjon.
     * Cette méthode sélectionne les salles ayant la position la plus élevée dans le donjon,
     * puis choisit une salle avec le moins de connexions pour y placer la sortie.
     * Elle retire au préalable les salles qui n'ont pas de passages ou qui n'ont pas de position définie.
     *
     * @param rooms - Liste des salles du donjon
     * @param contentService - Service de contenu pour obtenir les descriptions
     */
    private void generateExit(List<Room> rooms, ContentService contentService) {
        // On retire les salles qui n'ont pas de passages ou qui n'ont pas de position
        rooms.removeIf(room -> room.getPassages().isEmpty());
        logger.info("Dungeon created with {} rooms", rooms.size());

        // Chercher la position la plus grande
        int maxPos = rooms.stream()
                .mapToInt(Room::getDungeonPosition)
                .max()
                .orElse(0);

        // Calculer le seuil (25% des salles les plus éloignées)
        int seuil = (int)(maxPos - (maxPos * 0.25));

        // Sélection des salles ayant une position >= seuil
        List<Room> candidates = rooms.stream()
                .filter(r -> r.getDungeonPosition() >= seuil)
                .toList();

        // Récupérer les salles ayant le moins de connexions
        int minConnections = candidates.stream()
                .mapToInt(r -> r.getPassages().size())
                .min()
                .orElse(Integer.MAX_VALUE);
        List<Room> leastConnectedRooms = candidates.stream()
                .filter(r -> r.getPassages().size() == minConnections)
                .toList();

        // Choisir une salle au hasard parmi les candidates restantes
        Room exitRoom = leastConnectedRooms.get(RandomUtils.randomMax(leastConnectedRooms.size() - 1));

        // Créer la sortie
        exitRoom.setDescription(contentService.getString(ContentKey.COMMON_ROOM_DESC_EXIT));
        exitRoom.setIsExit(true);

    }

    /**
     * Calcule la position approximative de chaque salle dans le donjon en utilisant un parcours en largeur.
     * La première salle est positionnée à 1, et les salles voisines sont positionnées en fonction de leur distance.
     * Chaque salle voisine reçoit une position égale à la position de la salle actuelle plus 1.
     * Cette méthode est utile pour établir une hiérarchie ou un ordre de visite des salles dans le donjon. Mais
     * il est possible de les atteindre plus rapidement en utilisant d'autres passages.
     */
    private void dungeonRoomPositionCompute(List<Room> rooms) {
        Queue<Room> file = new LinkedList<>();

        if (rooms.isEmpty()) return;
        // Initialisation de la file avec la première salle
        Room firstRoom = rooms.getFirst();
        firstRoom.setDungeonPosition(1);
        file.add(firstRoom);

        // Parcours en largeur pour positionner les salles
        while (!file.isEmpty()) {
            Room current = file.poll();
            int currentPos = current.getDungeonPosition();

            // Parcours des passages de la salle actuelle
            for (Passage p : current.getPassages()) {
                Room voisin = p.getRoom();
                // Si la salle voisine n'a pas encore de position dans le donjon
                if (voisin.getDungeonPosition() == 0) {
                    voisin.setDungeonPosition(currentPos + 1);
                    file.add(voisin);
                }
                // Si la salle a déjà une position, on ne modifie pas
            }

        }
    }

    /**
     * Génère des ennemis dans chaque salle du donjon.
     * Cette méthode parcourt chaque salle et génère une liste d'ennemis aléatoires en fonction de la position de la salle dans le donjon.
     *
     * @param rooms - Liste des salles du donjon
     * @param contentService - Service de contenu pour obtenir les descriptions des ennemis
     */
    private void generateEnemies(List<Room> rooms, ContentService contentService) {
        rooms.forEach(room -> {
            int dungeonPosition = room.getDungeonPosition();
            List<Enemy> enemies = generateRandomListOfEnemies(dungeonPosition, contentService);
            room.setEnemies(enemies);
        });
    }

    /**
     * Génère des objets aléatoires dans chaque salle du donjon.
     * @param rooms - Liste des salles du donjon
     * @param contentService - Service de contenu pour obtenir les descriptions des objets dans la langue du joueur
     */
    private void generateItems(List<Room> rooms, ContentService contentService) {
        // On calcule la position maximale dans le donjon pour pondérer la génération d'objets
        int maxDungeonPosition = rooms.stream()
                .mapToInt(Room::getDungeonPosition)
                .max()
                .orElse(0);

        rooms.forEach(room -> {
            // Les salles les plus éloignées ont plus de chances d'avoir des objets.
            if (RandomUtils.randomMax(maxDungeonPosition) <= room.getDungeonPosition()) {
                // On génère un nombre aléatoire d'objets entre 1 et 5.
                int numberOfItems = RandomUtils.randomBetween(1, Math.min(5, room.getDungeonPosition() + 1));

                // Génération d'objets aléatoires
                List<Item> items = new ArrayList<>();
                for (int i = 0; i < numberOfItems; i++) {
                    // On génère un objet de type Potion aléatoire
                    PotionType potionType = PotionType.getRandomPotionType();
                    items.add(new Potion(potionType, contentService));

                    // On ajoute un unique objet de type EncounterCharacter
                    boolean hasEncounterCharacter = items.stream()
                            .anyMatch(item -> item instanceof EncounterCharacter);
                    boolean randomChance = RandomUtils.randomMax(100) < 100; // 100% de chance d'avoir un EncounterCharacter
                    if (!hasEncounterCharacter && randomChance) {
                        EncounterCharacterType encounterCharacterType = EncounterCharacterType.getRandomEncounterCharacterType();
                        items.add(new EncounterCharacter(encounterCharacterType, contentService));
                    }
                }
                // On ajoute les objets à la salle
                room.setItems(items);
            } else {
                room.setItems(new ArrayList<>());
            }
        });
    }

    /**
     * Génère une liste d'ennemis aléatoires pour une salle en fonction de sa position dans le donjon.
     * Cette méthode crée un nombre d'ennemis basé sur la position de la salle,
     * @param dungeonPosition - Position de la salle dans le donjon
     * @param contentService - Service de contenu pour obtenir les descriptions des ennemis
     * @return - Liste d'ennemis générée aléatoirement
     */
    private List<Enemy> generateRandomListOfEnemies(int dungeonPosition, ContentService contentService) {
        // Générer le nombre d'ennemis selon la position de la salle dans le donjon
        int nbEnemies = maxEnemies(dungeonPosition);
        List<Enemy> enemies = new ArrayList<>();

        // Récupérer les types d'ennemis possibles pour cette position de donjon
        List<EnemyType> possibleEnemyTypes = getPossibleEnemyTypes(dungeonPosition);

        // Ajouter les ennemis aléatoires
        if (!CollectionUtils.isEmpty(possibleEnemyTypes)) {
            for (int i = 0; i < nbEnemies; i++) {
                EnemyType type = getRandomEnemyType(possibleEnemyTypes);
                // Créer un nouvel ennemi avec le type choisi
                enemies.add(new Enemy(type, contentService));
            }
        }

        // Si des liches sont présentes dans la liste sur une position inférieure à 10, on ne conserve que les liches
        if (dungeonPosition < 10 && enemies.stream().anyMatch(e -> e.enemyType == EnemyType.LICH)) {
            // On ne garde que les liches pour les positions inférieures à 10.
            enemies.removeIf(e -> e.enemyType != EnemyType.LICH);
            if (dungeonPosition < 7 && enemies.size() > 1) {
                // On limite à une seule liche pour les positions inférieures à 7.
                enemies.subList(1, enemies.size()).clear();
            }

        }

        return enemies;
    }

    /**
     * Retourne le nombre maximum d'ennemis pouvant apparaître dans une salle en fonction de sa position dans le donjon.
     * @param dungeonPosition - Position de la salle dans le donjon
     * @return - Nombre maximum d'ennemis
     */
    private int maxEnemies(int dungeonPosition) {
        switch (dungeonPosition) {
            case 0, 1 -> {
                return 0; // Pas d'ennemis dans les premières salles
            }
            case 2, 3, 4, 5 -> {
                return RandomUtils.randomBetween(1, 2); // 2 ennemis maximum dans ces salles
            }
            case 6,7,8,9 -> {
                return RandomUtils.randomBetween(1, 3); // 3 ennemis maximum dans ces salles
            }
            default -> {
                return RandomUtils.randomBetween(2, 4); // 4 ennemis maximum dans les salles finales
            }
        }
    }

    /**
     * Retourne une liste des types d'ennemis possibles en fonction de la position de la salle dans le donjon.
     * @param dungeonPosition - Position de la salle dans le donjon
     * @return - Liste des types d'ennemis possibles
     */
    private List<EnemyType> getPossibleEnemyTypes(int dungeonPosition) {

        switch (dungeonPosition) {
            case 0, 1 -> {
                return Collections.emptyList(); // Pas d'ennemis
            }
            case 2, 3, 4 -> {
                return List.of(EnemyType.ZOMBIE, EnemyType.SKELETON);
            }
            case 5, 6, 7 -> {
                return List.of(EnemyType.SKELETON, EnemyType.VAMPIRE, EnemyType.LICH);
            }
            default -> {
                return List.of(EnemyType.VAMPIRE, EnemyType.LICH);
            }
        }

    }

    /**
     * Retourne un type d'ennemi aléatoire à partir d'une liste de types possibles.
     * @param possibleTypes - Liste des types d'ennemis possibles
     * @return - Type d'ennemi aléatoire
     */
    private EnemyType getRandomEnemyType(List<EnemyType> possibleTypes) {
        int id = RandomUtils.randomMax(possibleTypes.size()-1);
        return possibleTypes.get(id);
    }

}
