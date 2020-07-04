package pieces;

import java.util.ArrayList;
import java.util.List;

import chess.GameInstance;
import chess.Values;
import units.Move;
import units.Position;

public class Queen extends Piece {
	
	public Queen(boolean isWhite) {
		this.isWhite = isWhite;
		baseValue = Values.QUEEN_VALUE;
		realValue = baseValue;
	}

	@Override
	public List<Move> getMoves(GameInstance inst, Position currPos) {
		// update the position score
		int row = currPos.row;
		int col = currPos.col;
		
		if (isWhite) {
			posScore = Values.whiteQueenSquares[row][col];
		} else {
			posScore = Values.blackQueenSquares[row][col];
		}
		
		// get the moves
		List<Move> moves = new ArrayList<>();
		
		moves.addAll(getDiagMoves(inst, currPos));
		moves.addAll(getGridMoves(inst, currPos));
		
		return moves;
	}

}
