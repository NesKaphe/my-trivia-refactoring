package com.adaptionsoft.games.uglytrivia.out;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

public class GameConsoleOutput implements GameOutput {

    @Override
    public void printWrongAnswer(Player player) {
        System.out.println("Question was incorrectly answered");
        System.out.println(player.getName() + " was sent to the penalty box");
    }

    @Override
    public void printCorrectAnswer(Player player) {
        System.out.println("Answer was correct!!!!");
        System.out.println(player.getName()
                + " now has "
                + player.getPurses()
                + " Gold Coins.");
    }

    @Override
    public void printRollOutcome(Player player, Category category, String question) {
        System.out.println(player.getName()
                + "'s new location is "
                + player.getPlaces());
        System.out.println("The category is " + category);
        System.out.println(question);
    }

    @Override
    public void printPenaltyBoxExitStatus(Player player, boolean canGetOutOfPenaltyBox) {
        if (canGetOutOfPenaltyBox) {
            System.out.println(player.getName() + " is getting out of the penalty box");
        } else {
            System.out.println(player.getName() + " is not getting out of the penalty box");
        }
    }

    @Override
    public void printPlayerRoll(Player player, int roll) {
        System.out.println(player.getName() + " is the current player");
        System.out.println("They have rolled a " + roll);
    }

    @Override
    public void printPlayerAdded(Player player, int playerNumber) {
        System.out.println(player.getName() + " was added");
        System.out.println("They are player number " + playerNumber);
    }
}
