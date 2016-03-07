package controller;
import java.util.Random;

import model.Board;
import model.Game;
import model.Player;
import view.BattleshipView;

public class BattleshipController {
	
	//business logic controllers to call in the game loop
	SetUpController setControl = new SetUpController();
	FireMissileController missControl = new FireMissileController();
	GameOverController gameOver = new GameOverController();
	
	//the model
	private Game game;
	//the view
	private BattleshipView view;
	
	//controller's constructor
	public BattleshipController(Game game, BattleshipView view){
		this.game=game;
		this.view=view;
	}
	
	public BattleshipView getBattleshipView(){
		return view;
	}
	public Game getBattleshipGame(){
		return game;
	}
	public Player getGameUser(){return game.getUser();}
	public Player getGameCPU(){return game.getCPU();}
	
	//getting and setting the user's name
	public String getUserName(){return getGameUser().getName();}
	public void setUserName(String name){getGameUser().setName(name);}
	
	//getting the offensive and defensive Boards from the players
	public Board getUserDefBoard(){return getGameUser().getDefBoard();}
	public Board getCPUDefBoard(){return getGameCPU().getDefBoard();}
	public Board getUserOffBoard(){return getGameUser().getOffBoard();}
	public Board getCPUOffBoard(){return getGameCPU().getOffBoard();}
	
	//getting the String array from each player's boards
	public String[][] getUserDefenseBoardArray() {return getUserDefBoard().getGrid();}
	public String[][] getCPUGameBoard() {return getCPUDefBoard().getGrid();}
	public String[][] getUserOffGameBoard() {return getUserOffBoard().getGrid();}
	public String[][] getCPUOffGameBoard() {return getCPUOffBoard().getGrid();}
	
	
	//called from the main method to play
	public void playGame(){
		updateInitialView();						//the blank board is set and shown
		setControl.setAllShipsOnUserBoard(view, getGameCPU());//gets input coordinates, sets each ship, and updates view
		setControl.setAllShipsOnCPUBoard(getGameUser());					//the cpu sets all their ships randomly
		//the game loop
		while(!gameOver.isGameOver(getBattleshipGame())){
			missControl.fireUserMissle(view, getGameUser(), getCPUDefBoard());
			missControl.fireCPUMissile(getGameCPU(), getGameUser());
			afterShotView();
		}
		declareAWinner(gameOver.getTheWinner(getBattleshipGame()));
	}
	
	/* methods called to the view */
	
	private void declareAWinner(Player winner){
		view.sayWhoWon(winner.getName());
	}
	//displayed after every shot
	private void afterShotView(){
		setControl.updateUserDefenseBoard(getBattleshipGame());
		view.showBothBoards(getUserOffGameBoard(), getUserDefenseBoardArray());
	}
	//updating the screen after all ships are set
	private void updateInitialView(){
		getGameCPU().setName("computer");
		setControl.setAllBlankBoards(getBattleshipGame());							//make the blank boards
		view.showTheBoard(getUserDefenseBoardArray());		//show the User's blank board before placement
	}
}
