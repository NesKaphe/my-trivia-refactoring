package com.adaptionsoft.games.uglytrivia.event.payload;

import com.adaptionsoft.games.uglytrivia.entity.Player;

public record PlayerRolledPayload(Player player, int roll) implements GameEventPayload {
}
