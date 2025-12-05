package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Player> players;
    private final QuestionBank questionBank;
    
    private final GameOutput gameOutput;

    int currentPlayer = 0;

    public Game(GameOutput gameOutput) {
        players = new ArrayList<>();
        questionBank = QuestionBank.createDefaultQuestionBank();
        this.gameOutput = gameOutput;
    }

    public boolean add(String playerName) {

        Player player = new Player(playerName);
        players.add(player);

        gameOutput.printPlayerAdded(player, players.size());
        return true;
    }

    public void roll(int roll) {
        Player player = players.get(currentPlayer);
        gameOutput.printPlayerRoll(player, roll);


        if (player.isInPenaltyBox()) {
            boolean canGetOutOfPenaltyBox = canGetOutOfPenaltyBoxWith(roll);
            gameOutput.printPenaltyBoxExitStatus(player, canGetOutOfPenaltyBox);

            if(!canGetOutOfPenaltyBox) {
                return;
            }

            player.releaseFromPenaltyBox();
        }

        player.moveBy(roll);
        Category category = categoryFor(player);
        String question = questionBank.drawQuestionFor(category);

        gameOutput.printRollOutcome(player, category, question);

    }

    private Category categoryFor(Player player) {
        return switch (player.getPlaces() % 4) {
            case 0 -> Category.POP;
            case 1 -> Category.SCIENCE;
            case 2 -> Category.SPORTS;
            default -> Category.ROCK;
        };
    }

    private boolean canGetOutOfPenaltyBoxWith(int roll) {
        return roll % 2 != 0;
    }

    public boolean wasCorrectlyAnswered() {
        Player player = players.get(currentPlayer);

        if (canAnswerQuestion(player)) {
            player.addCoin();
            gameOutput.printCorrectAnswer(player);
        }

        return endOfTurn(player);
    }

    private boolean canAnswerQuestion(Player player) {
        return !player.isInPenaltyBox();
    }

    public boolean wrongAnswer() {
        Player player = players.get(currentPlayer);

        if(canAnswerQuestion(player)) {
            gameOutput.printWrongAnswer(player);

            player.toPenaltyBox();
        }

        return endOfTurn(player);
    }

    private boolean endOfTurn(Player player) {
        nextPlayer();
        return !player.didWin();
    }

    private void nextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

}
