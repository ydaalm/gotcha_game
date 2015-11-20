package com.aiproject.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.aiproject.game.GameBoard.player;

/**
 * Implements MiniMax algorithm (MiniMax). 
 * Methods to: 
 * -Score a state according to heuristic evaluation (getHeuristic);
 * -Convert suggested move into string (getMoveString)
 * 
 * @author Hayda Almeida
 * @since 2013
 *
 */
public class newAI {
	
	public GameBoard tempBoard;
	public int[] currentMove = null;
	public int[] bestMove = null;
	int[] move = null;
	int dep;
	int score = 0;
	int bestScore = 0;
	public player pl;
	public State myState;
	public State childState;
	
	public newAI()	{
		
		this.currentMove = null;
		this.bestMove = null;
		this.score = 0;
		this.bestScore = 0;
		this.dep = 0;
		this.move = null;
		this.pl = player.NONE;
		this.myState = null;
		this.tempBoard = null;
	}
	
	
	public int MiniMax(State state, player player_turn, int depth){
		
		State myState = new State(state.stateBoard, new int[]{});
		dep = depth;
		pl = player_turn;
								
					
		List<int[]> possibleMoves = myState.stateBoard.getPossibleMoves();			
						 		
		if(dep<=0 || (possibleMoves.isEmpty())){			
				myState.score = getHeuristic(myState.stateBoard, myState.stateBoard.player_turn);				
				return myState.score;	
		}			
		
		else {
						
			if(pl == player.MIN){			
				bestScore = Integer.MAX_VALUE;
				
				
				for(int i=0; i<possibleMoves.size(); i++){
					move = possibleMoves.get(i);					
					try {
						tempBoard = (myState.getStateBoard()).deepCopy();
					} catch (Exception e) {};						
					tempBoard.movePiece(move[0], move[1], move[2], tempBoard.openSlots(move[0], move[1], move[2]));						
					State childState = new State(tempBoard, move);
					myState.setPossibleStates(childState);						
				}				
				
				
				for(int i=0;  i < myState.childStates.size(); i++){
					myState.childStates.get(i).score = MiniMax((myState.childStates.get(i)), player.MAX, dep-1);					
					bestScore = Math.min(bestScore, myState.childStates.get(i).score);
										
					if(myState.childStates.get(i).score <= bestScore){				
						currentMove = myState.childStates.get(i).move;
					}
				}	
								
			}								
			 
			
			if (pl == player.MAX){ 							
				bestScore = Integer.MIN_VALUE;
				
				
				for(int i = 0; i < possibleMoves.size(); i++){
					move = possibleMoves.get(i);					
					try {
						tempBoard = (myState.getStateBoard()).deepCopy();
					} catch (Exception e) {};						
					tempBoard.movePiece(move[0], move[1], move[2], tempBoard.openSlots(move[0], move[1], move[2]));
					State childState = new State(tempBoard, move);
					myState.setPossibleStates(childState);						
				}				
									
				for(int i=0;  i < myState.childStates.size(); i++){
					
					myState.childStates.get(i).score = MiniMax((myState.childStates.get(i)), player.MIN, dep-1);					
					bestScore = Math.max(bestScore, myState.childStates.get(i).score);
					
					
					if(myState.childStates.get(i).score >= bestScore){
						currentMove = myState.childStates.get(i).move;
					}
				}
			}
			
		} 
	
		bestMove = currentMove;	
		return bestScore;	
	
	}


	private int getHeuristic(GameBoard board, player player_turn) {
		
		int heuristic = 0;
		int count = 0;		
		int slot = 0;
		double weight1 = 0.3;
		double weight2 = 0.7;
				
			if(player_turn == player.MIN){
				if(board.isGoalState()){				
					heuristic = Integer.MIN_VALUE;
				}
				else 			
					for(int i = 0; i < board.board.length; i++){
						for(int j = 0; j < board.board.length; j++){
							if (board.board[i][j]<0){
								//Contando quantidade de pe�as do current player no board
								count = count + 1;
								for(int dir=0; dir<8; dir++){
									//Contando quantidade de slots dispon�veis para current player no board
									slot += board.openSlots(i, j, dir);
								}
								if (GameAlgorithm.countTurn < 25){
									//At� 15� turno, reduzir peso dos slots e aumentar peso de espalhamento das pe�as
									heuristic = (int) (heuristic + (slot*weight1) + (count*weight2));
								}
								else {
									//Depois do 15� turno, reduzir espalhamento e aumentar peso dos slots disponiveis
									heuristic = (int) (heuristic + (slot*weight2) + (count*weight1));	
								}
							}
							else {
									count = count + 1;
									for(int dir=0; dir<8; dir++){
										slot += board.openSlots(i, j, dir);
									}
									if (GameAlgorithm.countTurn < 25){
										heuristic = (int) (heuristic - (slot*weight1) - (count*weight2));
									}
									else {
										heuristic = (int) (heuristic - (slot*weight2) - (count*weight1));	
									}
							}
								
								
						}
					}
				}						
			
			if(player_turn == player.MAX){
				if(board.isGoalState()){					
					heuristic = Integer.MAX_VALUE;
				}
				else 
					for(int i = 0; i < board.board.length; i++){
						for(int j = 0; j < board.board.length; j++){
							if (board.board[i][j]>0){
								count = count + 1;
								for(int dir=0; dir<8; dir++){
									slot += board.openSlots(i, j, dir);
								}
								if (GameAlgorithm.countTurn < 35){
									heuristic = (int) (heuristic + (slot*weight1) + (count*weight2));
								}
								else{
									heuristic = (int) (heuristic + (slot*weight2) + (count*weight1));	
								}
							}
							else {
									count = count + 1;
									for(int dir=0; dir<8; dir++){
										slot += board.openSlots(i, j, dir);
									}
									if (GameAlgorithm.countTurn < 35){
										heuristic = (int) (heuristic - (slot*weight1) - (count*weight2));
									}
									else {
										heuristic = (int) (heuristic - (slot*weight2) - (count*weight1));	
									}
								}
						}
					}			
			}
		return heuristic;
	}
	
	public String getMoveString(int[] instruction){
		String bMove = "";
		int a = instruction[0]+1;
		int b = instruction[1];
		int c = instruction[2];
		
		try {
				switch(b){
					case 0:
						bMove = new StringBuilder().append(a).append("a").append(c).toString();				
						break;
					case 1:
						bMove = new StringBuilder().append(a).append("b").append(c).toString();
						break;			
					case 2:
						bMove = new StringBuilder().append(a).append("c").append(c).toString();
						break;			
					case 3:
						bMove = new StringBuilder().append(a).append("d").append(c).toString();
						break;
					default:
						bMove = new StringBuilder().append(a).append("x").append(c).toString();
						break;
				}
			
		
		}
		catch (Exception e) { }
		
		return bMove;
		
	}

}
