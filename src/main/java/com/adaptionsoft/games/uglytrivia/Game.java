package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    List<Player> playerslist = new ArrayList<>();

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
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
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(player.getName() + " is getting out of the penalty box");
                player.moveBy(roll);

                System.out.println(player.getName()
                        + "'s new location is "
                        + player.getPlaces());
                System.out.println("The category is " + currentCategory());
                askQuestion();
            } else {
                System.out.println(player.getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            player.moveBy(roll);

            System.out.println(player.getName()
                    + "'s new location is "
                    + player.getPlaces());
            System.out.println("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void askQuestion() {
        if (currentCategory() == "Pop")
            System.out.println(popQuestions.removeFirst());
        if (currentCategory() == "Science")
            System.out.println(scienceQuestions.removeFirst());
        if (currentCategory() == "Sports")
            System.out.println(sportsQuestions.removeFirst());
        if (currentCategory() == "Rock")
            System.out.println(rockQuestions.removeFirst());
    }


    private String currentCategory() {
        Player player = playerslist.get(currentPlayer);
        if (player.getPlaces() == 0) return "Pop";
        if (player.getPlaces() == 4) return "Pop";
        if (player.getPlaces() == 8) return "Pop";
        if (player.getPlaces() == 1) return "Science";
        if (player.getPlaces() == 5) return "Science";
        if (player.getPlaces() == 9) return "Science";
        if (player.getPlaces() == 2) return "Sports";
        if (player.getPlaces() == 6) return "Sports";
        if (player.getPlaces() == 10) return "Sports";
        return "Rock";
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
