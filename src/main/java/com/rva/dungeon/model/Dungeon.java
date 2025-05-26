package com.rva.dungeon.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Dungeon {

    private List<Room> rooms;

    public Dungeon(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Calcule la position de chaque salle dans le donjon en utilisant un parcours en largeur.
     * La première salle est positionnée à 1, et les salles voisines sont positionnées en fonction de leur distance.
     */
    public void dungeonRoomPositionCompute() {
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

            for (Passage p : current.getPassages()) {
                Room voisin = p.getRoom();
                if (voisin.getDungeonPosition() == 0) { // pas encore positionnée
                    voisin.setDungeonPosition(currentPos + 1);
                    file.add(voisin);
                }
                // Si la salle a déjà une position, on ne modifie pas
            }

        }
    }

}
