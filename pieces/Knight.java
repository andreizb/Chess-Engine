package pieces;

import java.util.ArrayList;
import java.util.List;

import chess.GameInstance;
import chess.Values;
import units.Move;
import units.Position;

public class Knight extends Piece {
	
	public Knight(boolean isWhite) {
		this.isWhite = isWhite;
		baseValue = Values.KNIGHT_VALUE;
		realValue = baseValue;
	}

	@Override
	public List<Move> getMoves(GameInstance inst, Position currPos) {
		// update the position score
		int row = currPos.row;
		int col = currPos.col;
		
		if (isWhite) {
			posScore = Values.whiteKnightSquares[row][col];
		} else {
			posScore = Values.blackKnightSquares[row][col];
		}
		
		// get the moves
		List<Move> moves = new ArrayList<>();
		
		for (int i = -2; i <= 2; i++) {
			// 4 files
			if (i == 0) continue;
			
			int otherCoord = 3 - Math.abs(i);
			for (int j = -otherCoord; j <= otherCoord; j += 2*otherCoord) {
				// 2 rows for each file -> total = 8 moves (correct)
				int newRow = currPos.row + i;
				int newCol = currPos.col + j;
				
				Position newPos = new Position(newRow, newCol);
				Move newMove = new Move(currPos, newPos);
				
				boolean[] result = touchSquare(inst, newMove);
				
				if (result[0]) {
					// add if legal
					moves.add(newMove);
				}
				
				if (result[1]) {
					// piece or board margin reached, stop
					break;
				}
			}
		}	
		
		return moves;
	}
	
}