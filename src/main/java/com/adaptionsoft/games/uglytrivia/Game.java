package com.adaptionsoft.games.uglytrivia;

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
    private Turn turn;

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
        turn = new Turn(player, roll, questionBank, penaltyBoxRule, categoryRule, gameOutput);

        turn.start();

        if(turn.isFinished()) {
            playerRoaster.nextPlayer();
        }
    }

    public boolean correctAnswer() {
        turn.correctAnswer();

        playerRoaster.nextPlayer();
        return turn.result();
    }

    public boolean wrongAnswer() {
        turn.wrongAnswer();

        playerRoaster.nextPlayer();
        return turn.result();
    }

    public boolean isAwaitingAnswer() {
        return turn != null && turn.isWaitingAnswer();
    }
}
