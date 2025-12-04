package com.adaptionsoft.games.uglytrivia.out;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

public interface GameOutput {
    void printWrongAnswer(Player player);

    void printCorrectAnswer(Player player);

    void printRollOutcome(Player player, Category category, String question);

    void printPenaltyBoxExitStatus(Player player, boolean canGetOutOfPenaltyBox);

    void printPlayerRoll(Player player, int roll);

    void printPlayerAdded(Player player, int playerNumber);
}
