package minesweeper;

public class Command {
	int x;
	int y;
	CommandEnum commandEnum;

	public Command(String commandLine) {
		String[] split = commandLine.split(" ");

		this.x = Integer.parseInt(split[0]);
		this.y = Integer.parseInt(split[1]);
		commandEnum = CommandEnum.valueOf(split[2]);
	}

	public enum CommandEnum {
		free, mine
	}
}
