package mines;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/* Represents the entry point for the Minesweeper JavaFX application. 
It initializes and launches the Minesweeper game with a graphical user interface. */
public class RunMineFX extends Application {

	/*
	 * Starts the JavaFX application. stage: The primary stage for this application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// Load the FXML file defining the user interface
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mines.fxml"));
		Parent root = loader.load();// Load the FXML file into a Parent object

		// Image for icon
		File file2 = new File("src/graphics/spike.png"); // Path relative to project folder
		Image spike = new Image(file2.toURI().toString());

		// Set the title of the application window
		stage.setTitle("The Amazing Mines Sweeper");
		stage.getIcons().add(spike);
		stage.setResizable(false);
		// Get the controller from the FXMLLoader
		Controller controller = loader.getController();

		// Initialize the Minesweeper game with default dimensions and number of mines
		controller.initGame(10, 10, 10);

		// Set the scene with the loaded FXML layout and show the stage
		stage.setScene(new Scene(root));
		stage.show();

	}

	/*
	 * The main method that launches the JavaFX application. args: Command-line
	 * arguments passed to the program.
	 */
	public static void main(String[] args) {
		launch(args);// Launch the JavaFX application
	}

}
