package com.rva.dungeon.entity;

import com.rva.dungeon.model.Room;
import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.console.ConsoleUtils;
import com.rva.dungeon.utils.content.ContentKey;
import com.rva.dungeon.utils.random.RandomUtils;

public abstract class Character {

    private String name;
    private int life = 100;
    private int health = 100;
    private int attackPower = 10;
    private int defensePower = 2;
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
        this.life = health;
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

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health <= 0) {
            health = 0; // Assure que la santé ne peut pas être négative
            this.setIsAlive(false);
        }
        this.health = health ;
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

    public void fight(Character opponent, ContentService contentService) {
        // Vérification de l'état de vie des personnages
        if (!this.getIsAlive() || !opponent.getIsAlive()) {
            return;
        }

        // Calcul des dégâts : entre 50% et 100% de la puissance d'attaque, moins 50% à 100% la puissance de défense de l'adversaire
        int attackMin = Math.round((float) this.attackPower / 2);
        int attackMax = this.attackPower;
        int defenseMin = Math.round((float) opponent.getDefensePower() / 2);
        int defenseMax = opponent.getDefensePower();

        int attackValue = RandomUtils.randomBetween(attackMin, attackMax);
        int defenseValue = RandomUtils.randomBetween(defenseMin, defenseMax);

        int damage = Math.max(1, attackValue - defenseValue);

        // Réduire la santé de l'adversaire
        if (this.getIsAlive() && opponent.getIsAlive()) {
            boolean isPlayer = this instanceof Player;
            opponent.setHealth(opponent.getHealth() - damage);
            ContentKey randomKey = ContentKey.getCommonAttackResultList().get(RandomUtils.randomMax(ContentKey.getCommonCharacterDeadList().size() - 1));
            String message = contentService.getString(randomKey)
                .replace("%attacker%", this.getName())
                .replace("%defender%", opponent.getName())
                .replace("%damage%", String.valueOf(damage));
            ConsoleUtils.afficherCouleur(
                    false,
                    isPlayer ? ConsoleUtils.BOLD + ConsoleUtils.CYAN : ConsoleUtils.ITALIC + ConsoleUtils.BRIGHT_BLUE,
                    message
            );
        }
    }

    public void launchFight(Character opponent, ContentService contentService) {
        if (!this.getIsAlive() || !opponent.getIsAlive()) {
            String msg = ConsoleUtils.RETOUR + contentService.getString(ContentKey.COMMON_FIGHT_NO_ENEMIES_ALIVE);
            ConsoleUtils.afficher(msg);
            return;
        }

        ConsoleUtils.afficherCouleur(false, ConsoleUtils.BRIGHT_YELLOW,
                ConsoleUtils.RETOUR +  contentService.getFormattedString(ContentKey.COMMON_FIGHT_LAUNCHED, this.getName(), opponent.getName()) + ConsoleUtils.RETOUR);

        while (this.getIsAlive() && opponent.getIsAlive()) {
            if (!this.getIsAlive() || !opponent.getIsAlive()) {
                break; // sortie immédiate si quelqu'un est mort
            }

            // Le joueur attaque l'adversaire
            if (this.getIsAlive() && opponent.getIsAlive()) {
                this.fight(opponent, contentService);
                if (!opponent.getIsAlive()) {
                    ContentKey randomKey = ContentKey.getCommonCharacterDeadList().get(RandomUtils.randomMax(ContentKey.getCommonCharacterDeadList().size() - 1));
                    String msg = contentService.getString(randomKey)
                            .replace("%name%", opponent.getName());
                    ConsoleUtils.afficherCouleur(false, ConsoleUtils.RED, msg);
                    break; // sortie immédiate si l'adversaire est mort
                }
            }

            if (!this.getIsAlive() || !opponent.getIsAlive()) {
                break; // sortie immédiate si quelqu'un est mort
            }

            // L'adversaire attaque le joueur
            if (opponent.getIsAlive() && this.getIsAlive()) {
                opponent.fight(this, contentService);
                if (!this.getIsAlive()) {
                    ContentKey randomKey = ContentKey.getCommonCharacterDeadList().get(RandomUtils.randomMax(ContentKey.getCommonCharacterDeadList().size() - 1));
                    String msg = contentService.getString(randomKey)
                            .replace("%name%", this.getName());
                    ConsoleUtils.afficherCouleur(false, ConsoleUtils.RED, msg);
                }
            }

        }

    }

}
