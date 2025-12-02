package com.adaptionsoft.games.uglytrivia.entity;

public class Player {
    public static final int BOARD_SIZE = 12;

    private final String name;
    private int places;
    private int purses;
    private boolean inPenaltyBox;

    public Player(String name) {
        this.name = name;
        this.places = 0;
        this.purses = 0;
        inPenaltyBox = false;
    }

    public String getName() {
        return name;
    }

    public int getPlaces() {
        return places;
    }

    public int getPurses() {
        return purses;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }

    public boolean didWin() {
        return !(getPurses() == 6);
    }

    public void toPenaltyBox() {
        inPenaltyBox = true;
    }

    public void addCoin() {
        purses++;
    }

    public void moveBy(int roll) {
        places += roll;
        if (places >= BOARD_SIZE) {
            places -= BOARD_SIZE;
        }
    }
}
