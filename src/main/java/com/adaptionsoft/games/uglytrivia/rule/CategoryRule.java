package com.adaptionsoft.games.uglytrivia.rule;

import com.adaptionsoft.games.uglytrivia.entity.Category;
import com.adaptionsoft.games.uglytrivia.entity.Player;

public interface CategoryRule {
    Category categoryFor(Player player);
}
