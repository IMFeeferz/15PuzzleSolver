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
	MenuBar menu_bar;
	GridPane gameboard, nextBoard;
	BorderPane startPane;
	TextField textField;
	ListView<String> displayItems;
	ObservableList<String> storeQueueItemsInListView;
	HashMap<String, Scene> sceneMap; 
	Button A_StarBtn, leftBtn, rightBtn, upBtn, downBtn;
	GameButton button;
	int i = 0;
	int[] myPuzzle;
	PauseTransition pause;
	Stage myStage;
	Scene startScene;
	
	// Objects for the 15 puzzle project
	Node myNode;
	UserInterface myInterface;
	DB_Solver2 DBSolver;
	A_IDS_A_15solver mySolver;
	
	ArrayList<Integer> values = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
	//ArrayList<ArrayList<Integer>> valuesArray = new ArrayList<ArrayList<Integer>>();;

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
		myInterface = new UserInterface();
		
		//myInterface objects collects the values from the arrayList after it gets shuffled
		for(int i = 0; i < values.size(); i++) {
			myInterface.puzzle[i] = values.get(i);
		}
		
		myStage = primaryStage;
		gameboard = new GridPane();
		nextBoard = new GridPane();
		startPane = new BorderPane();
		A_StarBtn = new Button("A_Star");
		leftBtn = new Button("Left");
		rightBtn = new Button("Right");
		upBtn = new Button("Up");
		downBtn = new Button("Down");
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
		
		// Generate the puzzle
		for(int row = 0; row < 4; row++) {
			for(int col = 0; col < 4; col++) {
				
				//Assign the buttons based on their respective rows, columns, and values
				button = new GameButton(col, row, myInterface.puzzle[i]);
				
				//Make puzzle 0 white while the rest is colored
				if(myInterface.puzzle[i] == 0) {
					button.setStyle("-fx-background-color: white");
					button.setText(" ");
				}
				else {
					button.setStyle("-fx-background-color: magenta");
					button.setText(Integer.toString(button.puzzleVal));
				}
				
				i++;
				button.setOnAction(e->{
					// b carries the current GameButton object being clicked
					GameButton b = (GameButton) e.getSource();
					
					//Assign indexes
					int index = (4*b.row)+b.col;
					int leftIndex = index - 1;
					int rightIndex = index + 1;
					int upIndex = index - 4;
					int downIndex = index + 4;
					int tempIndex = 0;
					
					// Check each direction to see if the adjacent value is 0 upon being clicked and 
					// swap the values and update the gameboard if one of the conditions satisfy
					if((downIndex < 16 && downIndex >= 0) && myInterface.puzzle[downIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[downIndex]; 
						myInterface.puzzle[downIndex] = tempIndex;
						button.setText(Integer.toString(button.puzzleVal));
						// Print statements to help debug
						System.out.println("Down");
						System.out.println(myInterface.puzzle[downIndex]);
						System.out.println();
					}
					else if((rightIndex < 16 && rightIndex >= 0) && myInterface.puzzle[rightIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[rightIndex]; 
						myInterface.puzzle[rightIndex] = tempIndex;
						button.setText(Integer.toString(button.puzzleVal));
						// Print statements to help debug
						System.out.println("Right");
						System.out.println(myInterface.puzzle[rightIndex]);
						System.out.println();
					}
					else if((upIndex < 16 && upIndex >= 0) && myInterface.puzzle[upIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[upIndex]; 
						myInterface.puzzle[upIndex] = tempIndex;
						button.setText(Integer.toString(button.puzzleVal));
						// Print statements to help debug
						System.out.println("Up");
						System.out.println(myInterface.puzzle[upIndex]);
						System.out.println();
					}
					else if((leftIndex < 16 && leftIndex >= 0) && myInterface.puzzle[leftIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[leftIndex]; 
						myInterface.puzzle[leftIndex] = tempIndex;
						button.setText(Integer.toString(button.puzzleVal));
						// Print statements to help debug
						System.out.println("Left");
						System.out.println(myInterface.puzzle[leftIndex]);
						System.out.println();
					}
					else {
						System.out.println("Invalid move");
					}
					// Prints the array from the UserInterface object to help debug
					myInterface.printArray();
				});
	    		button.setPrefSize(70, 70);
	    		gameboard.add(button, col, row);
			}
		}
//		for(i = 0; i < myInterface.puzzle.length; i++) {
//			if(myInterface.puzzle[i] == 0) {
//				index = i;
//			}
//		}
//		leftIndex = index - 1;
//		rightIndex = index + 1;
//		upIndex = index - 4;
//		downIndex = index + 4;

		//System.out.println(+upIndex+" "+downIndex+" "+leftIndex+" "+rightIndex);
		
		// Solves the puzzle through A_Star algorithm
		A_StarBtn.setOnAction(e->{
			DBSolver.findSolutionPath();
		});
				
		
		startPane.setPadding(new Insets(70));
		startPane.setRight(A_StarBtn);
		A_StarBtn.setPrefSize(100, 30);
		startPane.setCenter(gameboard);
		startScene = new Scene(startPane, 500,500);
		
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
