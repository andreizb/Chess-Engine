package pieces;

import java.util.ArrayList;
import java.util.List;

import chess.*;
import units.*;

public class Pawn extends Piece {
	public boolean hasMoved = false;
	public boolean pawnProtected = false;
	
	public Pawn(boolean isWhite) {
		this.isWhite = isWhite;
		baseValue = Values.PAWN_VALUE;
		realValue = baseValue;
	}

	@Override
	public List<Move> getMoves(GameInstance inst, Position currPos) {
		// update the position score
		int row = currPos.row;
		int col = currPos.col;
		
		if (isWhite) {
			posScore = Values.whitePawnSquares[row][col];
		} else {
			posScore = Values.blackPawnSquares[row][col];
		}
		
		// get the moves
		List<Move> moves = new ArrayList<>();
		
		int direction = isWhite ? -1 : 1;
		int maxSquares = hasMoved ? 1 : 2;
		
		for (int i = 1; i <= maxSquares; i++) {
			// one square and/or two squares forward
			int newRow = currPos.row + i*direction;
			
			Position newPos = new Position(newRow, currPos.col);
			
			if (!newPos.isValid()) continue; // out of bounds
			
			Move newMove = new Move(currPos, newPos);
			
			Piece targetPiece = inst.board[newRow][currPos.col].piece;
			
			if (newPos.isValid() && targetPiece == null && moveIsLegal(inst, newMove)) {
				// free square
				if (newRow == 0 || newRow == 7) {
					// promotion available
					moves.add(new Promotion(currPos, newPos, 'q'));
					moves.add(new Promotion(currPos, newPos, 'n'));
				} else {
					// normal move
					moves.add(newMove);
				}
			} else {
				break;
			}
		}
		
		for (int i = -1; i <= 1; i += 2) {
			// attack squares for pawn
			int newRow = currPos.row + direction;
			int newCol = currPos.col + i;
			
			Position newPos = new Position(newRow, newCol);
			
			if (!newPos.isValid()) continue; // out of bounds
			
			Move newMove = new Move(currPos, newPos);
			
			Piece targetPiece = inst.board[newRow][newCol].piece;
			Piece adjPiece = inst.board[currPos.row][newCol].piece;
			
			if (!newPos.isValid()) {
				// nothing to check
				continue;
			}
			
			boolean captureAvbl = targetPiece != null && targetPiece.isWhite == !isWhite;
			boolean enPassantAvbl = targetPiece == null &&
									adjPiece instanceof Pawn &&
									((Pawn) (adjPiece)) == inst.enPassantPawn;
			
			if (newPos.isValid() && (captureAvbl || enPassantAvbl) && moveIsLegal(inst, newMove)) {
				// piece available for capture (en passant supported)
				if (newRow == 0 || newRow == 7) {
					// promotion available
					moves.add(new Promotion(currPos, newPos, 'q'));
					moves.add(new Promotion(currPos, newPos, 'n'));
				} else {
					// normal move
					moves.add(newMove);
				}
				
				
				if (targetPiece instanceof Pawn && !captureAvbl) {
					// a pawn of our own, nice pawn structure
					((Pawn) targetPiece).pawnProtected = true;
				}
			}
		}
		
		return moves;
	}

}
