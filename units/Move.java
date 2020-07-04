package units;

public class Move {
	public Position before = null;
	public Position after = null;
	
	public Move(Position before, Position after) {
		this.before = before;
		this.after = after;
	}
	
	public Move (String move) {
		before = new Position(move.substring(0, 2));
		after = new Position(move.substring(2, 4));
	}
	
	public boolean isValid() {
		return before != null && before.isValid() && after != null && after.isValid();
	}
	
	public boolean equals(Object o) {
		Move move = (Move) o;
		
		return before.equals(move.before) && after.equals(move.after);
	}
	
	public String toString() {
		return before.toString() + after.toString();
	}
}
