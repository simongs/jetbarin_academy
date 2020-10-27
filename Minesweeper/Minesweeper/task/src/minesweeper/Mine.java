package minesweeper;

import java.util.Objects;
import java.util.Random;

public class Mine {
	private static final Random random = new Random();

	int x;
	int y;

	public Mine(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Mine createNewRandomEntity() {
		int x = random.nextInt(Main.STAGE_SIZE);
		int y = random.nextInt(Main.STAGE_SIZE);

		return new Mine(x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Mine mine = (Mine) o;
		return x == mine.x && y == mine.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "Mine{" + "x=" + x + ", y=" + y + '}';
	}

	public boolean check(int x, int y) {
		return this.x == x && this.y == y;
	}
}
