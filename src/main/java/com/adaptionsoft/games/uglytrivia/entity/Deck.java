package com.adaptionsoft.games.uglytrivia.entity;

import java.util.LinkedList;
import java.util.List;

public class Deck {
    private final List<String> questions;

    public Deck(List<String> questions) {
        this.questions = new LinkedList<>(questions);
    }

    public String pickQuestion() {
        return questions.remove(0);
    }
}
