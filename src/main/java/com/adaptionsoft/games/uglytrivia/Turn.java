package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;
import com.adaptionsoft.games.uglytrivia.rule.CategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.PenaltyBoxRule;

import static com.adaptionsoft.games.uglytrivia.Turn.State.*;

public class Turn {
    private final Player player;
    private final int roll;
    private final GameOutput output;
    private final QuestionBank questionBank;
    private final PenaltyBoxRule penaltyBoxRule;
    private final CategoryRule categoryRule;

    private State state;

    enum State {
        PENDING,
        BLOCKED,
        WAITING_ANSWER,
        FINISHED
    }

    public Turn(Player player, int roll, QuestionBank questionBank, PenaltyBoxRule penaltyBoxRule, CategoryRule categoryRule, GameOutput output) {
        this.player = player;
        this.roll = roll;
        this.output = output;
        this.questionBank = questionBank;
        this.penaltyBoxRule = penaltyBoxRule;
        this.categoryRule = categoryRule;

        this.state = PENDING;
    }

    public void start() {
        if(!PENDING.equals(state)) {
            throw new IllegalStateException("Turn already started");
        }

        output.printPlayerRoll(player, roll);

        if (isBlockedByPenaltyBox()){
            state = BLOCKED;
            return;
        }

        movingPhase();
        askQuestionPhase();
    }

    public void wrongAnswer() {
        if (!WAITING_ANSWER.equals(state)) {
            throw new IllegalStateException("Not expecting an answer");
        }

        player.toPenaltyBox();
        output.printWrongAnswer(player);

        state = FINISHED;
    }

    public void correctAnswer() {
        if (!WAITING_ANSWER.equals(state)) {
            throw new IllegalStateException("Not expecting an answer");
        }

        player.addCoin();
        output.printCorrectAnswer(player);

        state = FINISHED;
    }

    public boolean result() {
        if(state != BLOCKED && state != FINISHED) {
            throw new IllegalStateException("Turn not resolved");
        }
        return !player.didWin();
    }

    public boolean hasAskedQuestion() {
        return WAITING_ANSWER.equals(state);
    }

    private boolean isBlockedByPenaltyBox() {
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

    private void movingPhase() {
        player.moveBy(roll);
    }

    private void askQuestionPhase() {
        Category category = categoryRule.categoryFor(player);
        String question = questionBank.drawQuestionFor(category);

        output.printRollOutcome(player, category, question);

        state = WAITING_ANSWER;
    }
}
