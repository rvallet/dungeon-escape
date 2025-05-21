package com.rva.dungeon.service.impl;

import com.rva.dungeon.model.Dungeon;
import com.rva.dungeon.model.Passage;
import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.DungeonService;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.utils.random.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class DungeonServiceImpl implements DungeonService {

    @Override
    public Dungeon generate(int numberOfRooms) {
        // Création des salles
        List<Room> rooms = generateRooms(numberOfRooms);

        // Création des passages entre les salles
        generateRandomPassage(rooms);

        // Génération des enemies
        // TODO : Génération des ennemis
        generateEnemies(rooms);

        // Génération des objets
        // TODO : Génération des objets
        generateItems(rooms);

        // Ajout des salles au donjon
        return new Dungeon(rooms);
    }

    private List<Room> generateRooms(int numberOfRooms) {
        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < numberOfRooms; i++) {
            Room room = new Room(
                    "Salle " + (i + 1),
                    i + 1,
                    "Description salle " + (i + 1)
            );
            room.setPassages(new ArrayList<>());
            rooms.add(room);
        }
        return rooms;
    }

    private void generatePassage(List<Room> rooms) {
        if (rooms.size() < 2) return;
        Room entree = rooms.get(0);
        Room sortie = rooms.get(rooms.size() - 1);
        Room salleInter = rooms.get(1);

        // Passage simple entre entrée et salle intermédiaire
        entree.getPassages().add(new Passage(Direction.SOUTH, salleInter, false));
        salleInter.getPassages().add(new Passage(Direction.NORTH, entree, false));

        // Passage entre salle intermédiaire et sortie
        salleInter.getPassages().add(new Passage(Direction.SOUTH, sortie, false));
        sortie.getPassages().add(new Passage(Direction.NORTH, salleInter, false));

    }

    private void generateRandomPassage(List<Room> rooms) {
        int n = rooms.size();
        if (n < 2) return;

        List<Room> connectedRooms = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Room current = rooms.get(i);

            // Définir un nombre de connexions pour cette salle
            int numConnections = RandomUtils.randomMax(4);
            if (numConnections > n - 1) numConnections = n - 1;

            int connectionsMade = 0;
            while (connectionsMade < numConnections) {
                int index = (int) (Math.random() * n);
                Room target = rooms.get(index);

                // Ne pas se relier lui-même et ne pas une connexion déjà existante
                if (target != current && !areRoomsConnected(current, target)) {
                    // Créer une connexion dans une direction non encore utilisée
                    Direction dir = getAvailableDirection(current);
                    if (dir != null) {
                        current.getPassages().add(new Passage(dir, target, false));
                        // Ajouter la connexion dans l'autre direction
                        Direction reverseDir = getOppositeDirection(dir);
                        target.getPassages().add(new Passage(reverseDir, current, false));
                        connectionsMade++;
                    }
                }
            }
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
        return directions.get(RandomUtils.randomMax(directions.size()));
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
