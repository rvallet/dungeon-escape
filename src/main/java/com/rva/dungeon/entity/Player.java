package com.rva.dungeon.entity;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;

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

    @Override
    public String toString() {
        StringBuilder player = new StringBuilder();
        return player
                .append("Name: ").append(getName()).append("\n")
                .append("Health: ").append(getHealth()).append("\n")
                .append("attackPower: ").append(getAttackPower()).append("\n")
                .append("defensePower: ").append(getDefensePower()).append("\n")
                .toString();
    }

    public String toFormatedString(ContentService contentService) {
        StringBuilder player = new StringBuilder();
        return player
                .append(contentService.getString(ContentKey.PLAYER_NAME)).append(": ").append(getName()).append("\n")
                .append(contentService.getString(ContentKey.PLAYER_HEALTH)).append(": ").append(getHealth()).append("\n")
                .append(contentService.getString(ContentKey.PLAYER_ATTACK)).append(": ").append(getDefensePower()).append("\n")
                .append(contentService.getString(ContentKey.PLAYER_DEFENSE)).append(": ").append(getAttackPower()).append("\n")
                .append(contentService.getString(ContentKey.PLAYER_GOLD)).append(": ").append(getGold())
                .toString();

    }

}
