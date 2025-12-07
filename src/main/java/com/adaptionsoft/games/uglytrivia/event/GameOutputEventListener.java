package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.event.payload.*;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;

public class GameOutputEventListener implements GameEventListener {

    private final GameOutput gameOutput;

    public GameOutputEventListener(GameOutput gameOutput) {
        this.gameOutput = gameOutput;
    }

    @Override
    public void receive(GameEventPayload e) {
        switch (e) {
            case PlayerRolledPayload payload            -> gameOutput.printPlayerRoll(payload.player(), payload.roll());
            case RollOutcomePayload payload             -> gameOutput.printRollOutcome(payload.player(), payload.category(), payload.question());
            case PlayerAddedPayload payload             -> gameOutput.printPlayerAdded(payload.player(), payload.playerNumber());
            case WrongAnswerPayload payload             -> gameOutput.printWrongAnswer(payload.player());
            case CorrectAnswerPayload payload           -> gameOutput.printCorrectAnswer(payload.player());
            case PenaltyBoxReleaseStatusPayload payload -> gameOutput.printPenaltyBoxExitStatus(payload.player(), payload.canGetOutOfPenaltyBox());
        }
    }
}
