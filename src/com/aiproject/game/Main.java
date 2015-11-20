package com.aiproject.game;

import java.util.Scanner;
/**
 * 
 * @author Bruno Paes
 * @since 2013
 * 
 */
public class Main 
{
	private static Scanner kb;
	
	public static void main(String[] args) 
	{		
		GameAlgorithm ga = new GameAlgorithm();
		kb = new Scanner(System.in);
		System.out.println("Gotcha! Welcome. Please select the player mode \t");
		System.out.println("Against machine[1] | Against player[2]: ");
		String answer = kb.nextLine();
		
		if(answer.equalsIgnoreCase("1")){
			ga.playGameAuto();
		}
		
		if(answer.equalsIgnoreCase("2")){
			ga.playGame();
		}
				
	}
}
