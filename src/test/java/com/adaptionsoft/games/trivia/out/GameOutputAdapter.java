package com.adaptionsoft.games.trivia.out;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;

public abstract class GameOutputAdapter implements GameOutput {

    @Override
    public void printWrongAnswer(Player player) {

    }

    @Override
    public void printCorrectAnswer(Player player) {

    }

    @Override
    public void printRollOutcome(Player player, Category category, String question) {

    }

    @Override
    public void printPenaltyBoxExitStatus(Player player, boolean canGetOutOfPenaltyBox) {

    }

    @Override
    public void printPlayerRoll(Player player, int roll) {

    }

    @Override
    public void printPlayerAdded(Player player, int playerNumber) {

    }

}
