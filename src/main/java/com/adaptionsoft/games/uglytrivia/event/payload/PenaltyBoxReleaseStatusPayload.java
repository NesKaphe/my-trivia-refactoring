package com.adaptionsoft.games.uglytrivia.event.payload;

import com.adaptionsoft.games.uglytrivia.entity.Player;

public record PenaltyBoxReleaseStatusPayload(Player player, boolean canGetOutOfPenaltyBox) implements GameEventPayload {
}
