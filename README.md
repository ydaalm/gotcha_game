# gotcha_game
Move tokens in order to limit your opponent's moves.
Game is over when one of the players has no free slots to place tokens.
Moves are represented as in:
1 (up), 2 (up/right), 3 (right), 4 (down/right), 5 (down), 6 (down/left), 7(left), 8(up/left).
 
Players start with a token=10. 
Tokens are placed in up to three available slots in the chosen direction.

Gotcha game can be played in two modes:

1. player 1 versus player 2
2. player vs (automatic) machine

When in automatic (machine player) mode, moves are generated through a [MiniMax](https://en.wikipedia.org/wiki/Minimax) implementation, with a maximum search depth of 5.


Heuristic used:

1. Number of pieces spread on the board by current player minus by the number of pieces spread on the board by opponent, with a weight of 0.7;
2. Number of possible moves left for current player minus by the number of moves left for opponent, with a weight of 0.3;
3. After 30 turns, heuristic 1 weight is changed for 0.3;
4. After 30 turns, heuristic 2 weight is changed for 0.7.

