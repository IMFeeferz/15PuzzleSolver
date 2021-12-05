import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
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
	Button A_StarBtn, heuristicOne, heuristicTwo, seeSolution;
	GameButton button;
	int[] myPuzzle;
	PauseTransition pause;
	Stage myStage;
	Scene startScene;
	ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	
	// Objects for the 15 puzzle project
	Node myNode;
	UserInterface myInterface;
	DB_Solver2 DBSolver;
	A_IDS_A_15solver mySolver;
	
	ArrayList<Integer> values = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
	ArrayList<Node> solutionPath = new ArrayList<Node>();

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
		startPane = new BorderPane();
		A_StarBtn = new Button("A_Star");
		heuristicOne = new Button("Heuristic One");
		heuristicTwo = new Button("Heuristic Two");
//		heuristicOne.setDisable(true);
//		heuristicTwo.setDisable(true);
		
		myNode = new Node(myInterface.puzzle);
		
		// Generate the puzzle
		int i = 0;
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
					
					//Assign indexes in relation to button being clicked
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
						//System.out.println(+index);
						// Print statements to help debug
						System.out.println("Down");
						System.out.println(myInterface.puzzle[downIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);
					}
					else if((rightIndex < 16 && rightIndex >= 0) && myInterface.puzzle[rightIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[rightIndex]; 
						myInterface.puzzle[rightIndex] = tempIndex;
						// Print statements to help debug
						System.out.println("Right");
						System.out.println(myInterface.puzzle[rightIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);
					}
					else if((upIndex < 16 && upIndex >= 0) && myInterface.puzzle[upIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[upIndex]; 
						myInterface.puzzle[upIndex] = tempIndex;
						// Print statements to help debug
						System.out.println("Up");
						System.out.println(myInterface.puzzle[upIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);

					}
					else if((leftIndex < 16 && leftIndex >= 0) && myInterface.puzzle[leftIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[leftIndex]; 
						myInterface.puzzle[leftIndex] = tempIndex;
						// Print statements to help debug
						System.out.println("Left");
						System.out.println(myInterface.puzzle[leftIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);
						
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
		// When the heuristic buttons are clicked
		heuristicOne.setOnAction(r->{
			// Performs executor service
			executorService.submit(new Runnable() {
			    public void run() {
			    	// Utilize mySolver object to get solution path from the node, get the puzzle 
			    	// from each path, and update the board by calling updateBoard function
			    	mySolver = new A_IDS_A_15solver();
			    	solutionPath = mySolver.A_Star(myNode, "heuristicOne");
			    	for(int j = 0; j < solutionPath.size(); j++) {
			    		Node curNode;
			    		curNode = solutionPath.get(j);
			    		int[] puzzleArray = curNode.getKey();
			    		Platform.runLater(()->updateBoard(puzzleArray));
			    	}
			    }
			});
		});
		
		heuristicTwo.setOnAction(r->{
			// Performs executor service
			executorService.submit(new Runnable() {
			    public void run() {
			    	// Utilize mySolver object to get solution path from the node, get the puzzle 
			    	// from each path, and update the board by calling updateBoard function
			    	mySolver = new A_IDS_A_15solver();
			    	solutionPath = mySolver.A_Star(myNode, "heuristicTwo");
			    	for(int j = 0; j < solutionPath.size(); j++) {
			    		Node curNode;
			    		curNode = solutionPath.get(j);
			    		int[] puzzleArray = curNode.getKey();
			    		Platform.runLater(()->updateBoard(puzzleArray));
			    	}
			    }
			});
		});				
		
		startPane.setPadding(new Insets(70));
		startPane.setTop(heuristicOne);
		startPane.setBottom(heuristicTwo);
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
	
	public void updateBoard(int[] _Values){//, GameButton button) {
		// Reset everything and create new gameboard with updated array
		gameboard.getChildren().clear();
		int j = 0;
		for(int row = 0; row < 4; row++) {
			for(int col = 0; col < 4; col++) {	
				//Assign the buttons based on their respective rows, columns, and values
				button = new GameButton(col, row, _Values[j]);
				
				//Make puzzle 0 white while the rest is colored
				if(myInterface.puzzle[j] == 0) {
					button.setStyle("-fx-background-color: white");
					button.setText(" ");
				}
				else {
					button.setStyle("-fx-background-color: magenta");
					button.setText(Integer.toString(button.puzzleVal));
				}
				
				j++;
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
						//b.setText(Integer.toString(b.puzzleVal));
						//System.out.println(+index);
						// Print statements to help debug
						System.out.println("Down");
						System.out.println(myInterface.puzzle[downIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);
					}
					else if((rightIndex < 16 && rightIndex >= 0) && myInterface.puzzle[rightIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[rightIndex]; 
						myInterface.puzzle[rightIndex] = tempIndex;
						//b.setText(Integer.toString(b.puzzleVal));
						
						// Print statements to help debug
						System.out.println("Right");
						System.out.println(myInterface.puzzle[rightIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);
					}
					else if((upIndex < 16 && upIndex >= 0) && myInterface.puzzle[upIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[upIndex]; 
						myInterface.puzzle[upIndex] = tempIndex;
						//b.setText(Integer.toString(b.puzzleVal));
						// Print statements to help debug
						System.out.println("Up");
						System.out.println(myInterface.puzzle[upIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);

					}
					else if((leftIndex < 16 && leftIndex >= 0) && myInterface.puzzle[leftIndex] == 0) {
						tempIndex = myInterface.puzzle[index];
						myInterface.puzzle[index] = myInterface.puzzle[leftIndex]; 
						myInterface.puzzle[leftIndex] = tempIndex;
						//b.setText(Integer.toString(b.puzzleVal));
						// Print statements to help debug
						System.out.println("Left");
						System.out.println(myInterface.puzzle[leftIndex]);
						System.out.println("Row: " +b.row+" Col: "+b.col+" Value: "+b.puzzleVal+" Index: "+index);
						System.out.println();
						updateBoard(myInterface.puzzle);
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
	}
}




/*btnHandler = new EventHandler<ActionEvent>() {

public void handle(ActionEvent e) {

	GameButton btnPressed = (GameButton)e.getSource();

	seeSolution.setDisable(true);

	int[] tempArray = puzzle.userMove(puzzleArray, btnPressed);



	if (tempArray != null) {

swapButtons(btnPressed);				// swaps the buttons on the grid

		puzzleArray = tempArray;				// updates the puzzleArray

//startState = new Node (puzzleArray);	// updates the startState node

		puzzle.startState = new Node(puzzleArray);

grid.getChildren().clear();				// clears the grid

addGrid(grid, puzzleArray);				// updates the grid with the new state
*/
