package com.adaptionsoft.games.uglytrivia.event.payload;

import com.adaptionsoft.games.uglytrivia.entity.Player;

public record WrongAnswerPayload(Player player) implements GameEventPayload {
}
