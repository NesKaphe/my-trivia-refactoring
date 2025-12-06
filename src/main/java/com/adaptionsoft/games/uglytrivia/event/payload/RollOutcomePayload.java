package com.adaptionsoft.games.uglytrivia.event.payload;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

public record RollOutcomePayload(Player player, Category category, String question) implements GameEventPayload {
}
