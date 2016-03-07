package view;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class BattleshipView {

	Scanner input = new Scanner(System.in);
	
	//showing the user's board with the ships and the CPU's shots (eventually)
	public void showTheBoard(String[][] theGrid){
		System.out.println("__________________________________\n");
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				System.out.print(theGrid[i][j]);
			}
			System.out.println();
		}
		System.out.println("__________________________________\n");
	}
	public void showBothBoards(String[][] off, String[][] def){
		System.out.println("         Your Attacks                                                           Your Ships\n");
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				System.out.print(off[i][j]);
			}
			System.out.print("                                      ");
			for(int j=0; j<11; j++){
				System.out.print(def[i][j]);
			}
			System.out.println();
		}
		System.out.println("________________________________________________________________________________________________________________\n");
	}
	
	//gets the coordinates and orientation for the placement of a ship
	//this should later be changed so that the user enters a char for the row
	public String getShipPlacement(int length){
		System.out.println("Please enter your row, column, and orienation for a ship of size "+length);
		System.out.println("Example would be (D 4 h) or (f 10 V): ");
		return input.nextLine();
	}
	//returns the firing coordinates from the user
	public String getFiringCoordinates(){
		System.out.println("Where do you want to fire?");
		System.out.println("Example coordinates would be (a 7) or (G 5):");
		try{
			return input.nextLine();
		}catch(NoSuchElementException exc){
			return "failure";
		}
	}
	public void sayWhoWon(String name){
		System.out.println("Game Over!");
		System.out.println("The winner is "+ name+"!");
	}
}
