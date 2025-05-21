package com.rva.dungeon.model;

import com.rva.dungeon.enumerated.Direction;

public class Passage {

    private Direction direction;
    private Room room;
    private boolean isLocked;

    public Passage(Direction direction, Room room, boolean isLocked) {
        this.direction = direction;
        this.room = room;
        this.isLocked = isLocked;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

}
