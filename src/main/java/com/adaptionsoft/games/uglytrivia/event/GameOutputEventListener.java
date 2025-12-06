package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.event.payload.GameEventPayload;
import com.adaptionsoft.games.uglytrivia.event.payload.PlayerRolledPayload;
import com.adaptionsoft.games.uglytrivia.event.payload.RollOutcomePayload;
import com.adaptionsoft.games.uglytrivia.out.GameOutput;

public class GameOutputEventListener implements GameEventListener {

    private final GameOutput gameOutput;

    public GameOutputEventListener(GameOutput gameOutput) {
        this.gameOutput = gameOutput;
    }

    @Override
    public void receive(GameEventPayload e) {
        if(e instanceof PlayerRolledPayload playerRolledPayload) {
            gameOutput.printPlayerRoll(playerRolledPayload.player(), playerRolledPayload.roll());
        } else if (e instanceof  RollOutcomePayload rollOutcomePayload) {
            gameOutput.printRollOutcome(rollOutcomePayload.player(), rollOutcomePayload.category(), rollOutcomePayload.question());
        }
    }
}
