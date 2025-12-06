package com.adaptionsoft.games.uglytrivia.entity;

import java.util.ArrayList;
import java.util.List;

public class PlayerRoaster {
    private final List<Player> players;
    private int currentPlayer;

    public PlayerRoaster() {
        this.players = new ArrayList<>();
        this.currentPlayer = 0;
    }

    public void add(Player player) {
        players.add(player);
    }

    public int size() {
        return players.size();
    }

    public void nextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }
}
