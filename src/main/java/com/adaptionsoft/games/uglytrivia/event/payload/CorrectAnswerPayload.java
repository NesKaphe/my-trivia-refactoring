package com.adaptionsoft.games.uglytrivia.event.payload;

import com.adaptionsoft.games.uglytrivia.entity.Player;

public record CorrectAnswerPayload(Player player) implements GameEventPayload {
}
