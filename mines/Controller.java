package mines;

import java.io.File;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

/* Controls the Minesweeper game logic and interaction with the user interface.
Handles board setup, user input, and game state updates. */
public class Controller {
	private MinesLogic mines; // Represents the Minesweeper game board
	private Button[][] buttons; // Array of buttons representing the game cells

	@FXML
	private Button resetButton;// Button to reset the game

	@FXML
	private GridPane g;// GridPane to display the game board

	@FXML
	private TextField textBoxHieght;// TextField for board height input

	@FXML
	private TextField textBoxMines;// TextField for number of mines input

	@FXML
	private TextField textBoxWidth;// TextField for board width input

	@FXML
	private StackPane window;// StackPane to hold the game layout

	@FXML
	private MenuItem GuideMenu;// MenuItem to show how to play the game

	@FXML
	private MenuItem AboutMenu;// MenuItem to show the Creator

	private double cellSize = 40;// Cell button size
	/*
	 * Resets the Minesweeper game based on user input for dimensions and mines.
	 * event: The action event triggered by clicking the reset button.
	 */

	@FXML
	void ResetMines(ActionEvent event) {
		int rows = Integer.parseInt(textBoxHieght.getText());
		int cols = Integer.parseInt(textBoxWidth.getText());
		int mines = Integer.parseInt(textBoxMines.getText());
		initGame(rows, cols, mines);// Initialize the game with new parameters
		resizeLayout(rows, cols);// Adjust layout size
	}
	//Show guide window when clicking guide in the menu
	@FXML
	void guide(ActionEvent event) {
		Stage guideStage = new Stage();
		guideStage.setTitle("Minesweeper Guide");
		guideStage.initModality(Modality.APPLICATION_MODAL);

		// Text elements with different styles
		Text title = new Text("Welcome to Minesweeper!\n\n");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		Text howToPlay = new Text("How to Play:\n");
		howToPlay.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		Text steps = new Text("\n  1. Click a tile to reveal it.\n\n"
				+"  2. Right-click a tile to place a flag (🚩) if you suspect a mine.\n     Right-click again to remove the flag.\n\n"
				+"  3. Numbers indicate how many mines are nearby.\n\n");

		Text winning = new Text("Winning:\n");
		winning.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		Text winText = new Text("- Clear all non-mine tiles.\n\n");

		Text losing = new Text("Losing:\n");
		losing.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		Text loseText = new Text("- Clicking a mine ends the game.\n\n");

		Text goodLuck = new Text("Good luck and have fun!");
		goodLuck.setFont(Font.font("Arial", FontWeight.BOLD, 12));

		// Create TextFlow with different styled texts
		TextFlow guideTextFlow = new TextFlow(title, howToPlay, steps, winning, winText, losing, loseText, goodLuck);

		ScrollPane scrollPane = new ScrollPane(guideTextFlow);
		scrollPane.setFitToWidth(true);

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> guideStage.close());

		VBox layout = new VBox(10, scrollPane, closeButton);
		layout.setStyle("-fx-padding: 15px; -fx-alignment: center;");

