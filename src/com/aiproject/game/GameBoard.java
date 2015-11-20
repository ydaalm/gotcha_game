package com.aiproject.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Methods to: 
 * -manage the game board (print, initialize);
 * -handle and execute user moves (isValidInstruction, getArrayInstruction, 
 * openSlots, move, movePiece, getArrayInstruction, countPieces);
 * -execute a deep copy of the board object (deepCopy);
 * -check the goal state (end of game -isGoalState);
 * -check available moves for each player (getPossibleMoves);
 * 
 * @author Bruno Paes
 * @since 2013
 *
 */
public class GameBoard implements Serializable
{
	
	public enum player { MIN, MAX, NONE }

	public int[][] board;
	public player player_turn;
	public static final int COL = 4;
	public static final int ROW = 4;
	public static int white = -10;
	public static int black = 10;
	public static int nada = 0;
	
	private static int charA = 65; // ascii table value for A in decimal
	
	// Constructor
	public GameBoard() 
	{
		board = new int[COL][ROW];
		for (int i = 0; i < COL; i++) 
			for (int j = 0; j < ROW; j++) 
					board[i][j] = nada;
	}
	
	public GameBoard deepCopy() throws Exception{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(this);
		ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream in = new ObjectInputStream(bis);
		GameBoard copied = (GameBoard) in.readObject();
		return copied;	
	}
	
	// Puts the initial values
	public void initialite()
	{
		this.board[0][0] = black;
		this.board[COL-1][ROW-1] = white;
		this.player_turn = player.MAX; // cuidar com isso!!
	}
	
	// Prints the board
	public void printBoard() 
	{
	
		System.out.print("   ");
		// prints the up part (A,B,C,D)...
		for (int i = 0; i < COL; i++) 
		{
			System.out.print("  ");
			System.out.print((char)(charA+i));
			System.out.print("  ");
		}
		System.out.println();
		// prints the board itself
		for (int i = 0; i < COL; i++) 
		{
			System.out.print(i+1);
			System.out.print(" [");
			for (int j = 0; j < ROW; j++)
			{
				System.out.print(' ');
				int lenght = String.valueOf(board[i][j]).length();
				switch (lenght) 
				{
				case 1:
					System.out.print(' ');
					System.out.print(board[i][j]);
					System.out.print(' ');
					break;
				case 2:
					System.out.print(' ');
					System.out.print(board[i][j]);
					break;
				case 3:
					System.out.print(board[i][j]);
					break;
				default:
					System.out.print(board[i][j]);
					break;
				}	
				System.out.print(' ');
			}
			System.out.print(']');
			System.out.println();	
		}
	}

	// Checks for simple invalid instructions
	public boolean isValidInstruction(String instruction)
	{
		try
		{
			instruction = instruction.toUpperCase();
			instruction = instruction.replace(" ","");
			
			// a instrucao nao condiz com o tamanho
			if (instruction.length() != 3)
			{
				System.out.println("Bad instruction: " + instruction);
				return false;
			}
			
			// resgatando informacoes da string....
			int r = Integer.valueOf(instruction.substring(0, 1)) -1;
			int dir = Integer.valueOf(instruction.substring(2, 3));
			char ch = instruction.charAt(1);
			int c = (int)ch - charA;
			
			// estou fora do tabuleiro?
			if ((r < 0) || (r > ROW)) return false;
			if ((c < 0) || (c > COL)) return false;
			if ((dir < 1) || (dir > 8)) return false;
			
			// estou movendo uma peca minha?
			player player_side; 
			if (board[r][c] > 0) player_side = player.MAX;
			else if (board[r][c] < 0) player_side = player.MIN;
			else player_side = player.NONE;
			
			if (player_side != player_turn)
			{
				System.out.println("Ooops... wrong piece: " + instruction);
				return false;
			}
			
			// tudo ok
			return true;
		}
		catch (Exception e) { }
		//System.out.println("Not a valid instruction [" + instruction  + "]");
		
		return false;
	}

	// Get r, c and dir!
	public int[] getArrayInstruction(String instruction)
	{
		int[] instructions = new int[3];
		
		try
		{
			instruction = instruction.toUpperCase();
			instruction = instruction.replace(" ","");
			
			instructions[0] = Integer.valueOf(instruction.substring(0, 1)) -1;
			instructions[1] = ((int)instruction.charAt(1) - charA);
			instructions[2] = Integer.valueOf(instruction.substring(2, 3));
		}
		catch(Exception e) { }
		
		return instructions;
		
	}
	
