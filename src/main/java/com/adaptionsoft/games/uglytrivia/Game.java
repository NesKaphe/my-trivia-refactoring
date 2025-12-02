package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

import java.util.*;

public class Game {

    List<Player> playerslist = new ArrayList<>();

    private final EnumMap<Category, Deque<String>> questions = new EnumMap<>(Category.class);

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
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

		playerslist.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + playerslist.size());
        return true;
    }

    public int howManyPlayers() {
        return playerslist.size();
    }

    public void roll(int roll) {
        Player player = playerslist.get(currentPlayer);
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
        System.out.println("The category is " + currentCategory());

        askQuestion();

    }

    private void askQuestion() {
        Category category = currentCategory();
        System.out.println(questions.get(category).removeFirst());
    }


    private Category currentCategory() {
        Player player = playerslist.get(currentPlayer);
        if (player.getPlaces() == 0) return Category.POP;
        if (player.getPlaces() == 4) return Category.POP;
        if (player.getPlaces() == 8) return Category.POP;
        if (player.getPlaces() == 1) return Category.SCIENCE;
        if (player.getPlaces() == 5) return Category.SCIENCE;
        if (player.getPlaces() == 9) return Category.SCIENCE;
        if (player.getPlaces() == 2) return Category.SPORTS;
        if (player.getPlaces() == 6) return Category.SPORTS;
        if (player.getPlaces() == 10) return Category.SPORTS;
        return Category.ROCK;
    }

    public boolean wasCorrectlyAnswered() {
        Player player = playerslist.get(currentPlayer);

        if (player.isInPenaltyBox()) {
            if (!isGettingOutOfPenaltyBox) {
                nextPlayer();
                return true;
            }
        }

        System.out.println("Answer was correct!!!!");
        player.addCoin();
        System.out.println(player.getName()
                + " now has "
                + player.getPurses()
                + " Gold Coins.");

        boolean winner = player.didWin();
        nextPlayer();

        return winner;
    }

    public boolean wrongAnswer() {
        Player player = playerslist.get(currentPlayer);
        System.out.println("Question was incorrectly answered");
        System.out.println(player.getName() + " was sent to the penalty box");
        player.toPenaltyBox();

        nextPlayer();
        return true;
    }

    private void nextPlayer() {
        currentPlayer++;
        if (currentPlayer == playerslist.size()) currentPlayer = 0;
    }

}
