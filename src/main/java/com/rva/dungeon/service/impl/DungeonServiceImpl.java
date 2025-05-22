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

               if (availableRandomDirection != null && !current.isConnectedTo(target)) {
                   // Vérifier si la salle cible n'est pas déjà reliée dans la direction opposée à une autre salle
                   boolean alreadyLinkedTarget = target.hasPassageInDirection(Direction.getOpposite(availableRandomDirection));

                        if (!alreadyLinkedTarget) {
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

    private Direction getAvailableRandomDirection(Room room) {
        // Retourne une direction libre dans ce room, sinon null
        List<Direction> directions = new ArrayList<>(Direction.getDirections());
        for (Passage p : room.getPassages()) {
            directions.remove(p.getDirection());
        }
        if (directions.isEmpty()) return null;
        return directions.get(RandomUtils.randomMax(directions.size()-1));
    }

    private void generateEnemies(List<Room> rooms) {
        // TODO : Génération des ennemis
    }

    private void generateItems(List<Room> rooms) {
        // TODO : Génération des objets
    }

}
