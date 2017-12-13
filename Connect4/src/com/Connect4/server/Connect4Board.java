package com.Connect4.server;

public class Connect4Board {
	
    private int[][] boardArray;
    protected String boardSize;
    
	public Connect4Board(String boardSize) {
		this.boardSize = boardSize;
		System.out.println("Board is instantiated");
		if (boardSize.equals("standard")){
			boardArray = new int[7][6];
		} else if (boardSize.equals("epic")){
			boardArray  = new int[10][9];
		}
		setEmpty();
	}
	
	public boolean updateBoard(int column, int userColor){
		column = column - 1;
		for (int i = 0; i < boardArray[column].length; i++){
			if (boardArray[column][i] == 0){ //zero = empty tile
				boardArray[column][i] = userColor; //set it to the user's color
				printBoardToConsole();
				return true; //for having updated the board
			}
		}
		return false; //column was already filled up
	}
	
	private void printBoardToConsole(){
		for (int i = boardArray[0].length - 1; i >= 0; i--){
			for (int j = 0; j < boardArray.length; j++){ //for #columns
				System.out.print(boardArray[j][i]);
			}
			System.out.println();
		}

	}
	
	public boolean checkWinConditions(int column, int userColor){
		column = column - 1;
		if (checkVertical(column, userColor) || 
				checkHorizontal(column, userColor) || 
				checkDiagonal(column, userColor)){
			return true;
		}
		return false;
	}

	void setEmpty(){
		for (int i = 0; i < boardArray.length; i++){
			for (int j = 0; j < boardArray[i].length; j++){
				boardArray[i][j] = 0;
			}
		}
	}
	
	//validation methods to check win conditions below
	
	private boolean checkDiagonal(int column, int userColor) {
		int count = 0;
		int count2 = 0;
		int row = 0;
		for (int i = 0; i < boardArray[column].length; i++){
			if (boardArray[column][i] == userColor){
				row = i; //find most recently placed row
			}
		}
		System.out.println(column + " " + row);
		
		int min = Math.min(column, row);
		int x = column - min;
		int y = row - min;
		System.out.println(min + " " + x + " " + y);
		
		while (x < boardArray.length && y < boardArray[x].length){
			if (boardArray[x][y] == userColor){
				count++;
				System.out.println("x " + x + " y " + y + " " + count);
			}  else if (boardArray[x][y] != userColor && count < 4){
				count = 0;
			}
			x++;
			y++;
		}
		
		min = Math.min(boardArray.length - 1 - column, row);
		x = column + min;
		y = row - min;
		System.out.println(min + " " + x + " " + y);
		
		while (x >= 0 && y < boardArray[x].length){
			if (boardArray[x][y] == userColor){
				count2++;
				System.out.println("x " + x + " y " + y + " " + count2);
			} else if (boardArray[x][y] != userColor && count2 < 4){
				count2 = 0;
			}
			x--;
			y++;
		}
		
		if (count >= 4 || count2 >= 4){
			System.out.println("count " + count);
			System.out.println("count2 " + count2);
			return true;
		} else {
			return false;
		}
	}

	private boolean checkHorizontal(int column, int userColor) {
		int count = 0;
		int row = 0;
		for (int i = boardArray[column].length - 1; i >= 0; i--){
			if (boardArray[column][i] == userColor){
				row = i; //find most recently placed row
			}
		}
		
		for (int i = 0; i < boardArray.length; i++){
			if (boardArray[i][row] == userColor){
				count++;
			} else if (boardArray[i][row] != userColor && count < 4){
				count = 0;
			}
		}
		
		if (count >= 4){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkVertical(int column, int userColor) {
		int count = 0;
		for (int i = 0; i < boardArray[column].length; i++){
			if (boardArray[column][i] == userColor){
				count++;
			} else if (boardArray[column][i] != userColor && count < 4){
				count = 0;
			}
		}
		
		if (count >= 4){
			return true;
		} else {
			return false;
		}
	}
	
}
