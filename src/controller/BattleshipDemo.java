package controller;

import model.Game;
import view.BattleshipView;

public class BattleshipDemo {
	
	public static void main(String[] args){
		
		
		Game battleship = new Game();
		BattleshipView view = new BattleshipView();
		BattleshipController controller = new BattleshipController(battleship, view);
		
		controller.playGame();
		
		
		
		
	
		
	}
}
