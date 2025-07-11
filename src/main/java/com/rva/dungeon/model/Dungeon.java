package com.rva.dungeon.model;

import java.util.List;

public class Dungeon {

    private List<Room> rooms;
    private int level = 1;

    public Dungeon(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}