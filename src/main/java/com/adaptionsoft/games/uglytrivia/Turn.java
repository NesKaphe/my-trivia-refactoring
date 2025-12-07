package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.event.GameEventPublisher;
import com.adaptionsoft.games.uglytrivia.event.payload.*;
import com.adaptionsoft.games.uglytrivia.rule.CategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.PenaltyBoxRule;

import static com.adaptionsoft.games.uglytrivia.Turn.State.*;

public class Turn {
    private final Player player;
    private final int roll;
    private final QuestionBank questionBank;
    private final PenaltyBoxRule penaltyBoxRule;
    private final CategoryRule categoryRule;
    private final GameEventPublisher gameEventPublisher;


    private State state;

    enum State {
        PENDING,
        BLOCKED,
        WAITING_ANSWER,
        FINISHED
    }

    public Turn(Player player, int roll, QuestionBank questionBank, PenaltyBoxRule penaltyBoxRule, CategoryRule categoryRule, GameEventPublisher gameEventPublisher) {
        this.player = player;
        this.roll = roll;
        this.questionBank = questionBank;
        this.penaltyBoxRule = penaltyBoxRule;
        this.categoryRule = categoryRule;
        this.gameEventPublisher = gameEventPublisher;

        this.state = PENDING;
    }

    public void start() {
        if(!PENDING.equals(state)) {
            throw new IllegalStateException("Turn already started");
        }

        gameEventPublisher.publish(new PlayerRolledPayload(player, roll));

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
        gameEventPublisher.publish(new WrongAnswerPayload(player));

        state = FINISHED;
    }

    public void correctAnswer() {
        if (!WAITING_ANSWER.equals(state)) {
            throw new IllegalStateException("Not expecting an answer");
        }

        player.addCoin();
        gameEventPublisher.publish(new CorrectAnswerPayload(player));

        state = FINISHED;
    }

    public boolean result() {
        if(!isFinished()) {
            throw new IllegalStateException("Turn not resolved");
        }
        return !player.didWin();
    }

    public boolean isFinished() {
        return state == BLOCKED || state == FINISHED;
    }

    public boolean isWaitingAnswer() {
        return WAITING_ANSWER.equals(state);
    }

    private boolean isBlockedByPenaltyBox() {
        if (player.isInPenaltyBox()) {
            boolean canGetOutOfPenaltyBox = penaltyBoxRule.canGetOutOfPenaltyBoxWith(roll);
            gameEventPublisher.publish(new PenaltyBoxReleaseStatusPayload(player, canGetOutOfPenaltyBox));

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

        gameEventPublisher.publish(new RollOutcomePayload(player, category, question));

        state = WAITING_ANSWER;
    }
}
