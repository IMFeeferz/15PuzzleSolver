import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class GameButton extends Button{
	
	int row, col, puzzleVal;
	String value;
	
	GameButton(int _Row, int _Col, int _PuzzleVal){
		// Initialize all of the variables that are being passed in
		row = _Row;
		col = _Col;
		puzzleVal = _PuzzleVal;
	}
}
