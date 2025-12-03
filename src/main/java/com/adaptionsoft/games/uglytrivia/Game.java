package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

import java.util.*;

public class Game {

    private final List<Player> players;

    private final EnumMap<Category, Deque<String>> questions;

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

    }

    public boolean isPlayable() {
        return (howManyPlayers() >= 2);
    }

    public boolean add(String playerName) {

		players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        Player player = players.get(currentPlayer);
        System.out.println(player.getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (player.isInPenaltyBox()) {
            if (roll % 2 == 0) {
                System.out.println(player.getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                return;
            }

            System.out.println(player.getName() + " is getting out of the penalty box");
            isGettingOutOfPenaltyBox = true;
        }

        player.moveBy(roll);
        System.out.println(player.getName()
                + "'s new location is "
                + player.getPlaces());

        Category category = currentCategory();
        System.out.println("The category is " + category);

        askQuestion(category);

    }

    private void askQuestion(Category category) {
        System.out.println(questions.get(category).removeFirst());
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

        if (player.isInPenaltyBox()) {
            if (!isGettingOutOfPenaltyBox) {
                nextPlayer();
                return true;
            }
        }

        player.addCoin();

        System.out.println("Answer was correct!!!!");
        System.out.println(player.getName()
                + " now has "
                + player.getPurses()
                + " Gold Coins.");

        return endOfTurn(player);
    }

    public boolean wrongAnswer() {
        Player player = players.get(currentPlayer);

        printWrongAnswer(player);

        player.toPenaltyBox();
        return endOfTurn(player);
    }

    private static void printWrongAnswer(Player player) {
        System.out.println("Question was incorrectly answered");
        System.out.println(player.getName() + " was sent to the penalty box");
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
