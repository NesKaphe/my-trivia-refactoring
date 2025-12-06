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
    void playerShouldNotGetOutOfPenaltyBoxOnPairRollWhenAlreadyInPenaltyBox() {
        aGame.roll(1);
        aGame.wrongAnswer();

        aGame.roll(1);
        aGame.correctAnswer();

        aGame.roll(2);

        Optional<Player> chet = gameOutput.getCapturedPlayer("Chet");
        assertTrue(chet.isPresent());
        assertTrue(chet.get().isInPenaltyBox());
    }

    @Test
    void playerInPenaltyBoxShouldNotGetToAnswerCorrectlyToQuestion() {
        aGame.roll(1);
        aGame.wrongAnswer();

        aGame.roll(1);
        aGame.correctAnswer();

        aGame.roll(2);

        assertFalse(gameOutput.isCurrentTurnQuestionAsked());

        aGame.correctAnswer();
        assertEquals(0, gameOutput.getCurrentTurnCorrectAnswers());
    }

    @Test
    void playerInPenaltyBoxShouldNotGetToAnswerIncorrectlyToQuestion() {
        aGame.roll(1);
        aGame.wrongAnswer();

        aGame.roll(1);
        aGame.correctAnswer();

        aGame.roll(2);
        assertFalse(gameOutput.isCurrentTurnQuestionAsked());

        aGame.wrongAnswer();
        assertEquals(0, gameOutput.getCurrentTurnWrongAnswers());
    }

    @Test
    void playerInPenaltyBoxShouldGetOutOfPenaltyBoxOnOddRoll() {
        aGame.roll(1);
        aGame.wrongAnswer();

        aGame.roll(1);
        aGame.correctAnswer();

        aGame.roll(3);

        Optional<Player> chet = gameOutput.getCapturedPlayer("Chet");
        assertTrue(chet.isPresent());
        assertFalse(chet.get().isInPenaltyBox());
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

    static class FakeOutput extends GameOutputAdapter {
        private final List<Player> capturedPlayers = new ArrayList<>();
        private Player currentPlayer;

        private boolean currentTurnQuestionAsked = false;
        private int currentTurnCorrectAnswers = 0;
        private int currentTurnWrongAnswers = 0;

        @Override
        public void printPlayerAdded(Player player, int playerNumber) {
            capturedPlayers.add(player);
        }

        @Override
        public void printPlayerRoll(Player player, int roll) {
            currentPlayer = player;
            currentTurnCorrectAnswers = 0;
            currentTurnWrongAnswers = 0;
            currentTurnQuestionAsked = false;
        }

        @Override
        public void printRollOutcome(Player player, Category category, String question) {
            currentTurnQuestionAsked = true;
        }

        @Override
        public void printWrongAnswer(Player player) {
            currentTurnWrongAnswers++;
        }

        @Override
        public void printCorrectAnswer(Player player) {
            currentTurnCorrectAnswers++;
        }

        public Optional<Player> getCapturedPlayer(String playerName) {
            return capturedPlayers.stream().filter(player -> player.getName().equals(playerName)).findFirst();
        }

        public Player getCurrentPlayer() {
            return currentPlayer;
        }

        public int getCurrentTurnCorrectAnswers() {
            return currentTurnCorrectAnswers;
        }

        public int getCurrentTurnWrongAnswers() {
            return currentTurnWrongAnswers;
        }

        public boolean isCurrentTurnQuestionAsked() {
            return currentTurnQuestionAsked;
        }
    }
}
