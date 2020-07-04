package pieces;

import java.util.ArrayList;
import java.util.List;

import chess.GameInstance;
import chess.Values;
import units.Move;
import units.Position;

public class King extends Piece {
	public boolean canCastleQSide = true;
	public boolean canCastleKSide = true;
	
	public King(boolean isWhite) {
		this.isWhite = isWhite;
		baseValue = Values.KING_VALUE;
	}

	@Override
	public List<Move> getMoves(GameInstance inst, Position currPos) {
		// update the position score
		int row = currPos.row;
		int col = currPos.col;
		
		if (isWhite) {
			posScore = Values.whiteKingSquares[row][col];
		} else {
			posScore = Values.blackKingSquares[row][col];
		}
		
		// get the moves
		List<Move> moves = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				
				int newRow = currPos.row + i;
				int newCol = currPos.col + j;
				
				Position newPos = new Position(newRow, newCol);
				Move newMove = new Move(currPos, newPos);
								
				boolean[] result = touchSquare(inst, newMove);
				
				if (result[0]) {
					// add
					moves.add(newMove);
				}
			}
		}

		do {
			// test king-side castle
			if (!canCastleKSide) {
				break;
			}

			// test empty squares between the rook and the king
			Piece firstSquare = inst.board[currPos.row][currPos.col + 1].piece;
			Piece secondSquare = inst.board[currPos.row][currPos.col + 2].piece;

			if ((firstSquare != null) || (secondSquare != null)) {
				break;
			}

			// check if the king is in check
			if (inst.kingInCheck(isWhite)) {
				break;
			}

			// intermediate position shouldn't be attacked
			Move intermediate = new Move(currPos, new Position(currPos.row, currPos.col + 1));
			if (!moveIsLegal(inst, intermediate)) {
				break;
			}

			Move castleKSide = new Move(currPos, new Position(currPos.row, currPos.col + 2));
			// final position shoudln't be attacked
			if (!moveIsLegal(inst, castleKSide)) {
				break;
			}

			// castle king-side
			moves.add(castleKSide);

		} while (false);
		
		do {
			// test queen-side castle
			if (!canCastleQSide) {
				break;
			}

			// test empty squares between the rook and the king
			Piece firstSquare = inst.board[currPos.row][currPos.col - 1].piece;
			Piece secondSquare = inst.board[currPos.row][currPos.col - 2].piece;
			Piece thirdSquare = inst.board[currPos.row][currPos.col - 3].piece;

			if ((firstSquare != null) || (secondSquare != null) || (thirdSquare != null)) {
				break;
			}

			// check if the king is in check
			if (inst.kingInCheck(isWhite)) {
				break;
			}

			// intermediate position shouldn't be attacked
			Move intermediate = new Move(currPos, new Position(currPos.row, currPos.col - 1));
			if (!moveIsLegal(inst, intermediate)) {
				break;
			}

			Move castleQSide = new Move(currPos, new Position(currPos.row, currPos.col - 2));
			// final position shouldn't be attacked
			if (!moveIsLegal(inst, castleQSide)) {
				break;
			}

			// castle queen-side
			moves.add(castleQSide);

		} while (false);

		return moves;
	}
	
	public boolean inCheck(GameInstance inst, Position currPos) {
		int direction = isWhite ? -1 : 1;

		// check for opponent pawns
		for (int i = -1; i < 2; i += 2) {
			int newRow = currPos.row + direction;
			int newCol = currPos.col + i;

			Position newPos = new Position(newRow, newCol);

			if (newPos.isValid() && inst.checkFor(Pawn.class, newPos, !isWhite)) {
				return true;
			}
		}

		// check for opponent knights
		for (int i = -2; i < 3; i++) {
			if (i != 0) {
				int coordinate = 3 - Math.abs(i);
				for (int j = -coordinate; j < coordinate + 1; j += 2 * coordinate) {
					int newRow = currPos.row + i;
					int newCol = currPos.col + j;

					Position newPos = new Position(newRow, newCol);

					if (newPos.isValid() && inst.checkFor(Knight.class, newPos, !isWhite)) {
						return true;
					}
				}
			}
		}

		// check for opponent bishops or queens
		for (int rDir = -1; rDir < 2; rDir += 2) {
			for (int cDir = -1; cDir < 2; cDir += 2) {
				for (int i = 1; i < 8; i++) {
					// for each of the 4 directions
					int newRow = currPos.row + rDir * i;
					int newCol = currPos.col + cDir * i;

					Position newPos = new Position(newRow, newCol);
					
					if (!newPos.isValid()) {
						// nothing to check
						continue;
					}

					boolean bishopCheck = inst.checkFor(Bishop.class, newPos, !isWhite);
					boolean queenCheck = inst.checkFor(Queen.class, newPos, !isWhite);
					
					if (bishopCheck || queenCheck) {
						return true;
					}
					
					if (inst.pieceAt(newPos) != null) {
						// another piece encountered, no reason to continue in this direction
						break;
					}
				}
			}
		}

		// check for opponent rooks or queens
		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				int rDir = (i - j) / 2;
				int cDir = (i + j) / 2;

				for (int k = 1; k < 8; k++) {
					// for each of the 4 directions
					int newRow = currPos.row + rDir * k;
					int newCol = currPos.col + cDir * k;

					Position newPos = new Position(newRow, newCol);
					
					if (!newPos.isValid()) {
						// nothing to check
						continue;
					}

					boolean rookCheck = inst.checkFor(Rook.class, newPos, !isWhite);
					boolean queenCheck = inst.checkFor(Queen.class, newPos, !isWhite);
					
					if (rookCheck || queenCheck) {
						return true;
					}
					
					if (inst.pieceAt(newPos) != null) {
						// another piece encountered, no reason to continue in this direction
						break;
					}
				}
			}
		}
		
		// check for opponent king
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				
				int newRow = currPos.row + i;
				int newCol = currPos.col + j;
				
				Position newPos = new Position(newRow, newCol);
				
				if (newPos.isValid() && inst.checkFor(King.class, newPos, !isWhite)) {
					return true;
				}
			}
		}

		// this king is not in check
		return false;
	}
}
