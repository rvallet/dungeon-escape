package com.rva.dungeon.utils.random;

import java.util.Random;

public class RandomUtils {

    private static final Random rand = new Random();

    /**
     * Renvoie un entier aléatoire compris entre min et max inclus.
     * @param min La valeur minimale (inclusif).
     * @param max La valeur maximale (inclusif).
     * @return Un entier aléatoire compris entre min et max inclus.
     */
    public static int randomBetween(int min, int max) {
        return min < max ? min + rand.nextInt(max - min + 1) : max + rand.nextInt(min - max + 1);
    }

    /**
     * Renvoie un entier aléatoire jusqu'à max inclus.
     * @param max La valeur maximale (inclusif).
     * @return Un entier aléatoire compris entre 0 et max inclus.
     */
    public static int randomMax(int max) {
        return max > 0 ? rand.nextInt(max + 1) : 0;
    }

    /**
     * Ajuste une valeur par un pourcentage aléatoire.
     * @param value La valeur de base à ajuster.
     * @param percentage Le pourcentage d'ajustement (peut être négatif).
     * @return La valeur ajustée.
     */
    public static int adjustByPercentage(int value, int percentage) {
        double variation = percentage / 100.0;
        double randFactor = (rand.nextDouble() * 2 - 1) * variation;
        double adjustedValue = value * (1 + randFactor);
        return (int) Math.round(adjustedValue);
    }

}
