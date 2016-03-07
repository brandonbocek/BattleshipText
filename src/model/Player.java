package model;

public class Player {
	
	private String name = "You";
	//the defensive board has ships and the other player's guessed strikes
	//the offensive board has the player's strikes but no ships
	Board defBoard, offBoard;
	//the view will show the user's offensive and defensive boards
	
	private int shipSpacesToGet = 17;
	private boolean hasWon=false;
	
	
	public Player(){
		defBoard=new Board();
		offBoard=new Board();
	}
	
	public Board getDefBoard(){
		return defBoard;
	}
	public Board getOffBoard(){
		return offBoard;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	// get ship spaces to see is anyone won the game
	public int getShipSpacesLeftToGet() {
		return shipSpacesToGet;
	}

	// set the ship spaces to be 1 lower
	public void decreaseShipSpaces() {
		shipSpacesToGet--;
	}
	public void setWinStatus(boolean hasWon){
		this.hasWon=hasWon;
	}
	public boolean getWinStatus(){
		return hasWon;
	}

}
