package com.adaptionsoft.games.uglytrivia.entity;

public enum Category {
    
    POP("Pop"), SCIENCE("Science"), SPORTS("Sports"), ROCK("Rock");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
