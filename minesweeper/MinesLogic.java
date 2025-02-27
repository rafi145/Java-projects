package mines;

import java.util.ArrayList;
import java.util.List;

/* Represents a Minesweeper game board with specified dimensions and number of mines. */
public class MinesLogic {
	private int numMines;
	private int height, width;
	private boolean showAll;
	private CheckDot dots[][]; // The game board represented as a 2D array of cells

	/*
	 * Initializes the Mines game with the specified height, width, and number of
	 * mines. height: The height of the board. width: The width of the board.
	 * numMines: The initial number of mines on the board.
	 */
	public MinesLogic(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		this.showAll = false;
		this.numMines = numMines;
		dots = new CheckDot[height][width];

		// Initialize the board with empty cells
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				dots[i][j] = new CheckDot(i, j);
			}
		}

		// Set neighbors for each cell
		for (CheckDot[] row1 : dots) {
			for (CheckDot col1 : row1) {
				col1.getNeighbors();
			}
		}
	}

	/*
	 * Adds a mine to the specified position on the board. row: The row index of the
	 * position. col: The column index of the position. Returns true if the mine was
	 * added successfully; false otherwise.
	 */
	public boolean addMine(int row, int col) {
		if (row < height && col < width && row >= 0 && col >= 0) {
			dots[row][col].setMine(true);
			numMines++;
			return true;
		}
		return false;
	}

	/*
	 * Opens a cell at the specified position. row: The row index of the position.
	 * col: The column index of the position. Returns false if the cell contains a
	 * mine; otherwise true.
	 */
	public boolean open(int row, int col) {
		if (dots[row][col].isMine) {// If the cell is a mine, return false
			return false;
		} else if (!dots[row][col].isOpen) {// Open the cell if it hasn't been opened yet
			dots[row][col].setOpen(true);

			// If no mines are nearby, recursively open neighbors
			if (dots[row][col].numOfMines() == 0) { // Only recurse if no mines nearby
				for (CheckDot dot : dots[row][col].Nlist) {
					if (dots[row][col].toString() == " ")
						open(dot.i, dot.j);
				}
				return true;
			}

		}
		return true;
	}

	/*
	 * Toggles a flag on a cell at the specified position. row: The row index of the
	 * position. col: The column index of the position.
	 */
	public void toggleFlag(int row, int col) {
		if (dots[row][col].isFlag) {
			dots[row][col].setFlag(false);
		} else {
			dots[row][col].setFlag(true);
		}
	}

	/*
	 * Checks if the game is completed, i.e., all non-mine cells are opened. Returns
	 * true if the game is done; false otherwise.
	 */
	public boolean isDone() {
		int openCnt = 0;
		for (CheckDot[] dotRow : dots) {
			for (CheckDot dotCol : dotRow) {
				if (dotCol.isOpen)
					openCnt++;
			}
		}
		// Game is done if the number of opened cells equals the total non-mine cells
		return ((height * width) - numMines) == openCnt;
	}

	/*
	 * Retrieves the string representation of the cell at the specified position.
	 * row: The row index of the position. col: The column index of the position.
	 * Returns the string representation of the cell.
	 */
	public String get(int row, int col) {
		return dots[row][col].toString();
	}

	/*
	 * Sets whether to reveal all cells on the board. showAll: True to reveal all
	 * cells, false to show only opened cells.
	 */
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	/* Returns a string representation of the game board. */
	public String toString() {
		StringBuilder g = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				g.append(get(i, j));// Append the string representation of each cell
			}
			g.append("\n");// Add a newline at the end of each row
		}

		return g.toString();
	}

	/* Represents a single cell on the game board. */
	private class CheckDot {
		private int i, j;// Coordinates of the cell
		private boolean isFlag;// Whether the cell is flagged
		private boolean isMine;// Whether the cell contains a mine
		private boolean isOpen;// Whether the cell is opened
		private List<CheckDot> Nlist = new ArrayList<>();

		/*
		 * Initializes a cell at the specified position. i: The row index of the cell.
		 * j: The column index of the cell.
		 */
		public CheckDot(int i, int j) {
			this.i = i;
			this.j = j;
			this.isFlag = false;
			this.isMine = false;
			this.isOpen = false;
		}

		/*
		 * Retrieves the neighbors of the cell and stores them in Nlist. Returns a list
		 * of neighboring cells.
		 */
		public List<CheckDot> getNeighbors() {
			return Nlist = neighborsList();
		}

		/*
		 * Checks if the cell is flagged. Returns true if the cell is flagged; otherwise
		 * false.
		 */
		public boolean checkFlag() {
			return isFlag;
		}

		/*
		 * Checks if the cell contains a mine. Returns true if the cell contains a mine;
		 * otherwise false.
		 */
		public boolean checkMine() {
			return isMine;
		}

		/*
		 * Checks if the cell is opened. Returns true if the cell is opened; otherwise
		 * false.
		 */
		public boolean checkOpen() {
			return isOpen;
		}

		/*
		 * Sets the flagged status of the cell. isFlag: True to flag the cell, false to
		 * unflag it.
		 */
		public void setFlag(boolean isFlag) {
			this.isFlag = isFlag;
		}

		/*
		 * Sets whether the cell contains a mine. isMine: True if the cell contains a
		 * mine, false otherwise.
		 */
		public void setMine(boolean isMine) {
			this.isMine = isMine;
		}

		/*
		 * Sets whether the cell is opened. isOpen: True to open the cell, false to
		 * close it.
		 */

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

		/*
		 * Checks if the specified position is within the board boundaries. i: The row
		 * index. j: The column index. Returns true if the position is within bounds;
		 * otherwise false.
		 */
		public boolean inBoard(int i, int j) {
			return i < height && j < width && i >= 0 && j >= 0;
		}

		/*
		 * Retrieves a list of neighboring cells for this cell. Returns a list of valid
		 * neighbors.
		 */
		public List<CheckDot> neighborsList() {
			List<CheckDot> list = new ArrayList<>();

			// Check all 8 possible neighbors and add them if they are within bounds
			if (inBoard(i - 1, j - 1)) {
				list.add(dots[i - 1][j - 1]);
			}
			if (inBoard(i - 1, j)) {
				list.add(dots[i - 1][j]);
			}
			if (inBoard(i - 1, j + 1)) {
				list.add(dots[i - 1][j + 1]);
			}
			if (inBoard(i, j - 1)) {
				list.add(dots[i][j - 1]);
			}
			if (inBoard(i, j + 1)) {
				list.add(dots[i][j + 1]);
			}
			if (inBoard(i + 1, j - 1)) {
				list.add(dots[i + 1][j - 1]);
			}
			if (inBoard(i + 1, j)) {
				list.add(dots[i + 1][j]);
			}
			if (inBoard(i + 1, j + 1)) {
				list.add(dots[i + 1][j + 1]);
			}
			return list;
		}

		/*
		 * Counts the number of mines in neighboring cells. Returns the count of
		 * neighboring mines.
		 */
		public int numOfMines() {
			int cnt = 0;
			for (CheckDot s : Nlist) {
				if (s.isMine)
					cnt++;
			}
			return cnt;
		}

		/*
		 * Returns a string representation of the cell, showing its state based on the
		 * game rules.
		 */
		public String toString() {
			int mines = numOfMines();
			if (showAll) {
				if (checkMine())
					return "X";
				if (mines != 0) {
					return mines + "";
				}
				return " ";
			} else {
				if (!checkOpen()) {
					if (checkFlag())
						return "F";
					return ".";
				} else {
					if (checkMine())
						return "X";
					if (mines != 0) {
						return mines + "";
					}
					return " ";
				}
			}
		}
	}
}
