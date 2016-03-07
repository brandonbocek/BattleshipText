package model;

public class Ship{


	private int length;
	private char orient;
	private boolean offBoard=false, overlap=false;
	
	//constructor for a ship with its length built into it
	public Ship(int length){
		this.length=length;
	}
	public boolean getOutOfBounds(){
		return offBoard;
	}
	public boolean getOverlap(){
		return overlap;
	}
	public void setOrient(char orient){
		this.orient=orient;
	}
	public char getOrient(){
		return orient;
	}
	public void setLength(int length){
		this.length=length;
	}
	public int getLength(){
		return length;
	}
}