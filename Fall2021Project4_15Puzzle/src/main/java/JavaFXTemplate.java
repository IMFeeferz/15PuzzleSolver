import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXTemplate extends Application {
	
	ListView<String> displayQueueItems;
	MenuItem menu_item;
	Menu playBtn, themeBtn, optionBtn;
	MenuBar menu_bar;
	GridPane gameboard;
	BorderPane startPane;
	TextField textField;
	ListView<String> displayItems;
	ObservableList<String> storeQueueItemsInListView;
	HashMap<String, Scene> sceneMap; 
	Button A_Star_Button;
	int curPlayer, curTurn, i = 0;
	int[] myPuzzle;
	PauseTransition pause;
	Stage myStage;
	Scene startScene;
	
	Node myNode;
	UserInterface myInterface;
	DB_Solver2 DBSolver;
	A_IDS_A_15solver mySolver;
	
	ArrayList<Integer> values = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
	ArrayList<ArrayList<Integer>> valuesArray = new ArrayList<ArrayList<Integer>>();;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("15 Puzzle Solver");
		Collections.shuffle(values);
		myPuzzle = new int[16];
		
		for(int i = 0; i < values.size(); i++) {
			myPuzzle[i] = values.get(i);
		}
		
		myStage = primaryStage;
		gameboard = new GridPane();
		startPane = new BorderPane();
		myInterface = new UserInterface();
		A_Star_Button = new Button("A_Star");
//		mySolver = new A_IDS_A_15solver();
		myNode = new Node(myInterface.puzzle);
		DBSolver = new DB_Solver2(myNode, "Heuristic");
//		Node solution = DBSolver.findSolutionPath();
//		ArrayList solutionPath = DBSolver.getSolutionPath(solution);
		
//		puzzle = new UserInterface();
		
//		for(int i = 0; i < 4; i++) {
//			valuesArray.add(new ArrayList<Integer>());
//			for(int j = 0; j < 4; j++) {
//				valuesArray.add(values);
//				//valuesArray[i][j] = values;
//			}
//		}
//		for(int i = 0; i < values.size(); i++) {
//			System.out.print(values.get(i)+" ");
//		}
		
//		for(int i = 0; i < values.size(); i++) {
//			myPuzzle = puzzle.stringToIntArray(values.get(i));
//		}
//		myPuzzle = puzzle.getPuzzle();
		for(int col = 0; col < 4; col++) {
			for(int row = 0; row < 4; row++) {
				GameButton button = new GameButton(row, col, myInterface.puzzle[i]);
				i++;
				button.setStyle("-fx-background-color: grey");
				button.setText(Integer.toString(button.puzzleVal));
	    		button.setPrefSize(70, 70);
	    		gameboard.add(button, col, row);
			}
		}
		
		startPane.setPadding(new Insets(70));
		startPane.setRight(A_Star_Button);
		A_Star_Button.setPrefSize(100, 30);
		startPane.setCenter(gameboard);
		startScene = new Scene(startPane, 800,800);
		
		A_Star_Button.setOnAction(e->{
			DBSolver.findSolutionPath();
		});
				
		//Scene scene = new Scene(new VBox(), 700,700);
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	//	Thread t = new Thread(()-> {A_IDS_A_15solver ids = new A_IDS_A_15solver();});
	//	t.start();

	}
	
	public Scene openingScene() {
		Stage primaryStage = new Stage();
		primaryStage.setTitle("15 Puzzle Solver");
		
		return new Scene(startPane, 800, 800);
	}

}
