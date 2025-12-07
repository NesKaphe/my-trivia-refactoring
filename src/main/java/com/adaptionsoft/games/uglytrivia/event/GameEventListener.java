package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.event.payload.GameEventPayload;

public interface GameEventListener {

    void receive(GameEventPayload e);
}
