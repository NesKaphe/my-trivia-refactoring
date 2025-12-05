
package com.adaptionsoft.games.trivia.runner;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.entity.QuestionBank;
import com.adaptionsoft.games.uglytrivia.out.GameConsoleOutput;
import com.adaptionsoft.games.uglytrivia.rule.DefaultCategoryRule;
import com.adaptionsoft.games.uglytrivia.rule.DefaultPenaltyBoxRule;


public class GameRunner {

	private static boolean notAWinner;

	public static void main(String[] args) {
		Game aGame = new Game(new GameConsoleOutput(), QuestionBank.createDefaultQuestionBank(), new DefaultCategoryRule(), new DefaultPenaltyBoxRule());
		
		aGame.add("Chet");
		aGame.add("Pat");
		aGame.add("Sue");
		
		Random rand = new Random();
	
		do {
			
			aGame.roll(rand.nextInt(5) + 1);
			
			if (rand.nextInt(9) == 7) {
				notAWinner = aGame.wrongAnswer();
			} else {
				notAWinner = aGame.wasCorrectlyAnswered();
			}
			
			
			
		} while (notAWinner);
		
	}
}
