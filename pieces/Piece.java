package pieces;

import java.util.ArrayList;
import java.util.List;

import chess.GameInstance;
import chess.Values;
import units.Move;
import units.Position;
import units.Square;

public abstract class Piece implements Cloneable {
	public boolean isWhite;
	public double baseValue;
	public double realValue;
	public double posScore;
	
	public void increaseValue(double otherValue) {
		if (this instanceof King) {
			// the king has constant max value
			return;
		}
		
		realValue += otherValue * Values.ATTACK_BONUS;
	}
	
	public void addProtector() {
		if (this instanceof King) {
			// the king has constant max value
			return;
		}
		
		realValue += baseValue * Values.DEFEND_COST;
	}
	
	public void decreaseValue() {
		if (this instanceof King) {
			// the king has constant max value
			return;
		}
		
		realValue -= baseValue * Values.DEFEND_COST;
	}
	
	public abstract List<Move> getMoves(GameInstance inst, Position currPos);
	
	public boolean[] touchSquare(GameInstance inst, Move newMove) {
		// boolean[0] => should add
		// boolean[1] => should stop
		
		Position newPos = newMove.after;

		if (!newPos.isValid()) {
			return new boolean[] {false, false};
		}

		Square targetSquare = inst.board[newPos.row][newPos.col];
		Piece targetPiece = targetSquare.piece;
		
		// the piece controls this square
		targetSquare.addControl(isWhite);

		if (targetPiece == null) {
			return new boolean[] {moveIsLegal(inst, newMove), false};
		}

		if (targetPiece.isWhite != isWhite) {
			// target piece can be captured, this piece is doing good
			targetPiece.increaseValue(targetPiece.baseValue);
			
			return new boolean[] {moveIsLegal(inst, newMove), true};
		}

		// target piece is protected, good for them but a little cost for this piece
		targetPiece.addProtector();
		this.decreaseValue();
		
		return new boolean[] {false, true};
	}
	
	public List<Move> getDiagMoves(GameInstance inst, Position currPos) {
		List<Move> moves = new ArrayList<>();
		
		for (int rowDir = -1; rowDir <= 1; rowDir += 2) {
			for (int colDir = -1; colDir <= 1; colDir += 2) {
				for (int i = 1; i <= 7; i++) {
					// for each of the 4 directions
					int newRow = currPos.row + rowDir*i;
					int newCol = currPos.col + colDir*i;
					
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
		}
		
		return moves;
	}

	public List<Move> getGridMoves(GameInstance inst, Position currPos) {
		List<Move> moves = new ArrayList<>();
		
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				int rowDir = (i - j) / 2;
				int colDir = (i + j) / 2;
				
				for (int k = 1; k <= 7; k++) {
					// for each of the 4 directions
					int newRow = currPos.row + rowDir*k;
					int newCol = currPos.col + colDir*k;
					
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
		}
		
		return moves;
	}

	public boolean moveIsLegal(GameInstance inst, Move newMove) {
		GameInstance check = (GameInstance) inst.clone();
		check.applyMove(newMove);

		// the king shouldn't be in check
		if (check.kingInCheck(isWhite)) {
			return false;
		}

		return true;
	}

	public Object clone() {
		Object cloned = null;
		
		try {
			cloned = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		if (cloned instanceof Pawn) {
			// reset for future modifications
			((Pawn) cloned).pawnProtected = false;
		}
		
		return cloned;
	}

	
}
