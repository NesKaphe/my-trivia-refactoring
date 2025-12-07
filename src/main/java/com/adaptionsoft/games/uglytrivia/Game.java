package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.PlayerRoaster;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.event.GameEventPublisher;
import com.adaptionsoft.games.uglytrivia.event.payload.PlayerAddedPayload;
import com.adaptionsoft.games.uglytrivia.rule.CategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.PenaltyBoxRule;

public class Game {

    private final GameEventPublisher gameEventPublisher;
    private final PlayerRoaster playerRoaster;
    private final QuestionBank questionBank;

    private final CategoryRule categoryRule;
    private final PenaltyBoxRule penaltyBoxRule;

    private Turn turn;

    public Game(GameEventPublisher gameEventPublisher, QuestionBank questionBank, CategoryRule categoryRule, PenaltyBoxRule penaltyBoxRule) {
        playerRoaster = new PlayerRoaster();
        this.questionBank = questionBank;
        this.categoryRule = categoryRule;
        this.penaltyBoxRule = penaltyBoxRule;
        this.gameEventPublisher = gameEventPublisher;
    }

    public boolean add(String playerName) {
        Player player = new Player(playerName);
        playerRoaster.add(player);

        gameEventPublisher.publish(new PlayerAddedPayload(player, playerRoaster.size()));

        return true;
    }

    public void roll(int roll) {
        if(turn != null && !turn.isFinished()) {
            throw new IllegalStateException("Cannot start new turn before resolving previous one");
        }

        Player player = playerRoaster.getCurrentPlayer();
        turn = new Turn(player, roll, questionBank, penaltyBoxRule, categoryRule, gameEventPublisher);

        turn.start();

        if(turn.isFinished()) {
            playerRoaster.nextPlayer();
        }
    }

    public boolean correctAnswer() {
        if(turn == null) {
            throw new IllegalStateException("No active turn");
        }

        turn.correctAnswer();

        playerRoaster.nextPlayer();
        return turn.result();
    }

    public boolean wrongAnswer() {
        if(turn == null) {
            throw new IllegalStateException("No active turn");
        }

        turn.wrongAnswer();

        playerRoaster.nextPlayer();
        return turn.result();
    }

    public boolean isAwaitingAnswer() {
        return turn != null && turn.isWaitingAnswer();
    }
}
