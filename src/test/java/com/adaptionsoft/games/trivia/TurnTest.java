package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.out.GameOutputAdapter;
import com.adaptionsoft.games.uglytrivia.Turn;
import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.rule.DefaultCategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.DefaultPenaltyBoxRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TurnTest {

    @Test
    void playerShouldGoToPenaltyBoxOnWrongAnswer() {
        Player chet = new Player("chet");
        Turn turn = createTurnFor(chet, 1);

        turn.start();
        turn.wrongAnswer();

        assertTrue(chet.isInPenaltyBox());
    }

    @Test
    void playerShouldNotGoToPenaltyBoxOnCorrectAnswer() {
        Player chet = new Player("chet");
        Turn turn = createTurnFor(chet, 1);

        turn.start();
        turn.correctAnswer();

        assertFalse(chet.isInPenaltyBox());
    }

    @Test
    void turnShouldAskQuestionForPlayerOutOfPenaltyBox() {
        Player chet = new Player("chet");
        Turn turn = createTurnFor(chet, 1);

        turn.start();

        assertTrue(turn.hasAskedQuestion());
    }

    @Test
    void turnShouldNotAskQuestionForPlayerBlockedInPenaltyBox() {
        Player chet = new Player("chet");
        chet.toPenaltyBox();
        Turn turn = createTurnFor(chet, 2);

        turn.start();

        assertFalse(turn.hasAskedQuestion());
    }

    @Test
    void turnShouldNotAllowAnswerWhenNoQuestionWasAsked() {
        Player chet = new Player("chet");
        Turn turn = createTurnFor(chet, 3);

        var ex = assertThrows(IllegalStateException.class, turn::correctAnswer);
        assertEquals("Not expecting an answer", ex.getMessage());

        var ex2 = assertThrows(IllegalStateException.class, turn::wrongAnswer);
        assertEquals("Not expecting an answer", ex2.getMessage());
    }

    @Test
    void turnShouldNotAllowMultipleAnswers() {
        Player chet = new Player("chet");
        Turn turn = createTurnFor(chet, 3);

        turn.start();

        turn.correctAnswer();

        var ex = assertThrows(IllegalStateException.class, turn::correctAnswer);
        assertEquals("Not expecting an answer", ex.getMessage());

        var ex2 = assertThrows(IllegalStateException.class, turn::wrongAnswer);
        assertEquals("Not expecting an answer", ex2.getMessage());
    }

    @Test
    void turnShouldNotBeStartedTwice() {
        Player player = new Player("Chet");
        Turn turn = createTurnFor(player, 3);

        turn.start();

        var ex = assertThrows(IllegalStateException.class, turn::start);
        assertEquals("Turn already started", ex.getMessage());
    }

    @Test
    void resultShouldNotBeAvailableBeforeAnswerWhenQuestionWasAsked() {
        Player chet = new Player("Chet");
        Turn turn = createTurnFor(chet, 3);

        turn.start(); // question posée, state = WAITING_ANSWER

        var ex = assertThrows(IllegalStateException.class, turn::result);
        assertEquals("Turn not resolved", ex.getMessage());
    }

    @Test
    void resultShouldBeAvailableAfterAnswer() {
        Player chet = new Player("Chet");
        Turn turn = createTurnFor(chet, 3);

        turn.start();
        turn.correctAnswer();

        assertDoesNotThrow(turn::result);
    }

    @Test
    void resultShouldBeAvailableImmediatelyForBlockedTurn() {
        Player chet = new Player("Chet");
        chet.toPenaltyBox();
        Turn turn = createTurnFor(chet, 2); // even => stays in jail

        turn.start();

        assertDoesNotThrow(turn::result);
    }

    private static Turn createTurnFor(Player chet, int roll) {
        return new Turn(chet, roll, QuestionBank.createDefaultQuestionBank(), new DefaultPenaltyBoxRule(), new DefaultCategoryRule(), new GameOutputAdapter());
    }

}
