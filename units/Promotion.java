package units;

public class Promotion extends Move {
	public Character piece = null;
	
	public Promotion(Position before, Position after, Character piece) {
		super(before, after);
		this.piece = piece;
	}

	public Promotion(String promotion) {
		super(promotion);
		piece = promotion.charAt(4);
	}

	public boolean isValid() {
		return super.isValid() && (piece != null);
	}

	public String toString() {
		return super.toString() + piece;
	}
}
