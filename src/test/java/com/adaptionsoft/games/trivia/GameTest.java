package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.out.GameOutputAdapter;
import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.rule.DefaultCategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.DefaultPenaltyBoxRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private FakeOutput gameOutput;
    private Game aGame;

    @BeforeEach
    void setUp() {
        gameOutput = new FakeOutput();
        aGame = new Game(gameOutput, QuestionBank.createDefaultQuestionBank(), new DefaultCategoryRule(), new DefaultPenaltyBoxRule());
        aGame.add("Chet");
        aGame.add("Pat");
    }

    @Test
    void playersRotationShouldOccurEachTurn() {
        // First player
        aGame.roll(1);

        Optional<Player> chet = gameOutput.getCapturedPlayer("Chet");
        assertTrue(chet.isPresent());
        assertEquals(gameOutput.getCurrentPlayer(), chet.get());

        aGame.wrongAnswer();

        //Second player
        aGame.roll(1);

        Optional<Player> pat = gameOutput.getCapturedPlayer("Pat");
        assertTrue(pat.isPresent());
        assertEquals(gameOutput.getCurrentPlayer(), pat.get());

        aGame.correctAnswer();

        // Back to first player
        aGame.roll(1);
        chet = gameOutput.getCapturedPlayer("Chet");
        assertTrue(chet.isPresent());
        assertEquals(gameOutput.getCurrentPlayer(), chet.get());
    }

    @Test
    void gameShouldNotAwaitAnswerWithoutARollForPlayerOutOfPenaltyBox() {
        assertFalse(aGame.isAwaitingAnswer());
    }

    @Test
    void gameShouldAwaitAnswerForPlayerOutOfPenaltyBox() {
        aGame.roll(2);

        assertTrue(aGame.isAwaitingAnswer());
    }

    @Test
    void gameShouldNotAwaitAnswerForPlayerBlockedInPenaltyBox() {
        aGame.roll(1);
        aGame.wrongAnswer();

        aGame.roll(1);
        aGame.correctAnswer();

        aGame.roll(2);

        assertFalse(aGame.isAwaitingAnswer());
    }

    @Test
    void gameShouldNotAcceptRollWhenPreviousTurnIsNotFinished() {
        aGame.roll(1);

        var ex = assertThrows(IllegalStateException.class, () -> aGame.roll(1));
        assertEquals("Cannot start new turn before resolving previous one", ex.getMessage());
    }

    @Test
    void gameShouldNotAcceptCorrectAnswersWithoutActiveTurn() {
        var ex = assertThrows(IllegalStateException.class, aGame::correctAnswer);

        assertEquals("No active turn", ex.getMessage());
    }

    @Test
    void gameShouldNotAcceptWrongAnswersWithoutActiveTurn() {
        var ex = assertThrows(IllegalStateException.class, aGame::wrongAnswer);

        assertEquals("No active turn", ex.getMessage());
    }

    static class FakeOutput extends GameOutputAdapter {
        private final List<Player> capturedPlayers = new ArrayList<>();
        private Player currentPlayer;

        @Override
        public void printPlayerAdded(Player player, int playerNumber) {
            capturedPlayers.add(player);
        }

        @Override
        public void printPlayerRoll(Player player, int roll) {
            currentPlayer = player;
        }

        public Optional<Player> getCapturedPlayer(String playerName) {
            return capturedPlayers.stream().filter(player -> player.getName().equals(playerName)).findFirst();
        }

        public Player getCurrentPlayer() {
            return currentPlayer;
        }
    }
}
