package com.rva.dungeon.model;

import com.rva.dungeon.entity.Player;
import com.rva.dungeon.enumerated.EncounterCharacterType;
import com.rva.dungeon.enumerated.PotionType;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;

import java.util.List;
import java.util.stream.Collectors;

public class EncounterCharacter extends Item {

    private EncounterCharacterType encounterCharacterType;
    private int price;
    private List<Item> items;
    private boolean isInteracted = false;

    public EncounterCharacter(EncounterCharacterType encounterCharacterType, ContentService contentService) {
        super();
        this.encounterCharacterType = encounterCharacterType;
        this.setName(contentService.getString(encounterCharacterType.getType()));
        this.setDescription(contentService.getString(encounterCharacterType.getDescription()));

        switch (encounterCharacterType) {
            case BEGGAR:
                this.price = 25;
                this.items = List.of(
                        new Potion(PotionType.HEALTH, contentService)
                );
                break;
            case MERCHANT:
                this.price = 100;
                this.items = List.of(
                        new Potion(PotionType.HEALTH, contentService),
                        new Potion(PotionType.STRENGTH, contentService)
                );
                break;
            case HEALER:
                this.price = 50;
                this.items = List.of(
                        new Potion(PotionType.HEALTH, contentService),
                        new Potion(PotionType.HEALTH, contentService),
                        new Potion(PotionType.LIFE, contentService)
                );
                break;
            case WIZARD:
                this.price = 250;
                this.items = List.of(
                        new Potion(PotionType.HEALTH, contentService),
                        new Potion(PotionType.HEALTH, contentService),
                        new Potion(PotionType.HEALTH, contentService),
                        new Potion(PotionType.STRENGTH, contentService),
                        new Potion(PotionType.LIFE, contentService)
                );
                break;
        }
    }

    public void interact(Player player, ContentService contentService) {

        if (!isInteracted) {
            ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, this.getDescription());
        }

        String response = ConsoleUtils.demanderCouleur(
                ConsoleUtils.BRIGHT_MAGENTA,
                contentService.getString(ContentKey.ENCOUNTER_CHARACTER_QUESTION),
                this.price
        );

        if (response.equalsIgnoreCase(contentService.getString(ContentKey.COMMON_ANSWER_YES))) {
            if (player.getGold() >= price) {
                buyItems(player);
                ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_ANSWER_YES), getPrice());
                ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_REWARD), getItemsToFormat());
                ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_VANISHED) + ConsoleUtils.RETOUR);
            } else {
                ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_NOT_ENOUGH_GOLD));
                ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_VANISHED) + ConsoleUtils.RETOUR);
            }
        } else if (response.equalsIgnoreCase(contentService.getString(ContentKey.COMMON_ANSWER_NO))) {
            ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_ANSWER_NO));
            ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW, contentService.getString(ContentKey.ENCOUNTER_CHARACTER_VANISHED) + ConsoleUtils.RETOUR);
        } else {
            ConsoleUtils.afficherCouleur(ConsoleUtils.RED, contentService.getString(ContentKey.COMMON_ANSWER_ERROR) + ConsoleUtils.RETOUR);
            // Appel récursif tant que l'utilisateur ne répond pas correctement
            this.isInteracted = true;
            interact(player, contentService);
        }

    }

    public void buyItems(Player player) {
            player.setGold(player.getGold() - this.price);
            player.addPotionsToInventory(this.items);
    }

    public EncounterCharacterType getEncounterCharacterType() {
        return encounterCharacterType;
    }

    public void setEncounterCharacterType(EncounterCharacterType encounterCharacterType) {
        this.encounterCharacterType = encounterCharacterType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getItemsToFormat() {
        return items.stream()
                .map(item -> "- " + item.getName() + " (" + item.getDescription() + ")")
                .collect(Collectors.joining(ConsoleUtils.RETOUR));
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return getName() + " (" + getPrice() + ")";
    }

}
