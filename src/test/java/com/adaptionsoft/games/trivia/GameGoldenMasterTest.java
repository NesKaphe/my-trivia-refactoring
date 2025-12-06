package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.out.GameConsoleOutput;
import com.adaptionsoft.games.uglytrivia.rule.DefaultCategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.DefaultPenaltyBoxRule;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameGoldenMasterTest {

    @Test
    void shouldReproduceSameOutputWithGoldenMaster() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        List<Integer> seeds = List.of(12345, 54321, 98765, 56789);

        seeds.forEach(seed -> {
            System.out.println("---Seed: " + seed + "---");
            Random rand = new Random(seed);

            Game aGame = new Game(new GameConsoleOutput(), QuestionBank.createDefaultQuestionBank(), new DefaultCategoryRule(), new DefaultPenaltyBoxRule());
            aGame.add("Chet");
            aGame.add("Pat");
            aGame.add("Sue");

            boolean notAWinner;
            do {

                aGame.roll(rand.nextInt(5) + 1);
                notAWinner = rand.nextInt(9) == 7
                        ? aGame.wrongAnswer()
                        : aGame.correctAnswer();
            } while (notAWinner);
        });

        String actualOutput = out.toString();
        assertEquals(readGoldenFile(), actualOutput);
    }

    private String readGoldenFile() throws Exception {
        URI fileUri = Objects.requireNonNull(getClass().getResource("/data/goldenMaster_v2")).toURI();
        Path filePath = Paths.get(fileUri);
        return String.join("\r\n", Files.readAllLines(filePath)) + "\r\n";
    }
}
