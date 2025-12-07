package com.adaptionsoft.games.uglytrivia.rule;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

public class DefaultCategoryRule implements CategoryRule {
    @Override
    public Category categoryFor(Player player) {
        return switch (player.getPlaces() % 4) {
            case 0 -> Category.POP;
            case 1 -> Category.SCIENCE;
            case 2 -> Category.SPORTS;
            default -> Category.ROCK;
        };
    }
}
