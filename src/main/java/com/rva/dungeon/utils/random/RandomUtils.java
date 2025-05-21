package com.rva.dungeon.utils.random;

import java.util.Random;

public class RandomUtils {

    private static final Random rand = new Random();

    /**
     * Renvoie un entier aléatoire compris entre min et max inclus.
     */
    public static int randomBetween(int min, int max) {
        return min + rand.nextInt(max - min + 1);
    }

    /**
     * Renvoie un entier aléatoire jusqu'à max inclus.
     */
    public static int randomMax(int max) {
        return rand.nextInt(max + 1);
    }

}
