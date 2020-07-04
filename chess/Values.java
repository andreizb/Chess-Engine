package chess;

public abstract class Values {
	// pieces
	public static final double PAWN_VALUE = 1;
	public static final double KNIGHT_VALUE = 6; // values here represent how many pawns it is worth
	public static final double BISHOP_VALUE = 6.5;
	public static final double ROOK_VALUE = 10;
	public static final double QUEEN_VALUE = 20;
//	public static final double PAWN_VALUE = 1;
//	public static final double KNIGHT_VALUE = 3; // values here represent how many pawns it is worth
//	public static final double BISHOP_VALUE = 3.1;
//	public static final double ROOK_VALUE = 4;
//	public static final double QUEEN_VALUE = 9;
	public static final double KING_VALUE = Double.MAX_VALUE;
	
	public static final double ATTACK_BONUS = 0.05;
	public static final double DEFEND_COST = 0.02;
	
	// evaluation factors
	public static final double MATERIAL_FACTOR = 1;
	
	public static final double BISHOP_PAIR_FACTOR = 0.0075;
	public static final double ROOKS_CONNECTED_BONUS = 0.02;
	public static final double ROOK_OPEN_FILE_FACTOR = 0.02;
		
	public static final double PASSED_PAWN_FACTOR = 0.02;
	public static final double DOUBLED_PAWN_PENALTY_FACTOR = -0.012;
	public static final double PAWN_STRUCTURE_FACTOR = 0.02;

	public static final double PST_FACTOR = 1;//0.033;
		
	// Piece-Square Tables
	public static final double[][] whitePawnSquares = {{36, 36, 36, 36, 36, 36, 36, 36},
														{36, 36, 36, 36, 36, 36, 36, 36},
														{14, 14, 20, 24, 24, 20, 16, 16},
														{10, 13, 13, 20, 20, 18, 12, 12},
														{6, 12, 12, 20, 20, 10, 10, 10},
														{12, 12, 10, 12, 12, 10, 55, 50},
														{10, 10, 10, 0, 0, 14, 55, 50},
														{0, 0, 0, 0, 0, 0, 0, 0}};

	public static final double[][] blackPawnSquares = {{0, 0, 0, 0, 0, 0, 0, 0},
														{10, 10, 10, 0, 0, 14, 55, 50},
														{12, 12, 10, 12, 12, 10, 55, 50},
														{6, 12, 12, 20, 20, 10, 10, 10},
														{10, 13, 13, 20, 20, 18, 12, 12},
														{14, 14, 20, 24, 24, 20, 16, 16},
														{36, 36, 36, 36, 36, 36, 36, 36},
														{36, 36, 36, 36, 36, 36, 36, 36}};

	public static final double[][] whiteKnightSquares = {{0, 10, 20, 20, 20, 20, 10, 0},
															{10, 24, 26, 26, 26, 26, 24, 10},
															{10, 28, 40, 50, 50, 40, 28, 10},
															{10, 23, 36, 40, 40, 36, 23, 10},
															{10, 22, 28, 30, 30, 28, 22, 10},
															{0, 26, 26, 30, 30, 26, 26, 0},
															{5, 20, 20, 23, 20, 20, 20, 5},
															{0, 5, 15, 15, 15, 15, 0, 0}};

	public static final double[][] blackKnightSquares = {{0, 5, 15, 15, 15, 15, 0, 0},
															{5, 20, 20, 23, 20, 20, 20, 5},
															{0, 26, 26, 30, 30, 26, 26, 0},
															{10, 22, 28, 30, 30, 28, 22, 10},
															{10, 23, 36, 40, 40, 36, 23, 10},
															{10, 28, 40, 50, 50, 40, 28, 10},
															{10, 24, 26, 26, 26, 26, 24, 10},
															{0, 10, 20, 20, 20, 20, 10, 0}};

