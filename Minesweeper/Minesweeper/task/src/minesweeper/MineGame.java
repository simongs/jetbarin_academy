package minesweeper;

import minesweeper.Command.CommandEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class MineGame {

	public final static int STAGE_SIZE = 9;

	Cell[][] cellInfoArray = new Cell[STAGE_SIZE][STAGE_SIZE];

	int correctMineCount = 0;
	int targetMineCount;

	Command firstCommand;

	public MineGame(int mineCount) {
		targetMineCount = mineCount;

		// Cell 배열 초기화
		initCellInfoArray();
	}

	private void initCellInfoArray() {
		for (int i = 0; i < STAGE_SIZE; i++) {
			for (int j = 0; j < STAGE_SIZE; j++) {
				cellInfoArray[i][j] = new Cell(i, j, false);
			}
		}
	}


	private void calculateNearbyMineCount(Cell[][] cellInfoArray) {
		for (int i = 0; i < STAGE_SIZE; i++) {
			for (int j = 0; j < STAGE_SIZE; j++) {
				Cell cell = cellInfoArray[i][j];
				if (cell.isMine) {
					// 8방향으로 숫자정보를 업데이트 해준다.
					for (Direction direction : Direction.values()) {
						updateCellInfoArray(cellInfoArray, i + direction.x, j + direction.y);
					}
				}
			}
		}
	}

	private void updateCellInfoArray(Cell[][] cellInfoArray, int i, int j) {
		if (i < 0 || j < 0 || i >= STAGE_SIZE || j >= STAGE_SIZE) {
			return;
		}

		Cell cell = cellInfoArray[i][j];
		if (cell.isMine) {
			return;
		}

		cell.updateNearCount();
	}

	private void createRandomMineInfoToCellInfoArray(int mineCount) {
		List<Mine> mineList = new ArrayList<>();

		for (int i = 0 ; i < mineCount ; i++) {
			mineList.add(getUniqueMine(mineList));
		}

		for (Mine mine : mineList) {
			cellInfoArray[mine.x][mine.y] = new Cell(mine.x, mine.y, true);
		}
	}

	private Mine getUniqueMine(List<Mine> mineList) {
		while (true) {
			final Mine randomMine = Mine.createNewRandomEntity();

			// 최초에 입력받은 좌표는 지뢰가 되면 안된다.
			if (firstCommand.x == randomMine.x && firstCommand.y == randomMine.y) {
				continue;
			}

			boolean isIncludeNearbyFirstCommand = false;
			for (Direction direction : Direction.values()) {
				if (firstCommand.x == randomMine.x + direction.x &&
						firstCommand.x == randomMine.y + direction.y) {
					isIncludeNearbyFirstCommand = true;
				}
			}

			if (isIncludeNearbyFirstCommand) {
				continue;
			}

			long count = mineList.stream().filter(i -> i.equals(randomMine)).count();
			if (count > 0) {
				continue;
			}

			return randomMine;
		}
	}

	public void play() {
		boolean isFirst = true;
		Scanner scanner = new Scanner(System.in);

		try {
			while (true) {
				printMineStage();

				int notExposureCount = getNotExposureCount();
				if (targetMineCount == (notExposureCount + correctMineCount)) {
					System.out.println("Congratulations! You found all mines!");
					return;
				}

				if (targetMineCount == correctMineCount) {
					System.out.println("Congratulations! You found all mines!");
					return;
				}

				System.out.print("Set/unset mines marks or claim a cell as free: ");

				Command command = new Command(scanner.nextLine());

				if (isFirst) {
					initializeMineGame(command);
					isFirst = false;
				}

				solveProblem(command);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private int getNotExposureCount() {
		int count = 0;

		for (int i = 0; i < STAGE_SIZE; i++) {
			for (int j = 0; j < STAGE_SIZE; j++) {
				Cell cell = cellInfoArray[i][j];
				if (!cell.isExposure) {
					count += 1;
				}
			}
		}

		return count;
	}

	private void initializeMineGame(Command command) {
		firstCommand = command;

		createRandomMineInfoToCellInfoArray(targetMineCount);

		calculateNearbyMineCount(cellInfoArray);
	}

	public void printMineStage() {
		System.out.println(" │123456789│");
		System.out.println("—│—————————│");

		for (int i = 0; i < STAGE_SIZE; i++) {
			System.out.print( (i + 1) + "|");
			for (int j = 0; j < STAGE_SIZE; j++) {
				Cell cell = cellInfoArray[i][j];

				if (cell.isExposure == false) {
					System.out.print(".");
				} else if (cell.isChecked) { // checked
					System.out.print("*");
				} else  if (cell.isMine) {
					System.out.print(".");
				} else if (cell.nearbyMineCount == 0) {
					System.out.print("/");
				} else {
					System.out.print(cell.nearbyMineCount);
				}
			}
			System.out.println("|");
		}

		System.out.println("—│—————————│");
	}

	private void solveProblem(Command command) {
		Cell cell = cellInfoArray[command.y - 1][command.x - 1];

		cell.isExposure = true;
		
		if (command.commandEnum == CommandEnum.free) {
			if (cell.nearbyMineCount == 0) {
				// BFS로 8방향으로 검색시작
				bfs(cell);
			}
		} else {
			// 지뢰라고 한 경우
			if (!cell.isMine) {
				throw new RuntimeException("You stepped on a mine and failed!");
			}

			if (!cell.isChecked) {
				cell.onChecked();
				correctMineCount += 1;
			} else {
				cell.onUnChecked();
				correctMineCount -= 1;
			}
		}

	}

	private void bfs(Cell cell) {
		Queue<Cell> q = new LinkedList<>();

		addNextDepth(q, cell);

		while(!q.isEmpty()) {
			Cell pollCell = q.poll();
			pollCell.isExposure = true;

			if (pollCell.nearbyMineCount == 0) {
				addNextDepth(q, pollCell);
			}
		}
	}

	private void addNextDepth(Queue<Cell> q, Cell cell) {
		for (Direction direction : Direction.values()) {
			int x = cell.x + direction.x;
			int y = cell.y + direction.y;

			if (x < 0 || y < 0 || x >= STAGE_SIZE || y >= STAGE_SIZE) {
				continue;
			}

			Cell nextCell = cellInfoArray[x][y];

			if (nextCell.isExposure) {
				continue;
			}

			if (nextCell.isMine) {
				continue;
			}

			if (nextCell.nearbyMineCount >= 0) {
				System.out.println("좌표 " + x + " " + y + "가 추가됨" + direction);
				q.add(nextCell);
			}
		}
	}


}
