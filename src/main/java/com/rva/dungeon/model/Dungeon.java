package com.rva.dungeon.model;

import java.util.List;

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

}