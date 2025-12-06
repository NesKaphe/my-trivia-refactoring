package com.adaptionsoft.games.uglytrivia.event.payload;

import com.adaptionsoft.games.uglytrivia.entity.Player;

public record PlayerAddedPayload(Player player, int playerNumber) implements GameEventPayload {
}
