package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.event.payload.GameEventPayload;

import java.util.ArrayList;
import java.util.List;

public class GameEventPublisher {

    private final List<GameEventListener> listeners = new ArrayList<>();

    public void subscribe(GameEventListener gameEventListener) {
         listeners.add(gameEventListener);
    }

    public void publish(GameEventPayload gameEvent) {
        listeners.forEach(listener -> listener.receive(gameEvent));
    }
}
