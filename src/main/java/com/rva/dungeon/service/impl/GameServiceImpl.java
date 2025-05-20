package com.rva.dungeon.service.impl;

import com.rva.dungeon.service.GameService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Override
    public void startGame() {

        System.out.println("Game started!");

    }

}
