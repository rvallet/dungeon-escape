package com.rva.dungeon.service.impl;

import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.Passage;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.DungeonService;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DungeonServiceImpl implements DungeonService {

    private Logger logger = LoggerFactory.getLogger(DungeonServiceImpl.class);

    @Override
    public Dungeon generate(int numberOfRooms, ContentService contentService) {
        // Création des salles
        List<Room> rooms = generateRooms(numberOfRooms, contentService);
        logger.info("Generating dungeon with {} rooms parameter", numberOfRooms);

        // Création des passages entre les salles
        generateRandomPassage(rooms);

        // Génération d'une sortie et retrait des salles sans passages
        generateExit(rooms, contentService);

        // Génération des enemies
        // TODO : Génération des ennemis
        generateEnemies(rooms);

        // Génération des objets
        // TODO : Génération des objets
        generateItems(rooms);

        // Ajout des salles au donjon
        return new Dungeon(rooms);
    }

    private List<Room> generateRooms(int numberOfRooms, ContentService contentService) {
        List<Room> rooms = new ArrayList<>();
        List<ContentKey> roomDescriptions = ContentKey.getRoomDescriptions();

        for (int i = 0; i < numberOfRooms; i++) {
            // Obtention d'une description aléatoire
            int randomIndex = RandomUtils.randomMax(roomDescriptions.size() - 1);
            ContentKey randomDescriptionKey = roomDescriptions.get(randomIndex);
            // Suppression de la description pour éviter les doublons
            roomDescriptions.remove(randomIndex);
            // Si la liste est vide, on la réinitialise
            if (roomDescriptions.isEmpty()) {
                roomDescriptions = ContentKey.getRoomDescriptions();
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

                            //TODO : remove this logs
                            // System.out.println(current.getPassages().toString());
                            // System.out.println(target.getPassages().toString());
                            connectionsMade++;
                        }
                }
            }
            // Si maxAttempts atteint et pas assez de connexions, on passe à la salle suivante
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
     *
     * @param rooms - Liste des salles du donjon
     * @param contentService - Service de contenu pour obtenir les descriptions
     */
    private void generateExit(List<Room> rooms, ContentService contentService) {
        // On retire les salles qui n'ont pas de passages ou qui n'ont pas de position
        rooms = rooms.stream()
                .filter(room -> !room.getPassages().isEmpty())
                .toList();
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
        exitRoom.setDescription(contentService.getString(ContentKey.COMMON_ROOM_DESCRIPTION_EXIT));
        exitRoom.setIsExit(true);

    }

    private void generateEnemies(List<Room> rooms) {
        // TODO : Génération des ennemis
    }

    private void generateItems(List<Room> rooms) {
        // TODO : Génération des objets
    }

}