		Scene scene = new Scene(layout, 400, 300);
		guideStage.setScene(scene);
		guideStage.showAndWait();
	}
	//Show about when clicking in the menu 
	@FXML
	void about(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("Welcome to minesweeper!");
		alert.setContentText(
				"Hi, I'm Rafi Azulay,\na third-year Software Engineering student with a passion for game development.\n"
						+ "This Minesweeper project showcases my skills in Java and JavaFX, focusing on logic, design, and user experience.\n"
						+ "I built this game to challenge players with a classic puzzle experience while enhancing my programming expertise.\n"
						+ "Enjoy the game and test your strategy!\n");
		alert.showAndWait();
	}

	@FXML
	void initialize() {
		resetButton.setDisable(true);
		// Add listener to the textBoxHieght
		textBoxHieght.textProperty().addListener((observable, oldValue, newValue) -> {
			handleTextFieldChange();
		});

		// Add listener to the textBoxWidth
		textBoxWidth.textProperty().addListener((observable, oldValue, newValue) -> {
			handleTextFieldChange();
		});
	}

	/*
	 * Validates the input and disables the reset button if invalid input is found.
	 */
	private void handleTextFieldChange() {
		try {
			// Try to parse the value as an integer to check if it's valid
			int width = Integer.parseInt(textBoxWidth.getText());
			int hieght = Integer.parseInt(textBoxHieght.getText());
			// Disable resetButton if value is less than or equal to 0
			if (width <= 0 || hieght <= 0) {
				resetButton.setDisable(true);
			} else {
				resetButton.setDisable(false);
			}
		} catch (NumberFormatException e) {
			// If parsing fails (non-numeric input), disable resetButton
			resetButton.setDisable(true);
		}
	}

	/*
	 * Displays an error message in an alert dialog. msg: The error message to
	 * display.
	 */
	private void showError(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	/*
	 * Initializes the Minesweeper game board and UI. rows: The number of rows in
	 * the board. cols: The number of columns in the board. numMines: The number of
	 * mines to place on the board.
	 */
	public void initGame(int rows, int cols, int numMines) {
		mines = new MinesLogic(rows, cols, 0);// Initialize the Mines board
		placeRandomMines(rows, cols, numMines);// Randomly place mines
		buttons = new Button[rows][cols];

		// Adjust grid properties for even button spacing
		g.getColumnConstraints().clear();
		g.getRowConstraints().clear();

		// Clear any existing buttons from the GridPane
		g.getChildren().clear();

		// Set up fixed-size rows and columns
		for (int i = 0; i < cols; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setMinWidth(cellSize); // Set fixed width for each column (adjust as needed)
			column.setMaxWidth(cellSize);
			g.getColumnConstraints().add(column);
		}

		for (int i = 0; i < rows; i++) {
			RowConstraints row = new RowConstraints();
			row.setMinHeight(cellSize); // Set fixed height for each row (adjust as needed)
			row.setMaxHeight(cellSize);
			g.getRowConstraints().add(row);
		}

		// Create buttons for each cell and add them to the GridPane
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// Create a button
				Button b = new Button(".");
				b.setPrefSize(cellSize, cellSize);// Set button size
				b.setUserData(new int[] { i, j });// Store cell coordinates
				b.setOnMouseClicked(this::handleCellClick);// Handle cell clicks
				buttons[i][j] = b;
				g.add(b, j, i); // Add the label at column 'j', row 'i'
			}
		}
		drawBoard();// Draw the initial board state

	}

	/* Adjusts the layout size to fit dynamically within the window. */
	private void resizeLayout(int rows, int cols) {
		// Request a layout update for the window
		window.requestLayout();

		// Calculate the total width and height based on the grid size
		double totalWidth = cols * cellSize + 160;
		double totalHeight = rows * cellSize + 84;

		// Request the stage to resize itself based on the new preferred size
		Stage stage = (Stage) window.getScene().getWindow();

		stage.setWidth(Double.max(totalWidth, 300)); // Add padding to the width
		stage.setHeight(Double.max(totalHeight, 200)); // Add padding to the height

		// Make sure the layout gets recomputed
		window.requestLayout();
	}

	/*
	 * Randomly places mines on the game board. rows: The number of rows in the
	 * board. cols: The number of columns in the board. numMines: The number of
	 * mines to place.
	 */
	private void placeRandomMines(int rows, int cols, int numMines) {
		Random rand = new Random();
		int minesPlaced = 0;
		while (minesPlaced < numMines) {
			int r = rand.nextInt(rows);
			int c = rand.nextInt(cols);
			if (mines.addMine(r, c)) {// Add mine if the cell is empty
				minesPlaced++;

			}
		}
	}

	/*
	 * Handles mouse clicks on game cells. e: The mouse event triggered by clicking
	 * a cell.
	 */
	private void handleCellClick(MouseEvent e) {
		Button b = (Button) e.getSource();
		int[] rc = (int[]) b.getUserData();
		int row = rc[0];
		int col = rc[1];

		if (e.getButton() == MouseButton.PRIMARY) { // Left-click: Open the cell
			// left click-> open
			boolean notMine = mines.open(row, col);
			if (!notMine) {// If the cell contains a mine, end the game
				// we hit a mine -> reveal all
				mines.setShowAll(true);
				drawBoard();
				showError("Game is Over! You clicked a mine here!");
				return;
			}
		} else if (e.getButton() == MouseButton.SECONDARY) {// Right-click: Toggle flag
			// toggling flag in case of right click
			mines.toggleFlag(row, col);
		}

		drawBoard();

		if (mines.isDone()) {// If all non-mine cells are opened, the player wins
			// in case user is done
			mines.setShowAll(true);
			drawBoard();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Good job mate!");
			alert.setHeaderText(null);
			alert.setContentText("You won!");
			alert.showAndWait();
		}
	}

	/*
	 * Sets and returns a graphical icon for flags or mines. s: The string
	 * representing the icon type ("F" for flag, "X" for mine). Returns an ImageView
	 * with the specified graphic.
	 */
	private ImageView setGrapich(String s) {
		File file1 = new File("src/graphics/flag.png"); // Path relative to project folder
		File file2 = new File("src/graphics/spike.png"); // Path relative to project folder
		Image flag = new Image(file1.toURI().toString());
		Image spike = new Image(file2.toURI().toString());
		// Create an ImageView to display the image
		ImageView imageView1 = new ImageView(flag);
		ImageView imageView2 = new ImageView(spike);
		// Optionally, you can resize the image to fit the button
		imageView1.setFitWidth(15); // Set the width of the image
		imageView1.setFitHeight(15); // Set the height of the image
		imageView1.setPreserveRatio(true);
		imageView2.setFitWidth(15); // Set the width of the image
		imageView2.setFitHeight(15); // Set the height of the image
		imageView2.setPreserveRatio(true);
		if (s.equals("F")) {
			return imageView1;
		}
		return imageView2;
	}

	/* Updates the visual representation of the game board. */
	private void drawBoard() {
		if (buttons == null || mines == null)
			return;
		int rows = buttons.length;
		int cols = buttons[0].length;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String cell = mines.get(r, c);
				Button btn = buttons[r][c];
				btn.setText("");
				if (cell.equals("F")) {
					btn.setGraphic(setGrapich("F"));
				} else if (cell.equals("X")) {
					btn.setGraphic(setGrapich("X"));
				} else {
					btn.setGraphic(null);
					btn.setText(cell);
				}
				btn.setStyle("");
				if (cell.matches("[1-8]")) {// Set text color based on the number of mines
					switch (cell) {
					case "1":
						btn.setStyle("-fx-text-fill: blue;");
						break;
					case "2":
						btn.setStyle("-fx-text-fill: green;");
						break;
					case "3":
						btn.setStyle("-fx-text-fill: red;");
						break;
					case "4":
						btn.setStyle("-fx-text-fill: navy;");
						break;
					case "5":
						btn.setStyle("-fx-text-fill: maroon;");
						break;
					case "6":
						btn.setStyle("-fx-text-fill: teal;");
						break;
					case "7":
						btn.setStyle("-fx-text-fill: black;");
						break;
					case "8":
						btn.setStyle("-fx-text-fill: gray;");
						break;
					}
				}
			}
		}
	}
}
