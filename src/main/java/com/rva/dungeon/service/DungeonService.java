package com.rva.dungeon.service;

import com.rva.dungeon.model.Dungeon;

public interface DungeonService {

    Dungeon generate(int numberOfRooms, ContentService contentService);

}
