package com.aiproject.game;

import java.util.ArrayList;
import java.util.List;

/* LUGER, George. AI Algorithms, 
 * Data Structures, and Idioms
 * in Prolog, Lisp and Java
 * 2009, pg.294 */
/**
 * Represents current state of game and holds possible 
 * next states from the current state. 
 * Methods to: 
 * -populate list of childStates (setPossibleStates); 
 * -get and set current state board
 * 
 * @since 2013
 *
 */
public class State {
	
	public GameBoard stateBoard;
	public int[] move;
	private State parent = null;
	private State child;
	public int score;
	public List<State> childStates = new ArrayList<State>();

	public State(GameBoard board, int[] move){
		this.stateBoard = board;
		this.move = move;
		this.parent = this;
	}	
	
	public void setPossibleStates(State current){
		childStates.add(current);
	}	
	
	public void setStateBoard(GameBoard board) {
		this.stateBoard = board;
	}
	
	public GameBoard getStateBoard() {
		return this.stateBoard;
	}


}
