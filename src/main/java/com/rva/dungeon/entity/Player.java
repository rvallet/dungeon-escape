package com.rva.dungeon.entity;

import com.rva.dungeon.model.EncounterCharacter;
import com.rva.dungeon.model.Item;
import com.rva.dungeon.model.Potion;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

    private String className;

    private List<Item> inventory;

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

    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Returns a formatted string representation of the player's inventory.
     * If the inventory is empty, it returns a message indicating that the inventory is empty.
     *
     * @param contentService The content service to retrieve localized strings.
     * @return A formatted string of the player's inventory.
     */
    public String getFormatedInventory(ContentService contentService) {
        if (CollectionUtils.isEmpty(inventory)){
            return contentService.getString(ContentKey.PLAYER_INVENTORY_EMPTY);
        }
        StringBuilder inventoryString = new StringBuilder();
        for (Item item : inventory) {
            inventoryString
                    .append(item.toString())
                    .append(ConsoleUtils.COMMA + ConsoleUtils.SPACE);
        }
        // Remove the last comma and space and add dot.
        if (!inventoryString.isEmpty()) {
            inventoryString.setLength(inventoryString.length() - 2); // Remove last ", "
            inventoryString.append(ConsoleUtils.DOT);
        }

        return inventoryString.toString();
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public void addPotionsToInventory(List<Item> items) {
        // Si la liste est vide, on ne fait rien.
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        // On filtre les potions et on les ajoute à l'inventaire.
        List<Item> potions = new ArrayList<>(items.stream().filter(item -> item instanceof Potion).toList());

        // Si la liste de potions est vide, on ne fait rien.
        if (CollectionUtils.isEmpty(potions)) {
            return;
        }

        // Si l'inventaire est déjà initialisé, on ajoute les potions.
        if (inventory != null) {
            inventory.addAll(potions);
        } else {
            inventory = new ArrayList<>(potions);
        }

    }

    public void removeItemFromInventory(Item item) {
        if (inventory != null) {
            inventory.remove(item);
        }
    }

    public void useItemFromInventory(Item item, ContentService contentService) {
        if (inventory != null && inventory.contains(item)) {
            switch (item) {
                case Potion potion -> {
                    switch (potion.getPotionType()) {
                        case HEALTH -> {
                            int healAmount = Math.min(potion.getAmount(), getLife() - getHealth());
                            setHealth(Math.min(getHealth() + potion.getAmount(), getLife()));
                            ConsoleUtils.afficherCouleur(
                                    ConsoleUtils.BRIGHT_YELLOW,
                                    contentService.getFormattedString(ContentKey.COMMON_ITEM_HEALTH_POTION_USED, healAmount)
                            );
                            this.removeItemFromInventory(item);
                        }
                        case STRENGTH -> {
                            setAttackPower(getAttackPower() + potion.getAmount());
                            ConsoleUtils.afficherCouleur(
                                    ConsoleUtils.BRIGHT_YELLOW,
                                    contentService.getFormattedString(ContentKey.COMMON_ITEM_STRENGTH_POTION_USED, potion.getAmount())
                            );
                            this.removeItemFromInventory(item);
                        }
                        case DEFENSE -> {
                            setDefensePower(getDefensePower() + potion.getAmount());
                            ConsoleUtils.afficherCouleur(
                                    ConsoleUtils.BRIGHT_YELLOW,
                                    contentService.getFormattedString(ContentKey.COMMON_ITEM_DEFENSE_POTION_USED, potion.getAmount())
                            );
                            this.removeItemFromInventory(item);
                        }
                        case LIFE -> {
                            setLife(getLife() + potion.getAmount());
                            setHealth(getHealth() + potion.getAmount());
                            ConsoleUtils.afficherCouleur(
                                    ConsoleUtils.BRIGHT_YELLOW,
                                    contentService.getFormattedString(ContentKey.COMMON_ITEM_LIFE_POTION_USED, potion.getAmount())
                            );
                            this.removeItemFromInventory(item);
                        }
                    }
                }
                // Non utilisés pour l'instant.
                case EncounterCharacter encounterCharacter -> {
                    encounterCharacter.interact(this, contentService);
                }
                // TODO : Ajouter d'autres types d'items si nécessaire.
                default -> throw new IllegalStateException("Unexpected value: " + item);
            }
            removeItemFromInventory(item);
        }
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
                .append(contentService.getString(ContentKey.PLAYER_NAME)).append(": ").append(getName()).append(ConsoleUtils.RETOUR)
                .append(contentService.getString(ContentKey.PLAYER_HEALTH)).append(": ").append(getHealth()).append("/").append(getLife()).append(ConsoleUtils.RETOUR)
                .append(contentService.getString(ContentKey.PLAYER_ATTACK)).append(": ").append(getAttackPower()).append(ConsoleUtils.RETOUR)
                .append(contentService.getString(ContentKey.PLAYER_DEFENSE)).append(": ").append(getDefensePower()).append(ConsoleUtils.RETOUR)
                .append(contentService.getString(ContentKey.PLAYER_GOLD)).append(": ").append(getGold()).append(ConsoleUtils.RETOUR)
                .append(contentService.getString(ContentKey.PLAYER_INVENTORY)).append(": ").append(getFormatedInventory(contentService))
                .toString();

    }

}
