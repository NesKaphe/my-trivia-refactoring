package com.adaptionsoft.games.trivia.event;

import com.adaptionsoft.games.uglytrivia.entity.Player;
import com.adaptionsoft.games.uglytrivia.event.payload.GameEventPayload;
import com.adaptionsoft.games.uglytrivia.event.GameEventListener;
import com.adaptionsoft.games.uglytrivia.event.GameEventPublisher;
import com.adaptionsoft.games.uglytrivia.event.payload.PlayerRolledPayload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameEventPublisherTest {

    @Test
    void shouldDispatchEventToSubscriber() {
        GameEventPublisher publisher = new GameEventPublisher();

        var listener = new FakeListener();
        publisher.subscribe(listener);

        publisher.publish(new PlayerRolledPayload(new Player("player"), 1));

        assertEquals(1, listener.numberOfCalls());
    }

    @Test
    void shouldDispatchEventToAllSubscribers() {
        GameEventPublisher publisher = new GameEventPublisher();

        var listener1 = new FakeListener();
        var listener2 = new FakeListener();
        publisher.subscribe(listener1);
        publisher.subscribe(listener2);

        publisher.publish(new PlayerRolledPayload(new Player("player"), 1));

        assertEquals(1, listener1.numberOfCalls());
        assertEquals(1, listener2.numberOfCalls());
    }

    static class FakeListener implements GameEventListener {
        int callTimes = 0;
        @Override
        public void receive(GameEventPayload e) {
            callTimes++;
        }

        public int numberOfCalls() {
            return callTimes;
        }
    }
}
