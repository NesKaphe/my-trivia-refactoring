package com.adaptionsoft.games.uglytrivia.entity;

import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

public class QuestionBank {
    private final EnumMap<Category, Deck> questionsDecks;

    public QuestionBank(Map<Category, Deck> initialDecks) {
        questionsDecks = new EnumMap<>(Category.class);
        questionsDecks.putAll(initialDecks);
    }

    public static QuestionBank createDefaultQuestionBank() {
        final EnumMap<Category, Deck> questionsDecks;
        questionsDecks = new EnumMap<>(Category.class);

        for (var category : Category.values()) {
            Deque<String> deck = new LinkedList<>();
            for (int i = 0; i < 50; i++) {
                deck.addLast(category + " Question " + i);
            }
            questionsDecks.put(category, new Deck(deck.stream().toList()));
        }
        return new QuestionBank(questionsDecks);
    }

    public String drawQuestionFor(Category category) {
        return questionsDecks.get(category).pickQuestion();
    }
}
