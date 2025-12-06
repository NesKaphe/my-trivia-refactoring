package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;
import com.adaptionsoft.games.uglytrivia.rule.CategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.PenaltyBoxRule;

class Turn {
    private final Player player;
    private final int roll;
    private final GameOutput output;
    private final QuestionBank questionBank;
    private final PenaltyBoxRule penaltyBoxRule;
    private final CategoryRule categoryRule;

    public Turn(Player player, int roll, QuestionBank questionBank, PenaltyBoxRule penaltyBoxRule, CategoryRule categoryRule, GameOutput output) {
        this.player = player;
        this.roll = roll;
        this.output = output;
        this.questionBank = questionBank;
        this.penaltyBoxRule = penaltyBoxRule;
        this.categoryRule = categoryRule;
    }

    public void start() {
        output.printPlayerRoll(player, roll);
    }

    public boolean isBlockedByPenaltyBox() {
        if (player.isInPenaltyBox()) {
            boolean canGetOutOfPenaltyBox = penaltyBoxRule.canGetOutOfPenaltyBoxWith(roll);
            output.printPenaltyBoxExitStatus(player, canGetOutOfPenaltyBox);

            if (!canGetOutOfPenaltyBox) {
                return true;
            }

            player.releaseFromPenaltyBox();
        }
        return false;
    }

    public void movingPhase() {
        player.moveBy(roll);
    }

    public void askQuestionPhase() {
        Category category = categoryRule.categoryFor(player);
        String question = questionBank.drawQuestionFor(category);

        output.printRollOutcome(player, category, question);
    }

    public void wrongAnswer() {
        if (!player.isInPenaltyBox()) {
            player.toPenaltyBox();
            output.printWrongAnswer(player);
        }
    }

    public boolean endOfTurn() {
        return !player.didWin();
    }

    public void correctAnswer() {
        if (!player.isInPenaltyBox()) {
            player.addCoin();
            output.printCorrectAnswer(player);
        }
    }
}
