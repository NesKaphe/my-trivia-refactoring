package com.adaptionsoft.games.uglytrivia.rule;

public class DefaultPenaltyBoxRule implements PenaltyBoxRule {
    @Override
    public boolean canGetOutOfPenaltyBoxWith(int roll) {
        return roll % 2 != 0;
    }
}
