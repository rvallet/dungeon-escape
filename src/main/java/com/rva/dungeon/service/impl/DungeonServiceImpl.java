package com.rva.dungeon.service.impl;

import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.Passage;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.service.DungeonService;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class DungeonServiceImpl implements DungeonService {

    @Override
    public Dungeon generate(int numberOfRooms, ContentService contentService) {
        // Création des salles
        List<Room> rooms = generateRooms(numberOfRooms, contentService);

        // Génération des enemies
        // TODO : Génération des ennemis
        generateEnemies(rooms);

        // Génération des objets
        // TODO : Génération des objets
        generateItems(rooms);

        // Création des passages entre les salles
        generateRandomPassage(rooms);

        // Ajout des salles au donjon
        return new Dungeon(rooms);
    }

    private List<Room> generateRooms(int numberOfRooms, ContentService contentService) {
        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < numberOfRooms; i++) {
            Room room = new Room(
                    contentService.getString(ContentKey.ROOM_NAME) + (i + 1),
                    i + 1,
                    contentService.getString(ContentKey.ROOM_DESCRIPTION)
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

            // Limiter le nombre de connexions à 4 moins le nombre de connexions déjà existantes
            int nbConnections = RandomUtils.randomMax(4) - connectionsMade;

            // Limitation des essais pour éviter les boucles infinies
            int attempts = 0;
            int maxAttempts = 2*nbConnections;

            while (nbConnections > connectionsMade && attempts < maxAttempts) {
                attempts++;
                int index = (int) (Math.random() * nbRooms);
                Room target = rooms.get(index);

                if (target != current && !areRoomsConnected(current, target)) {
                    // Vérifier si la salle possède une direction libre
                    Direction dir = getAvailableDirection(current);

                    if (dir != null) {
                        // Vérifier si la salle cible n'est pas déjà reliée dans la direction opposée à cette salle
                        boolean alreadyLinked = current.getPassages().stream()
                                .anyMatch(p -> p.getDirection() == getOppositeDirection(dir) && p.getRoom() == current);

                        if (!alreadyLinked) {
                            // Créer le passage entre les deux salles
                            current.getPassages().add(new Passage(dir, target, false));
                            // Créer le passage dans l'autre sens
                            Direction reverse = getOppositeDirection(dir);
                            target.getPassages().add(new Passage(reverse, current, false));
                            connectionsMade++;
                        }
                    }
                }
            }
            // Si maxAttempts atteint et pas assez de connexions, on passe à la salle suivante
        }
    }

    private boolean areRoomsConnected(Room a, Room b) {
        for (Passage p : a.getPassages()) {
            if (p.getRoom() == b) return true;
        }
        return false;
    }

    private Direction getAvailableDirection(Room room) {
        // Retourne une direction libre dans ce room, sinon null
        List<Direction> directions = new ArrayList<>(Direction.getDirections());
        for (Passage p : room.getPassages()) {
            directions.remove(p.getDirection());
        }
        if (directions.isEmpty()) return null;
        return directions.get(RandomUtils.randomMax(directions.size()-1));
    }

    private Direction getOppositeDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
        };
    }

    private void generateEnemies(List<Room> rooms) {
        // TODO : Génération des ennemis
    }

    private void generateItems(List<Room> rooms) {
        // TODO : Génération des objets
    }

}
