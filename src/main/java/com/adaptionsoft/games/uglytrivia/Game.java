package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

import java.util.*;

public class Game {

    private final List<Player> players;
    private final EnumMap<Category, Deque<String>> questions;
    
    private final GameConsoleOutput gameOutput;

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        players = new ArrayList<>();
        questions = new EnumMap<>(Category.class);

        for(var category : Category.values()) {
            Deque<String> deck = new LinkedList<>();
            for (int i = 0; i < 50; i++) {
                deck.addLast(category + " Question " + i);
            }
            questions.put(category, deck);
        }
        gameOutput = new GameConsoleOutput();
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
            gameOutput.printPenaltyBoxExitStatus(roll, player, this);
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

    static class GameConsoleOutput {

        public void printWrongAnswer(Player player) {
            System.out.println("Question was incorrectly answered");
            System.out.println(player.getName() + " was sent to the penalty box");
        }

        public void printCorrectAnswer(Player player) {
            System.out.println("Answer was correct!!!!");
            System.out.println(player.getName()
                    + " now has "
                    + player.getPurses()
                    + " Gold Coins.");
        }

        public void printRollOutcome(Player player, Category category, String question) {
            System.out.println(player.getName()
                    + "'s new location is "
                    + player.getPlaces());
            System.out.println("The category is " + category);
            System.out.println(question);
        }

        public void printPenaltyBoxExitStatus(int roll, Player player, Game game) {
            if (game.canGetOutOfPenaltyBoxWith(roll)) {
                System.out.println(player.getName() + " is getting out of the penalty box");
            } else {
                System.out.println(player.getName() + " is not getting out of the penalty box");
            }
        }

        public void printPlayerRoll(Player player, int roll) {
            System.out.println(player.getName() + " is the current player");
            System.out.println("They have rolled a " + roll);
        }

        public void printPlayerAdded(String playerName, int playerNumber) {
            System.out.println(playerName + " was added");
            System.out.println("They are player number " + playerNumber);
        }
    }
}