	public static final double[][] whiteBishopSquares = {{10, 10, 10, 10, 10, 10, 10, 10},
															{0, 22, 24, 24, 24, 24, 22, 0},
															{42, 48, 50, 52, 52, 50, 48, 42},
															{41, 42, 45, 48, 48, 45, 42, 41},
															{38, 45, 42, 48, 48, 42, 45, 38},
															{35, 45, 45, 42, 42, 45, 45, 45},
															{30, 48, 42, 35, 35, 42, 48, 30},
															{24, 30, 24, 30, 30, 20, 30, 24}};

	public static final double[][] blackBishopSquares = {{24, 30, 24, 30, 30, 20, 30, 24},
															{30, 48, 42, 35, 35, 42, 48, 30},
															{35, 45, 45, 42, 42, 45, 45, 45},
															{38, 45, 42, 48, 48, 42, 45, 38},
															{41, 42, 45, 48, 48, 45, 42, 41},
															{42, 48, 50, 52, 52, 50, 48, 42},
															{0, 22, 24, 24, 24, 24, 22, 0},
															{10, 10, 10, 10, 10, 10, 10, 10}};

	public static final double[][] whiteRookSquares = {{30, 30, 30, 30, 30, 30, 30, 30},
														{50, 50, 50, 50, 50, 50, 50, 50},
														{8, 8, 12, 20, 20, 12, 8, 8},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0}};

	public static final double[][] blackRookSquares = {{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{0, 0, 4, 6, 6, 4, 0, 0},
														{8, 8, 12, 20, 20, 12, 8, 8},
														{50, 50, 50, 50, 50, 50, 50, 50},
														{30, 30, 30, 30, 30, 30, 30, 30}};

	public static final double[][] whiteQueenSquares = {{38, 46, 50, 54, 54, 50, 46, 42},
														{38, 46, 54, 60, 60, 54, 54, 50},
														{34, 42, 46, 60, 60, 50, 50, 46},
														{32, 36, 34, 34, 34, 42, 40, 40},
														{32, 34, 34, 34, 34, 38, 36, 36},
														{32, 34, 34, 38, 38, 36, 34, 34},
														{32, 32, 36, 18, 36, 32, 32, 32},
														{20, 30, 30, 30, 30, 30, 30, 20}};

	public static final double[][] blackQueenSquares = {{20, 30, 30, 30, 30, 30, 30, 20},
														{32, 32, 36, 18, 36, 32, 32, 32},
														{32, 34, 34, 38, 38, 36, 34, 34},
														{32, 34, 34, 34, 34, 38, 36, 36},
														{32, 36, 34, 34, 34, 42, 40, 40},
														{34, 42, 46, 60, 60, 50, 50, 46},
														{38, 46, 54, 60, 60, 54, 54, 50},
														{38, 46, 50, 54, 54, 50, 46, 42}};

	public static final double[][] whiteKingSquares = {{0, 0, 0, 0, 0, 0, 0, 0},
														{10, 10, 10, 10, 10, 10, 10, 10},
														{20, 20, 20, 20, 20, 20, 20, 20},
														{35, 35, 35, 35, 35, 35, 35, 35},
														{50, 50, 50, 50, 50, 50, 50, 50},
														{60, 60, 60, 60, 60, 60, 60, 60},
														{86, 90, 84, 80, 80, 80, 88, 88},
														{88, 92, 90, 80, 90, 80, 90, 88}};

	public static final double[][] blackKingSquares = {{88, 92, 90, 80, 90, 80, 90, 88},
														{86, 90, 84, 80, 80, 80, 88, 88},
														{60, 60, 60, 60, 60, 60, 60, 60},
														{50, 50, 50, 50, 50, 50, 50, 50},
														{35, 35, 35, 35, 35, 35, 35, 35},
														{20, 20, 20, 20, 20, 20, 20, 20},
														{10, 10, 10, 10, 10, 10, 10, 10},
														{0, 0, 0, 0, 0, 0, 0, 0}};
}
