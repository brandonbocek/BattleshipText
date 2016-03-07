package controller;

import java.util.Random;

import model.Board;
import model.Player;
import view.BattleshipView;

public class FireMissileController extends ConvertInputController{
	
	char fireCharRow;
	int fireColumn;
	String[] firingInputArr;
	
	//the user firing a missile
	public void fireUserMissle(BattleshipView view, Player user, Board cpuBoard){
		do{		//while firing input isn't compatible with the format, ask for firing input
			while(!divideTheFiringInput(view.getFiringCoordinates())){
				System.out.println("Your firing input was wrong. Try again.");
			}	//while the space can't be marked, ask for firing input
		}while(!markOffBoard(convertRowChar(fireCharRow), fireColumn, user, cpuBoard));
	}
	//The firing input is divided, if not returns false
	public boolean divideTheFiringInput(String firingInput){
		try{
			firingInputArr = firingInput.split(" ");
			fireCharRow = firingInputArr[0].charAt(0);
			fireColumn = Integer.parseInt(firingInputArr[1]);
			return true;
		}catch(Exception exc){
			return false;
		}
	}
	
	//class variables for shooting missiles AI
		private boolean pursuingLine=false, singleStrikeWasHit=false, twoHitNeighbor=false;
		private int randSearchFails=0, searchStandard=5, fixedRow, fixedColumn, advRow, advColumn;
		private int markedRow, markedColumn;
		
		//the CPU firing AI methods encapsulated into this method
		public void fireCPUMissile(Player cpu, Player user){
			boolean isShotFired = false;
			Random rand = new Random();
			int r=1, c=1;
			//while a shot hasn't been fired, try to fire a shot
			while(!isShotFired){
				r=rand.nextInt(10)+1;
				c=rand.nextInt(10)+1;
				if(pursuingLine){	//shooting down the line of 2 consecutive hits
					isShotFired = fireOnLine(cpu, user);
				}else if(singleStrikeWasHit){	//searching vertically and horizontally from the random hit
					isShotFired=fireAroundLastShot(fixedRow, fixedColumn, cpu, user);
				}else if(twoHitNeighbor){ //if a space exists that borders 2 hits either right, left, up, or down
					isShotFired=fireAtTwoHitsNeighbor(cpu, user);
				}else if(!pursuingLine && randSearchFails<searchStandard){//fire semi-randomly
					isShotFired=fireCPUSemiRandomly(r,c, cpu, user);
				}else {												//fire randomly (last resort)
					isShotFired=fireCPURandomly(r, c, cpu, user);
				}
			}
			//potentially changing attack strategy
			checkTheMarkForStrategyChange(r, c, cpu);
		}

