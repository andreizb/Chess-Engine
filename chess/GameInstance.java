package chess;

import java.util.ArrayList;
import java.util.List;

import pieces.*;
import units.*;

public class GameInstance implements Cloneable {
	public boolean whiteToMove;
	public Square[][] board = new Square[8][8];
	
	public int currentMove;
	
	public Pawn enPassantPawn = null;
	
	public Position whiteKingPos;
	public Position blackKingPos;
	
	public GameInstance() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new Square();
			}
		}
	}
	
	public GameInstance newBoard() {
		// WHITE PIECES
		addPiece("a1", new Rook(true));
		addPiece("b1", new Knight(true));
		addPiece("c1", new Bishop(true));
		addPiece("d1", new Queen(true));
		addPiece("e1", new King(true));
		addPiece("f1", new Bishop(true));
		addPiece("g1", new Knight(true));
		addPiece("h1", new Rook(true));
		
		// WHITE PAWNS
		addPiece("a2", new Pawn(true));
		addPiece("b2", new Pawn(true));
		addPiece("c2", new Pawn(true));
		addPiece("d2", new Pawn(true));
		addPiece("e2", new Pawn(true));
		addPiece("f2", new Pawn(true));
		addPiece("g2", new Pawn(true));
		addPiece("h2", new Pawn(true));
		
		// BLACK PEICES
		addPiece("a8", new Rook(false));
		addPiece("b8", new Knight(false));
		addPiece("c8", new Bishop(false));
		addPiece("d8", new Queen(false));
		addPiece("e8", new King(false));
		addPiece("f8", new Bishop(false));
		addPiece("g8", new Knight(false));
		addPiece("h8", new Rook(false));
		
		// BLACK PAWNS
		addPiece("a7", new Pawn(false));
		addPiece("b7", new Pawn(false));
		addPiece("c7", new Pawn(false));
		addPiece("d7", new Pawn(false));
		addPiece("e7", new Pawn(false));
		addPiece("f7", new Pawn(false));
		addPiece("g7", new Pawn(false));
		addPiece("h7", new Pawn(false));
		
		whiteKingPos = new Position("e1");
		blackKingPos = new Position("e8");
		
		whiteToMove = true;
		currentMove = 1;
		
		// print(); //////////////////////////////////////////////////////////////
		
		return this; // method chaining
	}
	
	public void print() {
		// ONLY FOR DEBUGGING!
		
		MainClass.log.println("BOARD:");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board[i][j].piece;
				
				if (piece == null) {
					MainClass.log.print("[] ");
					continue;
				}
					
				MainClass.log.print(piece.isWhite ? "W" : "B");
				
				if (piece instanceof Pawn) {
					MainClass.log.print("P ");
				} else if (piece instanceof Rook) {
					MainClass.log.print("R ");
				} else if (piece instanceof Knight) {
					MainClass.log.print("N ");
				} else if (piece instanceof Bishop) {
					MainClass.log.print("B ");
				} else if (piece instanceof Queen) {
					MainClass.log.print("Q ");
				} else if (piece instanceof King) {
					MainClass.log.print("K ");
				}
			}
			
			MainClass.log.println();
		}
		MainClass.log.println();
	}
	
	public double relChange(double a, double b) {
		if (a == 0 && b == 0) return 0;
		
		return (a - b) / (a + b);
	}
	
	public double getMaterialValue(Piece piece) {
		if (piece instanceof King) return 0;
		
		return piece.baseValue;
	}
	
	public double getPSTValue(Piece piece) {
		return piece.posScore;
	}
	
	public double eval() {
		List<Move> ownMoves = getMoves(whiteToMove);
		List<Move> oppMoves = getMoves(!whiteToMove);
		
		if (kingInCheck(whiteToMove) && ownMoves.size() == 0) {
			// checkmate, from our perspective this is BAD
			return -Double.MAX_VALUE;
		}
		
		if (kingInCheck(!whiteToMove) && oppMoves.size() == 0) {
			// checkmate for them, from our perspective this is GOOD
			return Double.MAX_VALUE;
		}
		
		// actual evaluation
		double ownMaterial = 0;
		double oppMaterial = 0;
		
		double ownPST = 0;
		double oppPST = 0;
		
		double ownPawnStructure = 0;
		double oppPawnStructure = 0;
		
		// how many pawns are on file 'j'
		int[] ownPawnFiles = new int[10]; // 8 columns + 2 abstract columns
		int[] oppPawnFiles = new int[10];
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Square square = board[i][j];
				
				Piece piece = square.piece;
				
				if (piece != null) {
					boolean toMove = piece.isWhite == whiteToMove;
					
					// player specific
					if (toMove) {
						ownMaterial += getMaterialValue(piece);
						ownPST += getPSTValue(piece);
						
						if (piece instanceof Pawn) {
							ownPawnFiles[j + 1]++;
							
							if (((Pawn) piece).pawnProtected) {
								ownPawnStructure++;
							}
						}
					} else {
						oppMaterial += getMaterialValue(piece);
						oppPST += getPSTValue(piece);
						
						if (piece instanceof Pawn) {
							oppPawnFiles[j + 1]++;
							
							if (((Pawn) piece).pawnProtected) {
								oppPawnStructure++;
							}
						}
					}
				}
			}
		}
		
		// computing the passed pawns and doubled pawns
		int ownPassedPawns = 0;
		int oppPassedPawns = 0;
		
		int ownDoubledPawns = 0;
		int oppDoubledPawns = 0;
		
		for (int i = 1; i <= 8; i++) {
			boolean own = ownPawnFiles[i] != 0;
			boolean opp = oppPawnFiles[i] != 0;
			boolean ownLeft = ownPawnFiles[i - 1] != 0;
			boolean ownRight = ownPawnFiles[i + 1] != 0;
			boolean oppLeft = oppPawnFiles[i - 1] != 0;
			boolean oppRight = oppPawnFiles[i + 1] != 0;
			
			if (own && !opp && !oppLeft && !oppRight) {
				ownPassedPawns++;
			}
			
			if (opp && !own && !ownLeft && !ownRight) {
				oppPassedPawns++;
			}

			if (ownPawnFiles[i] >= 2) {
				ownDoubledPawns += ownPawnFiles[i] - 1;
			}

			if (oppPawnFiles[i] >= 2) {
				oppDoubledPawns += oppPawnFiles[i] - 1;
			}
		}
		
		double result = relChange(ownMaterial, oppMaterial) 			* Values.MATERIAL_FACTOR +
						relChange(ownPST, oppPST) 						* Values.PST_FACTOR +
						relChange(ownPassedPawns, oppPassedPawns)		* Values.PASSED_PAWN_FACTOR +
						relChange(ownPawnStructure, oppPawnStructure)	* Values.PAWN_STRUCTURE_FACTOR +
						relChange(ownDoubledPawns, oppDoubledPawns)		* Values.DOUBLED_PAWN_PENALTY_FACTOR;
				
		return result;
	}
	
	public boolean ended() {
		List<Move> ownMoves = getMoves(whiteToMove);
		List<Move> oppMoves = getMoves(!whiteToMove);
		
		if ((kingInCheck(whiteToMove) && ownMoves.size() == 0) ||
			(kingInCheck(!whiteToMove) && oppMoves.size() == 0)) {
			return true;
		}
		
		return false;
	}

	public List<Move> getMoves(boolean isWhite) {
		List<Move> moves = new ArrayList<>();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board[i][j].piece;
				
				if (piece != null && piece.isWhite == isWhite) {
					moves.addAll(piece.getMoves(this, new Position(i, j)));
				}
			}
		}
		
		return moves;
	}
	
	public void applyMove(Move m) {
		Position before = m.before;
		Position after = m.after;

		Piece p = pieceAt(before);
		board[before.row][before.col].piece = null;
		
		

		// this move is a promotion
		if (m instanceof Promotion) {
			Promotion pr = (Promotion) m;
			board[after.row][after.col].piece = promotePawn(pr.piece, p.isWhite);
			
			whiteToMove = !whiteToMove;
			currentMove++;

			return;
		}

		if (p instanceof Pawn) {
			board[after.row][after.col].piece = p;
			Pawn pw = (Pawn) p;
			pw.hasMoved = true;
			
			for (int i = -1; i <= 1; i += 2) {
				Position adjPos = new Position(before.row, before.col + i);
				
				if (adjPos.isValid() && checkFor(Pawn.class, adjPos, !p.isWhite)) {
					Pawn adjPawn = (Pawn) pieceAt(adjPos);
					
					if (adjPawn == enPassantPawn) {
						// adjPawn was captured by en passant rule
						
						enPassantPawn = null;
						board[adjPos.row][adjPos.col].piece = null; 
					}
				}
			}

			if (Math.abs(after.row - before.row) == 2) {
				enPassantPawn = pw;
			}
		}

		if (p instanceof Rook) {
			// white king-side castle is no longer possible
			if (before.equals(new Position("h1"))) {
				King king = getKing(true);
				king.canCastleKSide = false;
			}

			// white queen-side castle is no longer possible
			if (before.equals(new Position("a1"))) {
				King king = getKing(true);
				king.canCastleQSide = false;
			}

			// black king-side castle is no longer possible
			if (before.equals(new Position("h8"))) {
				King king = getKing(false);
				king.canCastleKSide = false;
			}

			// black queen-side castle is no longer possible
			if (before.equals(new Position("a8"))) {
				King king = getKing(false);
				king.canCastleQSide = false;
			}
		}

		if (p instanceof King) {
			King king = (King) p;

			// king-side castle, we move the rook as well
			if (after.col - before.col == 2 && king.canCastleKSide) {
				board[before.row][before.col + 1].piece = board[before.row][before.col + 3].piece;
				board[before.row][before.col + 3].piece = null;
			}

			// queen-side castle, we move the rook as well
			if (after.col - before.col == -2 && king.canCastleQSide) {
				board[before.row][before.col - 1].piece = board[before.row][before.col - 4].piece;
				board[before.row][before.col - 4].piece = null;
			}

			// we can't castle anymore after we moved the king
			king.canCastleKSide = false;
			king.canCastleQSide = false;

			if (king.isWhite) {
				whiteKingPos = after;
			} else {
				blackKingPos = after;
			}
		}
		
		if (!(p instanceof Pawn)) {
			// reset enPassantPawn (move generation already considered it)
			enPassantPawn = null;
		}

		board[after.row][after.col].piece = p;
		whiteToMove = !whiteToMove;
		
		currentMove++;
	}

	/**
	  * Promotion of a pawn.
	  * Our engine will only promote to queen or knight (situational), but
	  * promotion to rook or bishop are also valid, so we have to handle
	  * them if the opponent decides to do so.
	  */
	public Piece promotePawn(char p, boolean isWhite) {
		switch (p) {
			case ('q'):
				return new Queen(isWhite);
			case ('n'):
				return new Knight(isWhite);
			case ('r'):
				return new Rook(isWhite);
			case ('b'):
				return new Bishop(isWhite);
			default:
				return null;
		}
	}

	public boolean validateMove(Move move) {
		Position before = move.before;
		
		Piece piece = pieceAt(before);
		
		if (piece == null || piece.isWhite != whiteToMove) {
			// illegal: trying to move a piece that's not theirs
			return false;
		}
		
		// use our move generation algorithm
		List<Move> moves = piece.getMoves(this, move.before);
		
		boolean isLegal = false;
		
		// test if the intended move is among the generated moves
		for (Move testMove : moves) {
			if (testMove.equals(move)) {
				isLegal = true;
				break;
			}
		}
		
		return isLegal;
	}

	public boolean checkFor(Class<?> className, Position pos, boolean isWhite) {
		Piece targetPiece = pieceAt(pos);

		// searching for a PIECE of the SAME COLOUR and the SAME TYPE
		return (targetPiece != null) && (targetPiece.isWhite == isWhite) &&
			className.isInstance(targetPiece);
	}

	public King getKing(boolean isWhite) {
		if (isWhite) {
			return (King)(pieceAt(whiteKingPos));
		}

		return (King)(pieceAt(blackKingPos));
	}

	public boolean kingInCheck(boolean isWhite) {
		Position kingPos = isWhite ? whiteKingPos : blackKingPos;
		King king = (King)(pieceAt(kingPos));

		return king.inCheck(this, kingPos);
	}

	public Object clone() {
		GameInstance cloned = new GameInstance();
		
		cloned.currentMove = currentMove;
		
		cloned.whiteToMove = whiteToMove;
		cloned.whiteKingPos = (Position) whiteKingPos.clone();
		cloned.blackKingPos = (Position) blackKingPos.clone();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece originPiece = board[i][j].piece;
				
				if (originPiece != null) {
					cloned.board[i][j].piece = (Piece) originPiece.clone();
					
					Piece clonedPiece = cloned.board[i][j].piece;
					
					if (originPiece instanceof Pawn && (Pawn) originPiece == enPassantPawn) {
						cloned.enPassantPawn = (Pawn) clonedPiece;
					}
				}
			}
		}
		
		return cloned;
	}
	
	public Piece pieceAt(Position pos) {
		if (pos.isValid()) {
			return board[pos.row][pos.col].piece;
		}
		
		return null;
	}

	public void addPiece(String position, Piece piece) {
		Position pos = new Position(position);
		
		board[pos.row][pos.col].piece = piece; 
	}

}
