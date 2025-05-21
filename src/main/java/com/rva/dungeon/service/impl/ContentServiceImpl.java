package com.rva.dungeon.service.impl;

import com.rva.dungeon.service.ContentService;
import com.rva.dungeon.utils.content.ContentKey;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class ContentServiceImpl implements ContentService {

    private Locale locale = Locale.forLanguageTag("fr-FR");
    private ResourceBundle bundle;

    public ContentServiceImpl() {
        this.bundle = ResourceBundle.getBundle("content", locale);
    }

    /**
     * Récupère la chaîne de caractères associée à la clé donnée du fichier de ressources.
     * @param key - la clé de la chaîne de caractères à récupérer
     * @return - la chaîne de caractères associée à la clé
     */
    public String getString(ContentKey key) {
        return this.bundle.getString(key.toString());
    }

    /**
     * Change la locale du contenu et charge le bundle correspondant.
     * Requiert la présence du fichier properties
     * correspondant.
     * @param locale -
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("content", locale);
    }

}