		private void checkTheMarkForStrategyChange(int row, int column, Player cpu) {
			// if the CPU gets a hit on a random shot, saves the row and column of
			// that hit for the strategy next turn
			if (!pursuingLine && !singleStrikeWasHit && cpu.getOffBoard().getSquare(row, column).equals("| X")) {
				singleStrikeWasHit = true;
				fixedRow = row;
				fixedColumn = column;
			}
			if(!pursuingLine && !singleStrikeWasHit && (cpu.getShipSpacesLeftToGet()<16)){
				twoHitNeighbor=true; //check for squares bordering 2 strikes before firing randomly
			}
			// if the line the CPU is pursuing, didn't get a hit this time
			if (pursuingLine && cpu.getOffBoard().getSquare(markedRow, markedColumn).equals("| O")) {
				// if the CPU can't reverse the line, stop using pursue the line strategy
				pursuingLine = tryToReverseTheLine(cpu);
			}
		}
		//returns false if the line of attack could not be reversed
		public boolean tryToReverseTheLine(Player cpu){
			if(fixedRow==advRow){//reversing the column path
				if((fixedColumn>advColumn) && (fixedColumn+1 < 11)){
					advColumn = fixedColumn+1;
					if(cpu.getOffBoard().getSquare(advRow, advColumn).equals("| O")){//if other side of line is already a miss
						return false;
					}
				}else if(fixedColumn<advColumn && (fixedColumn-1 > 0)){
					advColumn = fixedColumn-1;
					if(cpu.getOffBoard().getSquare(advRow, advColumn).equals("| O")){
						return false;
					}
				}else{
					return false;
				}
			}else if(fixedColumn==advColumn){//reversing the row path
				if(fixedRow>advRow && (fixedRow+1 < 11)){
					advRow = fixedRow+1;
					if(cpu.getOffBoard().getSquare(advRow, advColumn).equals("| O")){
						return false;
					}
				}else if(fixedRow<advRow && (fixedRow-1 > 0)){
					advRow = fixedRow-1;
					if(cpu.getOffBoard().getSquare(advRow, advColumn).equals("| O")){
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
			return true;
		}
		
		//returns true if a space was marked for the turn
		public boolean fireCPUSemiRandomly(int r, int c, Player cpu, Player user){//firing randomly with some limitations
			if(cpu.getOffBoard().getSquare(r, c).equals("|__")){//if the space is available
				if(!isStrikeNextToSquare(r, c, cpu)){//if the space isn't touching another strike vertically or horizontally
					//this method being called here should always return true!
					markOffBoard(r, c, cpu, user.getDefBoard());//marks the space hit or miss on the CPU's offensive board
					return true;
				}else{
					randSearchFails++;
					return false;
				}
			}else{//the randomly generated space is already occupied by a strike
				return false;
			}
		}
		public boolean fireCPURandomly(int r, int c, Player cpu, Player user){//firing totally randomly, returns true if successfully marked
			if(cpu.getOffBoard().getSquare(r, c).equals("|__")){//if the space is available
				//this method being called here should always return true!
				markOffBoard(r, c, cpu, user.getDefBoard());//marks the space hit or miss on the CPU's offensive board
				return true;
			}else{//the randomly generated space is already occupied by a strike
				return false;
			}
		}
		//called when a single shot was a strike
		public boolean fireAroundLastShot(int r, int c, Player cpu, Player user){//returns true if searched shot was fired
			Random rand = new Random();
			int square=rand.nextInt(4)+1;
			switch(square){//change either the row or column by 1 and set the advancing row and column for a potential pursuing line strategy
				case 1:r+=1;advRow=r;advColumn=c;break;
				case 2:r-=1;advRow=r;advColumn=c;break;
				case 3:c+=1;advColumn=c;advRow=r;break;
				case 4:c-=1;advColumn=c;advRow=r;break;
			}
			if(cpu.getOffBoard().getSquare(r, c).equals("|__")){//if the square is open
				markOffBoard(r, c, cpu, user.getDefBoard());//mark the square
				if(cpu.getOffBoard().getSquare(r, c).equals("| X")){//if the mark was a hit (this would be the second hit in the line)
					pursuingLine=true;//going to the line strategy next round
					singleStrikeWasHit=false;//making sure the new strategy is used next round
				}
				return true;
			}else{
				return false;
			}
		}
		//returns true if a strike is next to the square, diagonal doesn't matter
		private boolean isStrikeNextToSquare(int r, int c, Player cpu){
			if(cpu.getOffBoard().getSquare(r+1, c).equals("| X")||cpu.getOffBoard().getSquare(r+1, c).equals("| O")){
				return true;
			}else if(cpu.getOffBoard().getSquare(r-1, c).equals("| X")||cpu.getOffBoard().getSquare(r-1, c).equals("| O")){
				return true;
			}else if(cpu.getOffBoard().getSquare(r, c+1).equals("| X")||cpu.getOffBoard().getSquare(r, c+1).equals("| O")){
				return true;
			}else if(cpu.getOffBoard().getSquare(r, c-1).equals("| X")||cpu.getOffBoard().getSquare(r, c-1).equals("| O")){
				return true;
			}else{
				return false;
			}
		}
		
		//returns true if a strike was fired successfully
		public boolean fireOnLine(Player cpu, Player user){
			//save the stationary rows and columns for a potential line reversal later
			markedColumn=fixedColumn;
			markedRow=fixedRow;
			/* VERTICAL LINE PATH STRATEGY */
			if(fixedRow < advRow){//if the path is vertical and moving down
				return findNextMovingDown(cpu, user);
			}else if(fixedRow > advRow){//if the path is vertical and moving up
				return findNextMovingUp(cpu, user);
				
			/*  HORIZONTAL LINE PATH STRATEGY */ 	
			}else if(fixedColumn < advColumn){//if the path is horizontal and moving right
				return findNextMovingRight(cpu, user);
			}else if(fixedColumn > advColumn){//if the path is horizontal and moving left
				return findNextMovingLeft(cpu, user);
			}else{//So this method was called and there isn't a line to pursue? This should never happen.
				pursuingLine=false;
				return false;
			}
		}
		//these next 4 methods return true if they could mark a blank square next on the line of fire
		private boolean findNextMovingDown(Player cpu, Player user){
			if(cpu.getOffBoard().getSquare(advRow+1, fixedColumn).equals("|__")){//if one below on the column is empty, mark that square
				markedRow=advRow+=1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else if(cpu.getOffBoard().getSquare(fixedRow-1, fixedColumn).equals("|__")){//if the square below isn't open, try to mark above
				markedRow=advRow=fixedRow-1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else{//no empty squares on either side, abandon the strategy
				pursuingLine=false;
				return false;
			}
		}
		private boolean findNextMovingUp(Player cpu, Player user){
			if(cpu.getOffBoard().getSquare(advRow-1, fixedColumn).equals("|__")){//if one above on the column is empty, mark that square
				markedRow=advRow-=1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else if(cpu.getOffBoard().getSquare(fixedRow+1, fixedColumn).equals("|__")){//if the square below isn't open, try to mark below
				markedRow=advRow=fixedRow+1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else{//no empty squares on either side, abandon the strategy
				pursuingLine=false;
				return false;
			}
		}
		private boolean findNextMovingRight(Player cpu, Player user){
			if(cpu.getOffBoard().getSquare(fixedRow, advColumn+1).equals("|__")){//if one right on the row is empty, mark that square
				markedColumn=advColumn+=1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else if(cpu.getOffBoard().getSquare(fixedRow, fixedColumn-1).equals("|__")){//if the square right isn't open, try to mark left
				markedColumn=advColumn=fixedColumn-1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else{//no empty squares on either side, abandon the strategy
				pursuingLine=false;
				return false;
			}
		}
		private boolean findNextMovingLeft(Player cpu, Player user){
			if(cpu.getOffBoard().getSquare(fixedRow, advColumn-1).equals("|__")){//if one left on the row is empty, mark that square
				markedColumn=advColumn-=1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else if(cpu.getOffBoard().getSquare(fixedRow, fixedColumn+1).equals("|__")){//if the square left isn't open, try to mark right
				markedColumn=advColumn=fixedColumn+1;
				markOffBoard(markedRow, markedColumn, cpu, user.getDefBoard());
				return true;
			}else{//no empty squares on either side, abandon the strategy
				pursuingLine=false;
				return false;
			}
		}
		
		//returns true if a square neighboring 2 hits is marked
		private boolean fireAtTwoHitsNeighbor(Player cpu, Player user){
			int hits=0;
			for(int i=1; i<=10; i++){//search all 100 squares
				for(int j=1; j<=10; j++){
					if(cpu.getOffBoard().getSquare(i, j).equals("|__")){//if the square is available to fire at
						if(cpu.getOffBoard().getSquare(i-1, j).equals("| X")){//check 1 higher for a hit
							hits++;
						}
						if(cpu.getOffBoard().getSquare(i+1, j).equals("| X")){//check 1 lower for a hit
							hits++;
						}
						if(cpu.getOffBoard().getSquare(i, j-1).equals("| X")){//check 1 to the left for a hit
							hits++;
						}
						if(cpu.getOffBoard().getSquare(i, j+1).equals("| X")){//check 1 to the right for a hit
							hits++;
						}
						if(hits>=2){										//if 2 or more hits
							markOffBoard(i, j, cpu, user.getDefBoard()); //mark the square neighboring 2 hits
							return true;
						}
					}
				}
			}
			return false;
		}
		
		//the offensive player fires at the defensive player's board and returns true and marks X or O if successful
		private boolean markOffBoard(int row, int column, Player offPlayer, Board defBoard){
			if(!isBadSquare(row, column, defBoard)){//does the square exist and not been fired at already?
				if(squareHasShip(row, column, defBoard)){//is the square occupied by an opponent's ship?
					offPlayer.getOffBoard().setSquare(row, column, "| X");//mark the player's offensive board with X on that space
					offPlayer.decreaseShipSpaces();						//decrease the player's ships to get by 1
					return true;
				}else{
					offPlayer.getOffBoard().setSquare(row, column, "| O");//mark the player's offensive board with O on that space
					return true;
				}
			}else{
				return false;
			}
		}
		//returns true if the square can't be fired at (doesn't exist or is a border label square)
		private boolean isBadSquare(int row, int column, Board board){
			if(board.getSquare(row, column).equals("|__")){
				return false;
			}else if(board.getSquare(row, column).equals("| @")){
				return false;
			}else{
				return true;
			}
		}
		//returns true if the square is occupied by a ship
		private boolean squareHasShip(int row, int column, Board board){
			if(board.getSquare(row, column).equals("| @")){
				return true;
			}else{
				return false;
			}
		}
}