	// Checks how many moves can we do for the specified direction
	public int openSlots(int r, int c, int dir)
	{
		int slots = 0; // number of slots available
		player player_side;
		
		if (board[r][c] > 0) player_side = player.MAX;
		else player_side = player.MIN;
		
		// now, let's check the direction specified
		switch (dir) 
		{
			//up
			case 1:
				while (r > 0)
				{
					r--;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//up-right 
			case 2:
				while ((r > 0) && (c < COL-1))
				{
					r--;
					c++;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//right
			case 3:
				// nao estou na ultima coluna, preciso verificar
				while (c < COL-1)
				{
					c++;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//down-right
			case 4:
				while ((r < ROW-1) && (c < COL-1))
				{
					r++;
					c++;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//down
			case 5:
				while (r < ROW-1)
				{
					r++;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//down-left
			case 6:
				while ((r < ROW-1) && (c > 0))
				{
					r++;
					c--;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//left
			case 7:
				while (c > 0)
				{
					c--;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
			//up-left
			case 8:
				while ((r > 0) && (c > 0))
				{
					r--;
					c--;
					if (player_side == player.MAX)
					{
						if (board[r][c] >= 0) slots++;
						else break;
					}
					else
					{
						if (board[r][c] <= 0) slots++;
						else break;
					}
				}
				break;
		}
	
		return slots;
		
	}

	// Moves the piece in the board
	public void movePiece(int r, int c, int dir, int slots)
	{
		if (slots <= 0) return;
		
		switch (dir) 
		{
			//up
			case 1:
				switch (slots) 
				{
					case 1:
						board[r-1][c] = Math.abs(board[r-1][c]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r-1][c] = board[r-1][c] * -1;						
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r-2][c] = Math.abs(board[r-2][c]) + Math.abs(board[r][c]) - 1;
							board[r-1][c] = Math.abs(board[r-1][c]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-2][c] = board[r-2][c] * -1;
								board[r-1][c] = board[r-1][c] * -1;
							}
						}
						else
						{
							board[r-1][c] = Math.abs(board[r-1][c]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r-1][c] = board[r-1][c] * -1;
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r-3][c] = Math.abs(board[r-3][c]) + Math.abs(board[r][c]) - 3;
							board[r-2][c] = Math.abs(board[r-2][c]) + 2;
							board[r-1][c] = Math.abs(board[r-1][c]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-3][c] = board[r-3][c] * -1;
								board[r-2][c] = board[r-2][c] * -1;
								board[r-1][c] = board[r-1][c] * -1;
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r-2][c] = Math.abs(board[r-2][c]) + Math.abs(board[r][c]) - 1;
							board[r-1][c] = Math.abs(board[r-1][c]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-2][c] = board[r-2][c] * -1;
								board[r-1][c] = board[r-1][c] * -1;
							}
						}
						else
						{
							board[r-1][c] = Math.abs(board[r-1][c]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r-1][c] = board[r-1][c] * -1;
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//up-right 
			case 2:
				switch (slots) 
				{
					case 1:
						board[r-1][c+1] = Math.abs(board[r-1][c+1]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r-1][c+1] = board[r-1][c+1] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r-2][c+2] = Math.abs(board[r-2][c+2]) + Math.abs(board[r][c]) - 1;
							board[r-1][c+1] = Math.abs(board[r-1][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-2][c+2] = board[r-2][c+2] * -1;
								board[r-1][c+1] = board[r-1][c+1] * -1;
							}
						}
						else
						{
							board[r-1][c+1] = Math.abs(board[r-1][c+1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r-1][c+1] = board[r-1][c+1] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r-3][c+3] = Math.abs(board[r-3][c+3]) + Math.abs(board[r][c]) - 3;
							board[r-2][c+2] = Math.abs(board[r-2][c+2]) + 2;
							board[r-1][c+1] = Math.abs(board[r-1][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-3][c+3] = board[r-3][c+3] * -1;
								board[r-2][c+2] = board[r-2][c+2] * -1;
								board[r-1][c+1] = board[r-1][c+1] * -1;
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r-2][c+2] = Math.abs(board[r-2][c+2]) + Math.abs(board[r][c]) - 1;
							board[r-1][c+1] = Math.abs(board[r-1][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-2][c+2] = board[r-2][c+2] * -1;
								board[r-1][c+1] = board[r-1][c+1] * -1;
							}
						}
						else
						{
							board[r-1][c+1] = Math.abs(board[r-1][c+1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if(player_turn == player.MIN){
								board[r-1][c+1] = (board[r-1][c+1]) * -1;
							}
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//right
			case 3:
				switch (slots) 
				{
					case 1:
						board[r][c+1] = Math.abs(board[r][c+1]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r][c+1] = board[r][c+1] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r][c+2] = Math.abs(board[r][c+2]) + Math.abs(board[r][c]) - 1;
							board[r][c+1] = Math.abs(board[r][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r][c+2] = board[r][c+2] * -1;
								board[r][c+1] = board[r][c+1] * -1;
							}
						}
						else
						{
							board[r][c+1] = Math.abs(board[r][c+1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r][c+1] = board[r][c+1] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r][c+3] = Math.abs(board[r][c+3]) + Math.abs(board[r][c]) - 3;
							board[r][c+2] = Math.abs(board[r][c+2]) + 2;
							board[r][c+1] = Math.abs(board[r][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r][c+3] = board[r][c+3] * -1;
								board[r][c+2] = board[r][c+2] * -1;
								board[r][c+1] = board[r][c+1] * -1;
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r][c+2] = Math.abs(board[r][c+2]) + Math.abs(board[r][c]) - 1;
							board[r][c+1] = Math.abs(board[r][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r][c+2] = board[r][c+2] * -1;
								board[r][c+1] = board[r][c+1] * -1;
							}
						}
						else
						{
							board[r][c+1] = Math.abs(board[r][c+1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r][c+1] = board[r][c+1] * -1;	
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//down-right
			case 4:
				switch (slots) 
				{
					case 1:
						board[r+1][c+1] = Math.abs(board[r+1][c+1]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r+1][c+1] = board[r+1][c+1] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r+2][c+2] = Math.abs(board[r+2][c+2]) + Math.abs(board[r][c]) - 1;
							board[r+1][c+1] = Math.abs(board[r+1][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r+2][c+2] = board[r+2][c+2] * -1;
								board[r+1][c+1] = board[r+1][c+1] * -1;	
							}
						}
						else
						{
							board[r+1][c+1] = Math.abs(board[r+1][c+1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r+1][c+1] = board[r+1][c+1] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r+3][c+3] = Math.abs(board[r+3][c+3]) + Math.abs(board[r][c]) - 3;
							board[r+2][c+2] = Math.abs(board[r+2][c+2]) + 2;
							board[r+1][c+1] = Math.abs(board[r+1][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r+3][c+3] = board[r+3][c+3] * -1;
								board[r+2][c+2] = board[r+2][c+2] * -1;
								board[r+1][c+1] = board[r+1][c+1] * -1;	
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r+2][c+2] = Math.abs(board[r+2][c+2]) + Math.abs(board[r][c] )- 1;
							board[r+1][c+1] = Math.abs(board[r+1][c+1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r+2][c+2] = board[r+2][c+2] * -1;
								board[r+1][c+1] = board[r+1][c+1] * -1;	
							}
						}
						else
						{
							board[r+1][c+1] = Math.abs(board[r+1][c+1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r+1][c+1] = board[r+1][c+1] * -1;
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//down
			case 5:
				switch (slots) 
				{
					case 1:
						board[r+1][c] = Math.abs(board[r+1][c]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r+1][c] = board[r+1][c] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r+2][c] = Math.abs(board[r+2][c]) + Math.abs(board[r][c]) - 1;
							board[r+1][c] = Math.abs(board[r+1][c]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN) 
							{
								board[r+2][c] = board[r+2][c] * -1;	
								board[r+1][c] = board[r+1][c] * -1;	
							}
						}
						else
						{
							board[r+1][c] = Math.abs(board[r+1][c]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r+1][c] = board[r+1][c] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r+3][c] = Math.abs(board[r+3][c]) + Math.abs(board[r][c]) - 3;
							board[r+2][c] = Math.abs(board[r+2][c]) + 2;
							board[r+1][c] = Math.abs(board[r+1][c]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN) 
							{
								board[r+3][c] = board[r+3][c] * -1;	
								board[r+2][c] = board[r+2][c] * -1;	
								board[r+1][c] = board[r+1][c] * -1;	
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r+2][c] = Math.abs(board[r+2][c]) + Math.abs(board[r][c]) - 1;
							board[r+1][c] = Math.abs(board[r+1][c]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN) 
							{
								board[r+2][c] = board[r+2][c] * -1;	
								board[r+1][c] = board[r+1][c] * -1;	
							}
						}
						else
						{
							board[r+1][c] = Math.abs(board[r+1][c]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r+1][c] = board[r+1][c] * -1;
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//down-left
			case 6:
				switch (slots) 
				{
					case 1:
						board[r+1][c-1] = Math.abs(board[r+1][c-1]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r+1][c-1] = board[r+1][c-1] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r+2][c-2] = Math.abs(board[r+2][c-2]) + Math.abs(board[r][c]) - 1;
							board[r+1][c-1] = Math.abs(board[r+1][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r+2][c-2] = board[r+2][c-2] * -1;	
								board[r+1][c-1] = board[r+1][c-1] * -1;	
							}
						}
						else
						{
							board[r+1][c-1] = Math.abs(board[r+1][c-1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r+1][c-1] = board[r+1][c-1] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r+3][c-3] = Math.abs(board[r+3][c-3]) + Math.abs(board[r][c]) - 3;
							board[r+2][c-2] = Math.abs(board[r+2][c-2]) + 2;
							board[r+1][c-1] = Math.abs(board[r+1][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r+3][c-3] = board[r+3][c-3] * -1;	
								board[r+2][c-2] = board[r+2][c-2] * -1;	
								board[r+1][c-1] = board[r+1][c-1] * -1;	
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r+2][c-2] = Math.abs(board[r+2][c-2]) + Math.abs(board[r][c]) - 1;
							board[r+1][c-1] = Math.abs(board[r+1][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r+2][c-2] = board[r+2][c-2] * -1;	
								board[r+1][c-1] = board[r+1][c-1] * -1;	
							}
						}
						else
						{
							board[r+1][c-1] = Math.abs(board[r+1][c-1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r+1][c-1] = board[r+1][c-1] * -1;	
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//left
			case 7:
				switch (slots) 
				{
					case 1:
						board[r][c-1] = Math.abs(board[r][c-1]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r][c-1] = board[r][c-1] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r][c-2] = Math.abs(board[r][c-2]) + Math.abs(board[r][c]) - 1;
							board[r][c-1] = Math.abs(board[r][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN) 
							{
								board[r][c-2] = board[r][c-2] * -1;	
								board[r][c-1] = board[r][c-1] * -1;	
							}
						}
						else
						{
							board[r][c-1] = Math.abs(board[r][c-1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r][c-1] = board[r][c-1] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r][c-3] = Math.abs(board[r][c-3]) + Math.abs(board[r][c]) - 3;
							board[r][c-2] = Math.abs(board[r][c-2]) + 2;
							board[r][c-1] = Math.abs(board[r][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN) 
							{
								board[r][c-3] = board[r][c-3] * -1;	
								board[r][c-2] = board[r][c-2] * -1;	
								board[r][c-1] = board[r][c-1] * -1;	
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r][c-2] = Math.abs(board[r][c-2]) + Math.abs(board[r][c]) - 1;
							board[r][c-1] = Math.abs(board[r][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN) 
							{
								board[r][c-2] = board[r][c-2] * -1;	
								board[r][c-1] = board[r][c-1] * -1;	
							}
						}
						else
						{
							board[r][c-1] = Math.abs(board[r][c-1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r][c-1] = board[r][c-1] * -1;	
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
			//up-left
			case 8:
				switch (slots) 
				{
					case 1:
						board[r-1][c-1] = Math.abs(board[r-1][c-1]) + Math.abs(board[r][c]);
						board[r][c] = nada;
						if (player_turn == player.MIN) board[r-1][c-1] = board[r-1][c-1] * -1;	
						break;
					case 2:
						if (Math.abs(board[r][c]) > 1)
						{
							board[r-2][c-2] = Math.abs(board[r-2][c-2]) + Math.abs(board[r][c]) - 1;
							board[r-1][c-1] = Math.abs(board[r-1][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-2][c-2] = board[r-2][c-2] * -1;	
								board[r-1][c-1] = board[r-1][c-1] * -1;	
							}
						}
						else
						{
							board[r-1][c-1] = Math.abs(board[r-1][c-1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r-1][c-1] = board[r-1][c-1] * -1;	
						}
						break;
						
					case 3:
						if (Math.abs(board[r][c]) > 3)
						{
							board[r-3][c-3] = Math.abs(board[r-3][c-3]) + Math.abs(board[r][c]) - 3;
							board[r-2][c-2] = Math.abs(board[r-2][c-2]) + 2;
							board[r-1][c-1] = Math.abs(board[r-1][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-3][c-3] = board[r-3][c-3] * -1;	
								board[r-2][c-2] = board[r-2][c-2] * -1;	
								board[r-1][c-1] = board[r-1][c-1] * -1;	
							}
						}
						else if (Math.abs(board[r][c]) > 1)
						{
							board[r-2][c-2] = Math.abs(board[r-2][c-2]) + Math.abs(board[r][c]) - 1;
							board[r-1][c-1] = Math.abs(board[r-1][c-1]) + 1;
							board[r][c] = nada;
							if (player_turn == player.MIN)
							{
								board[r-2][c-2] = board[r-2][c-2] * -1;	
								board[r-1][c-1] = board[r-1][c-1] * -1;	
							}
						}
						else
						{
							board[r-1][c-1] = Math.abs(board[r-1][c-1]) + Math.abs(board[r][c]);
							board[r][c] = nada;
							if (player_turn == player.MIN) board[r-1][c-1] = board[r-1][c-1] * -1;	
						}
						break;
						default:
							System.out.println("Undefined Move");
							break;
				}
				break;
		}
		
	}
	
	// Moves the pieces in the board
	public boolean move(String move)
	{		
		if (isValidInstruction(move))
		{
			int[] inst = getArrayInstruction(move);
			int slots = openSlots(inst[0], inst[1], inst[2]);
			
			if (slots > 0)
			{
				movePiece(inst[0], inst[1], inst[2], slots);
				
				if (player_turn == player.MAX) player_turn = player.MIN;
				else player_turn = player.MAX;
			}

		}
		
		return false;		
	}
	
	public int countPieces(player desired_player)
	{
		
		int counter = 0;
		
		for (int i=0; i< 4; i++)
		{
			for(int j=0; j< 4; j++)
			{
				if (desired_player == player.MAX)
				{
					if (board[i][j] > 0) counter++;
				}
				else
				{
					if (board[i][j] < 0) counter++;
				}
			}
		}
		
		return counter;
		
	}
	
	public boolean isGoalState() 
	{
		
		boolean[] pieces = new boolean[countPieces(player_turn)];
		int position = 0;
		
		if (player_turn== player.MAX)
		{
			for (int i=0; i< 4; i++)
			{
				for(int j=0; j< 4; j++)
				{
					if (board[i][j] > 0)
					{
						if ( (openSlots(i, j, 1))==0 && (openSlots(i, j, 2))==0 &&
							 (openSlots(i, j, 3))==0 && (openSlots(i, j, 4))==0 &&
							 (openSlots(i, j, 5))==0 && (openSlots(i, j, 6))==0 &&
							 (openSlots(i, j, 7))==0 && (openSlots(i, j, 8))==0 )
						{
							pieces[position] = true;
						}
						else pieces[position] = false;
						
						position++;
					}
				}
			}
		}
		if (player_turn==player.MIN)
		{
			for (int i=0; i< 4; i++)
			{
				for(int j=0; j< 4; j++)
				{
					if (board[i][j] < 0)
					{
						if ( (openSlots(i, j, 1))==0 && (openSlots(i, j, 2))==0 &&
							 (openSlots(i, j, 3))==0 && (openSlots(i, j, 4))==0 &&
							 (openSlots(i, j, 5))==0 && (openSlots(i, j, 6))==0 &&
							 (openSlots(i, j, 7))==0 && (openSlots(i, j, 8))==0 )
						{
							pieces[position] = true;
						}
						else pieces[position] = false;
						
						position++;
					}
				}
			}
		}
		
		for (int i=0;i<pieces.length;i++)
		{
			if (pieces[i] == false) return false;
		}
		
		//printBoard();
		
		/*if (player_turn == player.MAX) System.out.println("Win: MAX - Congratulations!");
		else  System.out.println("Win: MIN - Congratulations!");*/
			
		return true;
		
	}
	
	public List<int[]> getPossibleMoves() {
		List<int[]> moves = new ArrayList<int[]>();
		
		if(isGoalState()){
			return moves;
		}
		
		if (player_turn == player.MIN){
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 4; j++){
					if(board[i][j] < 0){	
						for (int dir = 1; dir <= 8; dir++){
							if (openSlots(i, j, dir)!=0){
								int[] pmove = {i, j, dir};
								moves.add(pmove);
							}
							
						}
					}
				}
			}
		}
		
		if (player_turn == player.MAX){
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					if(board[i][j] > 0){
						for(int dir = 1; dir <= 8; dir++){
							if(openSlots(i, j, dir)!=0){
								int[] pmove = {i, j, dir};
								moves.add(pmove);
								}
							}
							
						}
					}
				}
				
			}	
		
		return moves;
	}
	
	
}