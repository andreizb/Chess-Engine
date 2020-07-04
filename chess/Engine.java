package chess;

import java.util.ArrayList;
import java.util.List;

import units.Move;

public class Engine {
	public boolean isWhite;
	public boolean isForced;
	public GameInstance currInstance;
	public long initialTime;
	public long time;
	
	public long avgTime = 0;
	public long maxTime = 0;
	public long movesRecorded = 0;
	
	public double worstMove;
	
	public Engine() {
		isWhite = false;
		isForced = false;
		currInstance = new GameInstance().newBoard();
		initialTime = 0;
		time = 0;
	}
	
	public void makeMove() {
		long startTime = System.nanoTime();
		
		// shouldn't play
		if (isForced) {
			return;
		}
		
		// not our turn
		if (currInstance.whiteToMove != isWhite) {
			return;
		}
		
		if (isWhite && currInstance.currentMove == 1) {
			currInstance.applyMove(new Move("e2e4"));
			
			System.out.println("move e2e4");
			return;
		}
		
		int searchDepth = (time >= 6000 ? 4 : 2);
				
		worstMove = Double.MAX_VALUE;
		
		Object[] result = alphaBeta(currInstance, searchDepth, -Double.MAX_VALUE, Double.MAX_VALUE);
				
		Move move = (Move) result[1];
		
		if (move != null) {
			currInstance.applyMove(move);
						
			System.out.println("move " + move.toString());
		}
		
		long endTime = System.nanoTime();
		
		long totalTime = (long) ((endTime - startTime) / Math.pow(10, 7));
		maxTime = Math.max(maxTime, totalTime);
		avgTime = ((avgTime * movesRecorded) + totalTime) / (movesRecorded + 1);
		movesRecorded++;
	}
	
	public void registerMove(Move move) {		
		if (isForced && (currInstance.whiteToMove == isWhite || !currInstance.validateMove(move))) {
			// not opponent's turn or out of board
			return;
		}

		currInstance.applyMove(move);
	}
	
	public void updateTime(long newTime) {
		if (initialTime == 0) {
			initialTime = newTime;
		}
		
		time = newTime;
	}
	
	public void calcDepth() {
		
	}
	
	public Object[] alphaBeta(GameInstance inst, int depth, double alpha, double beta) {		
		// stop condition
		if (depth == 0 || inst.ended()) {
			return new Object[] {inst.eval(), null};
		}
		
		List<Move> moves = inst.getMoves(inst.whiteToMove);
		double max = -Double.MAX_VALUE;
//		Move bestMove = null;
		
		List<Move> equalMoves = new ArrayList<>();
		
		for (Move move : moves) {			
			// new game state
			GameInstance newInst = (GameInstance) inst.clone();
			
			// apply the move
			newInst.applyMove(move);
			
			Object[] result = alphaBeta(newInst, depth - 1, -beta, -alpha);
			
			// keep the best score
			double score = -(double) result[0];
			
			worstMove = Math.min(score, worstMove);
			
			if (score > max) {
				equalMoves.clear();
				equalMoves.add(move);
				
				max = score;
//				bestMove = move;
			} else if (score == max) {
				equalMoves.add(move);
			}
			
			if (max > alpha) {
				alpha = max;
			}
			
			// other beta values don't matter, cut-off
			if (alpha >= beta) {
				break;
			}
		}
		
		
		int listSize = equalMoves.size();
		if (listSize == 0) {
			return new Object[] {alpha, null};
		}
		
		int index = (int) Math.random() % listSize;
		Move randomMove = equalMoves.get(index);
		
		return new Object[] {alpha, randomMove};
	}

}
