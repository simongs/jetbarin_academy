package minesweeper;

public class Cell {
	int x;
	int y;
	boolean isMine;
	int nearbyMineCount = 0;
	boolean isChecked;
	boolean isExposure;

	public Cell(int x, int y, boolean isMine) {
		this.x = x;
		this.y = y;
		this.isMine = isMine;

		if (isMine) {
			nearbyMineCount = -1;
		}
	}

	public void updateNearCount() {
		nearbyMineCount += 1;
	}

	public void onChecked() {
		isChecked = true;
	}

	public void onUnChecked() {
		isChecked = false;
	}
}
