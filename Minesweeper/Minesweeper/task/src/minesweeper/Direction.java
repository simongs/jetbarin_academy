package minesweeper;

public enum Direction {
	NW(-1, -1),
	N(0, -1),
	NE(1, -1),

	E(1, 0),
	W(-1, 0),

	SW(-1, 1),
	S(0, 1),
	SE(1, 1);

	final int x;
	final int y;
	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
