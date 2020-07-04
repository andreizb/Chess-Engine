package pieces;

import java.util.List;

import chess.GameInstance;
import chess.Values;
import units.Move;
import units.Position;

public class Bishop extends Piece {
	
	public Bishop(boolean isWhite) {
		this.isWhite = isWhite;
		baseValue = Values.BISHOP_VALUE;
		realValue = baseValue;
	}

	@Override
	public List<Move> getMoves(GameInstance inst, Position currPos) {
		// update the position score
		int row = currPos.row;
		int col = currPos.col;
		
		if (isWhite) {
			posScore = Values.whiteBishopSquares[row][col];
		} else {
			posScore = Values.blackBishopSquares[row][col];
		}
		
		// get the moves
		return getDiagMoves(inst, currPos);
	}

}
