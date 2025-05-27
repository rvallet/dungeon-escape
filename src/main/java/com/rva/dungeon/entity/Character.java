package com.rva.dungeon.entity;

import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

public abstract class Character {

    private String name;
    private int health = 100;
    private int attackPower = 10;
    private int defensePower = 5;
    private int level = 1;
    private int experiencePoints = 0;
    private int gold = 0;
    private boolean isAlive = true;
    private Room currentRoom;

    public Character() {
        // Default constructor
    }

    public Character(String name) {
        this.name = name;
    }

    public Character(int health, int attackPower, int defensePower) {
        this.health = health;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public void setDefensePower(int defensePower) {
        this.defensePower = defensePower;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public String getIsAliveFormatedString(ContentService contentService) {
        return isAlive ?
                contentService.getString(ContentKey.COMMON_ROOM_ENEMIES_ALIVE_LABEL) : contentService.getString(ContentKey.COMMON_ROOM_ENEMIES_DEAD_LABEL);
    }

}
