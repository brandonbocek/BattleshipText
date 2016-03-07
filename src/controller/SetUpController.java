package controller;

import java.util.Random;

import model.Board;
import model.Game;
import model.Player;
import view.BattleshipView;

public class SetUpController extends ConvertInputController{

	/* Setting up 4 blank boards, offense and defense for user and offense and defense for cpu */
	
	//accessing a square on all 4 boards at once
	public void setAllBoardSquares(Game game, int row, int column, String value){
		game.getUser().getDefBoard().setSquare(row, column, value);
		game.getCPU().getDefBoard().setSquare(row, column, value);
		game.getUser().getOffBoard().setSquare(row, column, value);
		game.getCPU().getOffBoard().setSquare(row, column, value);
	}
	// setting the A-J and 1-10 labels on the side of the String array on all 4 boards
	private void setBoardSideLabels(Game game) {
		setAllBoardSquares(game, 0, 0, " ");
		for (int i = 1; i < 11; i++) {
			setAllBoardSquares(game, i, 0, String.valueOf(Character.toChars(i + 64)));
			setAllBoardSquares(game, 0, i, String.valueOf(" " + i + " "));
		}
	}
	//makes all blank double String array for the start of a game
	public void setAllBlankBoards(Game game){
		setBoardSideLabels(game);
		for(int i=1; i<game.getUser().getDefBoard().getRows(); i++){
			for(int j=1; j<game.getUser().getDefBoard().getColumns(); j++){
				setAllBoardSquares(game, i, j, "|__");
			}
		}
	}
	/* Setting the ships on a user and cpu defense board*/
	public boolean offBoard=false;
	public boolean overlap=false;
	
	private boolean isOrientH(char or){
		if(or=='h' || or=='H'){
			return true;
		}
		return false;
	}
	private boolean isOrientV(char or){
		if(or=='v' || or=='V'){
			return true;
		}
		return false;
	}
	
	private boolean shipCanBePlaced(Player player, int startRow, int startCol, char orient, int length){
		if(isOrientH(orient)){
			lookHorizontal(player, startRow, startCol, orient, length);
		}else if(isOrientV(orient)){
			lookVertical(player, startRow, startCol, orient, length);
		}
		if(offBoard || overlap){
			return false;
		}
		return true;
	}
	//look at the spaces the ship will occupy. Do they exist and are they unoccupied?
	//looking horizontally
	public void lookHorizontal(Player player, int startRow, int startCol, char orient, int length){
		for(int i=0; i<length; i++){
			try{
				if(!player.getDefBoard().getGrid()[startRow][startCol+i].equals("|__")){
					overlap=true;
					break;
				}
			}catch(Exception exc){
				offBoard=true;
				break;
			}
		}
	}
	//looking vertically
	public void lookVertical(Player player, int startRow, int startCol, char orient, int length){
		for(int i=0; i<length; i++){
			try{
				if(!player.getDefBoard().getGrid()[startRow+i][startCol].equals("|__")){
					overlap=true;
					break;
				}
			}catch(Exception exc){
				offBoard=true;
				break;
			}
		}
	}
	//set a ship of any length, anywhere, on either player's board
	public boolean setShipOnBoard(Player player, int startRow, int startCol, char orient, int length){
		offBoard=false;
		overlap=false;
		//placing the ships if they can be placed
		if(shipCanBePlaced(player, startRow, startCol, orient, length) &&  isOrientH(orient)){
			for(int i=0; i<length; i++){
				player.getDefBoard().getGrid()[startRow][startCol + i] = "| @";
			}
			return true;
		}else if(shipCanBePlaced(player, startRow, startCol, orient, length) && isOrientV(orient)){
			for(int i=0; i<length; i++){
				player.getDefBoard().getGrid()[startRow+i][startCol] = "| @";
			}
			return true;
		}else{	//if the user is trying to set a ship, print any warning messages for them
			if(player.equals(!player.getName().equals("computer"))){ 
				if(offBoard){
					System.out.println("The ship would go off the board. Try again.");
				}else if(overlap){
					System.out.println("The ship would overlap with another ship. Try again.");
				}
			}
			return false;
		}
	}
	//variables for parsing user firing input
	private String[] inputArr;
	private char inputRow, inputOrient;
	private int inputColumn;
	
	//try to separate the input from the user so the set<Ship>OnUserBoard() can use the info
	private void divideTheInput(String inputFromUser){
		try{
			inputArr = inputFromUser.split(" ");
			inputRow = inputArr[0].charAt(0);				//the letter (A-J)
			inputColumn = Integer.parseInt(inputArr[1]);	//the number (1-10)
			inputOrient = inputArr[2].charAt(0);			//(v or h)
		}catch(Exception exc){
			System.out.println("Your input was in an incorrect format. Try again.");
		}
	}

	//putting all of the ships on the user board and getting user input from the view
	public void setAllShipsOnUserBoard(BattleshipView view, Player user) {
		int i=5;
		boolean firstThree=true;
		//carrier
		for(int j=0;j<5;j++){
			do {				//while the ship hasn't been placed, get input and try to place the ship
				divideTheInput(view.getShipPlacement(i));
			}while(!setShipOnBoard(user, convertRowChar(inputRow), inputColumn, inputOrient, i));
			view.showTheBoard(user.getDefBoard().getGrid()); //show the updated view after successfully placing a ship
			if(i==3 && firstThree){//make sure to place a ship of size 3 twice
				i=4;
				firstThree=false;
			}//decrease the size of the ship to place
			i--;
		}
	}
	private int cpuPlaceRow, cpuPlaceColumn, cpuIntOrient;
	private char cpuPlaceOrient;
	public void getRandomCPUInput(){
		Random rand = new Random();
		cpuPlaceRow = rand.nextInt(10) + 1;
		cpuPlaceColumn = rand.nextInt(10) + 1;
		cpuIntOrient = rand.nextInt(2);
		if (cpuIntOrient == 1) {
			cpuPlaceOrient = 'V';
		} else {
			cpuPlaceOrient = 'H';
		}
	}
	
	//setting all of the CPU ships on the board in one method
	public void setAllShipsOnCPUBoard(Player cpu){
		boolean firstThree=true;
		int i=5;
		for(int j=0;j<5;j++){
			do{
				getRandomCPUInput();
			}while(!setShipOnBoard(cpu, cpuPlaceRow, cpuPlaceColumn, cpuPlaceOrient, i));
			if(i==3 && firstThree){//make sure to place a ship of size 3 twice
				i=4;
				firstThree=false;
			}//decrease the size of the ship to place
			i--;
		}
	}
	
	/* change user Defense board to mark the CPU hits and misses */
	public void updateUserDefenseBoard(Game game){
		for(int i=1; i<game.getUser().getDefBoard().getRows(); i++){
			for(int j=1; j<game.getUser().getDefBoard().getColumns(); j++){
				if(game.getCPU().getOffBoard().getSquare(i, j).equals("| O")){
					game.getUser().getDefBoard().setSquare(i, j, "| O");
				}
				if(game.getCPU().getOffBoard().getSquare(i, j).equals("| X")){
					game.getUser().getDefBoard().setSquare(i, j, "| X");
				}
			}
		}
	}
}