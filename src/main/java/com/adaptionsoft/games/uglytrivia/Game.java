package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.PlayerRoaster;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;
import com.adaptionsoft.games.uglytrivia.rule.CategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.PenaltyBoxRule;

public class Game {

    private final PlayerRoaster playerRoaster;
    private final QuestionBank questionBank;

    private final CategoryRule categoryRule;
    private final PenaltyBoxRule penaltyBoxRule;

    private final GameOutput gameOutput;

    public Game(GameOutput gameOutput, QuestionBank questionBank, CategoryRule categoryRule, PenaltyBoxRule penaltyBoxRule) {
        playerRoaster = new PlayerRoaster();
        this.questionBank = questionBank;
        this.categoryRule = categoryRule;
        this.penaltyBoxRule = penaltyBoxRule;
        this.gameOutput = gameOutput;
    }

    public boolean add(String playerName) {

        Player player = new Player(playerName);
        playerRoaster.add(player);

        gameOutput.printPlayerAdded(player, playerRoaster.size());
        return true;
    }

    public void roll(int roll) {
        Player player = playerRoaster.getCurrentPlayer();
        gameOutput.printPlayerRoll(player, roll);


        if (player.isInPenaltyBox()) {
            boolean canGetOutOfPenaltyBox = penaltyBoxRule.canGetOutOfPenaltyBoxWith(roll);
            gameOutput.printPenaltyBoxExitStatus(player, canGetOutOfPenaltyBox);

            if(!canGetOutOfPenaltyBox) {
                return;
            }

            player.releaseFromPenaltyBox();
        }

        player.moveBy(roll);
        Category category = categoryRule.categoryFor(player);
        String question = questionBank.drawQuestionFor(category);

        gameOutput.printRollOutcome(player, category, question);

    }

    public boolean correctAnswer() {
        Player player = playerRoaster.getCurrentPlayer();

        if (player.isInPenaltyBox()) {
            return endOfTurn(player);
        }

        player.addCoin();
        gameOutput.printCorrectAnswer(player);

        return endOfTurn(player);
    }

    public boolean wrongAnswer() {
        Player player = playerRoaster.getCurrentPlayer();

        if (player.isInPenaltyBox()) {
            return endOfTurn(player);
        }

        player.toPenaltyBox();
        gameOutput.printWrongAnswer(player);

        return endOfTurn(player);
    }

    private boolean endOfTurn(Player player) {
        playerRoaster.nextPlayer();
        return !player.didWin();
    }
}
