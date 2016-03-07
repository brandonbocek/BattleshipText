package controller;

public class ConvertInputController {

	//used by FireMissile and SetUp Controllers
	//converts the row's character to the correct integer
	protected int convertRowChar(char c){
		switch(c){
			case 'A':case 'a': return 1;
			case 'B':case 'b': return 2;
			case 'C':case 'c': return 3;
			case 'D':case 'd': return 4;
			case 'E':case 'e': return 5;
			case 'F':case 'f': return 6;
			case 'G':case 'g': return 7;
			case 'H':case 'h': return 8;
			case 'I':case 'i': return 9;
			case 'J':case 'j': return 10;
			default: return -1; 
		}
	}
}
