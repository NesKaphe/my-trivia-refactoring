package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    public static final int BOARD_SIZE = 12;

    static class Player {
        private String name;
        private int places;
        private int purses;
        private boolean inPenaltyBox;

        public Player(String name) {
            this.name = name;
            this.places = 0;
            this.purses = 0;
            inPenaltyBox = false;
        }

        public String getName() {
            return name;
        }

        public int getPlaces() {
            return places;
        }

        public int getPurses() {
            return purses;
        }
    }

    ArrayList<String> players = new ArrayList<>();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

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

        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

		playerslist.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + playerslist.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        Player player = playerslist.get(currentPlayer);
        System.out.println(player.getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(player.getName() + " is getting out of the penalty box");
                moveBy(roll);

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

            moveBy(roll);

            System.out.println(player.getName()
                    + "'s new location is "
                    + player.getPlaces());
            System.out.println("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void moveBy(int roll) {
        places[currentPlayer] = places[currentPlayer] + roll;
        if (places[currentPlayer] >= BOARD_SIZE) {
            places[currentPlayer] = places[currentPlayer] - BOARD_SIZE;
        }

        Player player = playerslist.get(currentPlayer);
        player.places += roll;
        if (player.places >= BOARD_SIZE) {
            player.places -= BOARD_SIZE;
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
        if (places[currentPlayer] == 0) return "Pop";
        if (places[currentPlayer] == 4) return "Pop";
        if (places[currentPlayer] == 8) return "Pop";
        if (places[currentPlayer] == 1) return "Science";
        if (places[currentPlayer] == 5) return "Science";
        if (places[currentPlayer] == 9) return "Science";
        if (places[currentPlayer] == 2) return "Sports";
        if (places[currentPlayer] == 6) return "Sports";
        if (places[currentPlayer] == 10) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        Player player = playerslist.get(currentPlayer);
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                addCoin(currentPlayer);
                System.out.println(player.getName()
                        + " now has "
                        + player.getPurses()
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }


        } else {

            System.out.println("Answer was corrent!!!!");
            addCoin(currentPlayer);
            System.out.println(player.getName()
                    + " now has "
                    + player.getPurses()
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        Player player = playerslist.get(currentPlayer);
        System.out.println("Question was incorrectly answered");
        System.out.println(player.getName() + " was sent to the penalty box");
        toPenaltyBox(currentPlayer);

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }

    private void addCoin(int currentPlayer) {
        purses[currentPlayer]++;
        playerslist.get(currentPlayer).purses++;
    }

    private void toPenaltyBox(int currentPlayer) {
        inPenaltyBox[currentPlayer] = true;
        playerslist.get(currentPlayer).inPenaltyBox = true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
