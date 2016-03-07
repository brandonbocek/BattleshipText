package controller;

import model.Game;
import model.Player;

public class GameOverController {

	public boolean isGameOver(Game battleship){		//check for a winner
		if(battleship.getUser().getShipSpacesLeftToGet()<=0){
			return true;
		}else if(battleship.getCPU().getShipSpacesLeftToGet()<=0){
			return true;
		}else{
			return false;
		}
	}
	public Player getTheWinner(Game battleship){
		if(battleship.getUser().getShipSpacesLeftToGet()<=0){
			return battleship.getUser();
		}else{
			return battleship.getCPU();
		}
	}
}
