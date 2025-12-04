package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;

import java.util.*;

public class Game {

    private final List<Player> players;
    private final EnumMap<Category, Deque<String>> questions;
    
    private final GameOutput gameOutput;

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game(GameOutput gameOutput) {
        players = new ArrayList<>();
        questions = new EnumMap<>(Category.class);

        for(var category : Category.values()) {
            Deque<String> deck = new LinkedList<>();
            for (int i = 0; i < 50; i++) {
                deck.addLast(category + " Question " + i);
            }
            questions.put(category, deck);
        }
        this.gameOutput = gameOutput;
    }

    public boolean add(String playerName) {

		players.add(new Player(playerName));

        gameOutput.printPlayerAdded(playerName, players.size());
        return true;
    }

    public void roll(int roll) {
        Player player = players.get(currentPlayer);
        gameOutput.printPlayerRoll(player, roll);


        if (player.isInPenaltyBox()) {
            gameOutput.printPenaltyBoxExitStatus(player, canGetOutOfPenaltyBoxWith(roll));
        }

        if (!canMove(roll, player)) {
            return;
        }

        player.moveBy(roll);
        Category category = currentCategory();
        String question = takeQuestionFor(category);

        gameOutput.printRollOutcome(player, category, question);

    }

    private boolean canMove(int roll, Player player) {
        if (player.isInPenaltyBox()) {
            isGettingOutOfPenaltyBox = canGetOutOfPenaltyBoxWith(roll);
            return isGettingOutOfPenaltyBox;
        }
        return true;
    }

    private boolean canGetOutOfPenaltyBoxWith(int roll) {
        return roll % 2 != 0;
    }

    private String takeQuestionFor(Category category) {
        return questions.get(category).removeFirst();
    }


    private Category currentCategory() {
        Player player = players.get(currentPlayer);

        return switch (player.getPlaces() % 4) {
            case 0 -> Category.POP;
            case 1 -> Category.SCIENCE;
            case 2 -> Category.SPORTS;
            default -> Category.ROCK;
        };
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
        if (player.isInPenaltyBox()) {
            return isGettingOutOfPenaltyBox;
        }
        return true;
    }

    public boolean wrongAnswer() {
        Player player = players.get(currentPlayer);

        player.toPenaltyBox();

        gameOutput.printWrongAnswer(player);

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
