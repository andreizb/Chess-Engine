package units;

public class Position implements Cloneable {
	public int row;
	public int col;
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Position(String pos) {
		row = '8' - pos.charAt(1);
		col = pos.charAt(0) - 'a';
	}
	
	public boolean isValid() {
		return row >= 0 && row <= 7 && col >= 0 && col <= 7;
	}
	
	public boolean equals(Object o) {
		Position pos = (Position) o;
		
		return row == pos.row && col == pos.col;
	}
	
	public Object clone() {
		return new Position(row, col);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append((char) ('a' + col));
		sb.append((char) ('8' - row));
		
		return sb.toString();
	}
}
