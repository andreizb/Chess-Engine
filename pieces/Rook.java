package pieces;

import java.util.List;

import chess.GameInstance;
import chess.Values;
import units.Move;
import units.Position;

public class Rook extends Piece {
	
	public Rook(boolean isWhite) {
		this.isWhite = isWhite;
		baseValue = Values.ROOK_VALUE;
		realValue = baseValue;
	}

	@Override
	public List<Move> getMoves(GameInstance inst, Position currPos) {
		// update the position score
		int row = currPos.row;
		int col = currPos.col;
		
		if (isWhite) {
			posScore = Values.whiteRookSquares[row][col];
		} else {
			posScore = Values.blackRookSquares[row][col];
		}
		
		// get the moves
		return getGridMoves(inst, currPos);
	}

}
