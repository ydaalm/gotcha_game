package com.aiproject.game;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.aiproject.game.GameBoard.player;

/**
 * 
 * @author Bruno Paes, Hayda Almeida
 * @since 2013
 *
 */
public class GameAlgorithm 
{
	private GameBoard gb;
	private Scanner keyboard;
	private newAI move;
	private State current;
	public static int countTurn = 1;
	
	
	public GameAlgorithm()
	{
		keyboard = new Scanner(System.in);
		gb = new GameBoard();
		gb.initialite();	
	}
	
	private void playTurn()
	{
		gb.printBoard();
		System.out.print("Turn " + countTurn + " | " + gb.player_turn + " turn:");
		String s = keyboard.nextLine();      
		gb.move(s);
		countTurn++;
	}
	
	public void playGame()
	{
		do playTurn();
		while(!gb.isGoalState());
	}
	
	private void playTurnAuto() 
	{
		move = new newAI();		
		String s = "";
		gb.printBoard();
		System.out.print("Turn " + countTurn + " | " + gb.player_turn + " turn: ");
		
		if(gb.player_turn == player.MAX){
			
			current = new State(gb, new int[]{});
			move.MiniMax(current, player.MAX, 5);
			System.out.print(move.getMoveString(move.bestMove));
			s = keyboard.nextLine();
		}
		
		if(gb.player_turn == player.MIN){			
				s = keyboard.nextLine();		
		}	
			
		gb.move(s);
		countTurn++;	
	}
	
	public void playGameAuto() 
	{		
		do playTurnAuto();
		while(!gb.isGoalState());
		gb.printBoard();
		if(gb.player_turn == player.MAX)
			System.out.print("*.*.*.* Win MIN *.*.*.*! Congratulations :)");
		else 
			System.out.print("*.*.*.* Win MAX *.*.*.*! Congratulations :)");
	}
	
	

		

} 
