package com.rva.dungeon.service;

import com.rva.dungeon.utils.content.ContentKey;

import java.util.Locale;

public interface ContentService {

    String getString(ContentKey key);

    String getFormattedString(ContentKey key, Object... args);

    void setLocale(Locale locale);

}
