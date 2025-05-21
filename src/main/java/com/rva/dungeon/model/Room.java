package com.rva.dungeon.model;

import com.rva.dungeon.entity.Enemy;

import java.util.List;

public class Room {

    private String name;
    private int roomIndex;
    private String description;

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
}
