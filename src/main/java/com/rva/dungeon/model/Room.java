package com.rva.dungeon.model;

import com.rva.dungeon.entity.Enemy;
import com.rva.dungeon.enumerated.Direction;
import com.rva.dungeon.service.ContentService;

import java.util.List;

public class Room {

    private String name;
    private int roomIndex;
    private String description;
    private boolean isVisited;
    private boolean isExit;

    private List<Passage> passages;
    private List<Item> items;
    private List<Enemy> enemies;

    public Room(String name, int roomIndex, String description) {
        this.name = name;
        this.roomIndex = roomIndex;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Passage> getPassages() {
        return passages;
    }

    public void setPassages(List<Passage> passages) {
        this.passages = passages;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setIsExit(boolean isExit) {
        this.isExit = isExit;
    }

    /**
     * Détermine si la salle possède un passage dans une direction donnée.
     * Cette méthode est utilisée pour vérifier si la salle actuelle peut être atteinte.
     * @param dir - Direction
     * @return - true si un passage existe dans la direction donnée, false sinon
     */
    public boolean hasPassageInDirection (Direction dir) {
        return this.getPassages().stream().anyMatch(passage -> passage.getDirection() == dir);
    }

    /**
     * Vérifie si la salle est connectée à une autre salle.
     * Cette méthode est utilisée pour déterminer si la salle actuelle peut être atteinte quelle que soit la direction.
     * @param other - Salle cible
     * @return true si connectée, false sinon
     */
    public boolean isConnectedTo(Room other) {
        return this.getPassages().stream().anyMatch(p -> p.getRoom() == other);
    }

    public static Room moveToRoomInDirection(Room currentRoom, Direction direction) {
        for (Passage passage : currentRoom.getPassages()) {
            if (passage.getDirection() == direction) {
                return passage.getRoom();
            }
        }
        return null;
    }

    public static String displayFormatedAvailableDirections(Room room, ContentService contentService) {
        StringBuilder passageList = new StringBuilder();
        for (Passage passage : room.getPassages()) {
            passageList
                    .append(passage.getDirection().getNumber())
                    .append(" - ")
                    .append(passage.getDirection().getContent(contentService))
                    .append("\n");
        }
        passageList.deleteCharAt(passageList.length() - 1);
        return passageList.toString();
    }

}
