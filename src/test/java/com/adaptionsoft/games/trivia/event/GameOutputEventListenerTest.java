package com.adaptionsoft.games.trivia.event;

import com.adaptionsoft.games.trivia.out.GameOutputAdapter;
import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.event.*;
import com.adaptionsoft.games.uglytrivia.event.payload.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameOutputEventListenerTest {

    FakeOutput gameOutput;
    GameOutputEventListener listener;

    @BeforeEach
    void setUp() {
        gameOutput = new FakeOutput();
        listener = new GameOutputEventListener(gameOutput);
    }

    @Test
    void shouldReceivePlayerRolledEvent() {
        var player = new Player("player");
        PlayerRolledPayload e = new PlayerRolledPayload(player, 1);

        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
        assertEquals(e.roll(), gameOutput.getReceivedRoll());
    }

    @Test
    void shouldReceiveRollOutcomeEvent() {
        var player = new Player("player");
        RollOutcomePayload e = new RollOutcomePayload(player, Category.POP, "Une question");

        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
        assertEquals(e.category(), gameOutput.getReceivedCategory());
        assertEquals(e.question(), gameOutput.getReceivedQuestion());
    }

    @Test
    void shouldReceivePlayerAddedEvent() {
        var player = new Player("player");
        PlayerAddedPayload e = new PlayerAddedPayload(player, 1);

        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
        assertEquals(e.playerNumber(), gameOutput.getReceivedPlayerNumber());
    }

    @Test
    void shouldReceiveWrongAnswerEvent() {
        var player = new Player("player");
        WrongAnswerPayload e = new WrongAnswerPayload(player);

        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
    }

    @Test
    void shouldReceiveCorrectAnswerEvent() {
        var player = new Player("player");
        CorrectAnswerPayload e = new CorrectAnswerPayload(player);

        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
    }

    @Test
    void shouldReceivePenaltyBoxReleaseStatusEvent() {
        var player = new Player("player");
        PenaltyBoxReleaseStatusPayload e = new PenaltyBoxReleaseStatusPayload(player, true);

        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
        assertEquals(e.canGetOutOfPenaltyBox(), gameOutput.isReceivedCanGetOutOfPenaltyBox());
    }

    static class FakeOutput extends GameOutputAdapter {
        private Player receivedPlayer;
        private int receivedRoll;
        private Category receivedCategory;
        private String receivedQuestion;
        private int receivedPlayerNumber;
        private boolean receivedCanGetOutOfPenaltyBox;

        @Override
        public void printPlayerRoll(Player player, int roll) {
            receivedPlayer = player;
            receivedRoll = roll;
        }

        @Override
        public void printRollOutcome(Player player, Category category, String question) {
            receivedPlayer = player;
            receivedCategory = category;
            receivedQuestion = question;
        }

        @Override
        public void printPlayerAdded(Player player, int playerNumber) {
            receivedPlayer = player;
            receivedPlayerNumber = playerNumber;
        }

        @Override
        public void printWrongAnswer(Player player) {
            receivedPlayer = player;
        }

        @Override
        public void printCorrectAnswer(Player player) {
            receivedPlayer = player;
        }

        @Override
        public void printPenaltyBoxExitStatus(Player player, boolean canGetOutOfPenaltyBox) {
            receivedPlayer = player;
            receivedCanGetOutOfPenaltyBox = canGetOutOfPenaltyBox;
        }

        public Player getReceivedPlayer() {
            return receivedPlayer;
        }

        public int getReceivedRoll() {
            return receivedRoll;
        }

        public Category getReceivedCategory() {
            return receivedCategory;
        }

        public String getReceivedQuestion() {
            return receivedQuestion;
        }

        public int getReceivedPlayerNumber() {
            return receivedPlayerNumber;
        }

        public boolean isReceivedCanGetOutOfPenaltyBox() {
            return receivedCanGetOutOfPenaltyBox;
        }
    }
}
