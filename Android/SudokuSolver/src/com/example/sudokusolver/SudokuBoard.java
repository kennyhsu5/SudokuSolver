package com.example.sudokusolver;

//Class to store states of sudoku
public class SudokuBoard {
    //Holds the current game board
    private int[][] gameBoard;
    private int numSolved;

    //Game states optimized for read
    //Shows eliminated numbers in each category
    //Horizontal rows, from top to bottom
    //Vertical columns, from left to right
    //Box of 9 tiles, left to right, top to bottom
    private int[] rows, columns, boxes;
    
    //Constructor for sudoku board
    public SudokuBoard() {
		gameBoard = new int[9][9];
		rows = new int[9];
		columns = new int[9];
		boxes = new int[9];
		numSolved = 0;
    }

    //Set a tile to a certain value
    public void setTile(int row, int column, int num) {
		if (num > 9 || num <= 0) {
		    return;
		}
		//Set actual gameboard
		int value = Utility.setAnswer(gameBoard[row][column], num);
		gameBoard[row][column] = value;
	
		//Set extra gameboards, optimized for read
		int mask = 1 << (num - 1);
		rows[row] |= mask;
		columns[column] |= mask;
		boxes[Utility.getBoxNum(row, column)] |= mask;
		numSolved++;
    }

    //Set corresponding variables
    public void setRows(int[] newRows) {
		rows = newRows;
    }
    public void setColumns(int[] newColumns) {
    	columns = newColumns;
    }
    public void setBoxes(int[] newBoxes) {
    	boxes = newBoxes;
    }
    public void setBoard(int[][] newBoard) {
    	gameBoard = newBoard;
    }

    //Getter methods
    public int[] getRows() {
    	return rows;
    }
    public int[] getColumns() {
    	return columns;
    }
    public int[] getBoxes() {
    	return boxes;
    }
    public int[][] getGameBoard() {
    	return gameBoard;
    }
    public int getNumSolved() {
    	return numSolved;
    }
}