package com.rva.dungeon.model;

import com.rva.dungeon.entity.Enemy;

import java.util.List;

public class Room {

    private String nom;
    private int numSalle;
    private String description;

    private List<Passage> passages;
    private List<Item> items;
    private List<Enemy> enemies;

    public Room(String nom, int numSalle, String description) {
        this.nom = nom;
        this.numSalle = numSalle;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNumSalle() {
        return numSalle;
    }

    public void setNumSalle(int numSalle) {
        this.numSalle = numSalle;
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
