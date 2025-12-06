package com.adaptionsoft.games.trivia.event;

import com.adaptionsoft.games.trivia.out.GameOutputAdapter;
import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.event.GameOutputEventListener;
import com.adaptionsoft.games.uglytrivia.event.payload.RollOutcomePayload;
import com.adaptionsoft.games.uglytrivia.event.payload.PlayerRolledPayload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameOutputEventListenerTest {

    @Test
    void shouldReceivePlayerRolledEvent() {
        var chet = new Player("player");
        FakeOutput gameOutput = new FakeOutput();
        var listener = new GameOutputEventListener(gameOutput);

        PlayerRolledPayload e = new PlayerRolledPayload(chet, 1);
        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
        assertEquals(e.roll(), gameOutput.getReceivedRoll());
    }

    @Test
    void shouldReceiveRollOutcomeEvent() {
        var chet = new Player("player");
        FakeOutput gameOutput = new FakeOutput();
        var listener = new GameOutputEventListener(gameOutput);

        RollOutcomePayload e = new RollOutcomePayload(chet, Category.POP, "Une question");
        listener.receive(e);

        assertEquals(e.player(), gameOutput.getReceivedPlayer());
        assertEquals(e.category(), gameOutput.getReceivedCategory());
        assertEquals(e.question(), gameOutput.getReceivedQuestion());
    }

    static class FakeOutput extends GameOutputAdapter {
        private Player receivedPlayer;
        private int receivedRoll;
        private Category receivedCategory;
        private String receivedQuestion;

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
    }
}
