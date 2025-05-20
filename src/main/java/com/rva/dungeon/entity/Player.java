package com.rva.dungeon.entity;

public class Player extends Character {

    private String className;

    public Player() {
        super();
    }

    public Player(String name) {
        super(name);
    }

    public Player(int health, int attackPower, int defensePower) {
        super(health, attackPower, defensePower);
    }

   public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

}
