package model;

public class Game {

	private Player user;
	private Player cpu;
	private boolean isGameOver=false;
	
	public Game(){
		user=new Player();
		cpu=new Player();
	}
	
	public Player getUser(){
		return user;
	}
	public Player getCPU(){
		return cpu;
	}
	public void setGameOver(boolean status){
		isGameOver=status;
	}
	public boolean getGameStatus(){
		return isGameOver;
	}	
}
