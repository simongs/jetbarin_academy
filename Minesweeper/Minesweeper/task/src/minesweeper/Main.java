package minesweeper;

import java.util.Scanner;

public class Main {
	public final static int STAGE_SIZE = 9;

    public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("How many mines do you want on the field?");
		int mineCount = scanner.nextInt();

		MineGame mineGame = new MineGame(mineCount);

		mineGame.play();
    }
}
