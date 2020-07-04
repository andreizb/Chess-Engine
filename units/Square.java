package units;

import pieces.Piece;

public class Square implements Cloneable {
	public Piece piece = null;
	
	public int whiteControl = 0;
	public int blackControl = 0;
	
	public Square() {
		
	}
	
	public void addControl(boolean isWhite) {
		if (isWhite) {
			whiteControl++;
		} else {
			blackControl++;
		}
	}
	
	public Object clone() {
		Square cloned = new Square();
		
		if (piece != null) {
			cloned.piece = (Piece) piece.clone();
		}
		
		return cloned;
	}

}
