package com.rva.dungeon.utils.content;

import java.util.ResourceBundle;

public class ContentUtils {

    private final ResourceBundle bundle;

    /**
     * Constructeur de la classe ContentUtils
     * @param baseName - Nom de la base de ressources
     */
    public ContentUtils(String baseName) {
        this.bundle = ResourceBundle.getBundle(baseName);
    }

    /**
     * Méthode pour obtenir une chaîne de caractères à partir de la clé
     * @param key - Clé de la chaîne de caractères
     * @return - Chaîne de caractères correspondante à la clé
     */
    public String getString(String key) {
        return bundle.getString(key);
    }

}
