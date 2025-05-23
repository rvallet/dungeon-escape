package com.rva.dungeon.model;

public abstract class Item {

    private String name;

    public Item() {
        // Default constructor
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
