package com.example.sudokusolver;

import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;

// Methods/algorithms to solve sudoku
public final class SudokuSolver {

    // Sudoku constants
    final static int ANSWERS_TO_WIN = 81;
    final static int WIDTH = 9;
    final static int HEIGHT = 9;
    final static int ROW = 1;
    final static int COLUMN = 2;
    final static int BOX = 3;

    // Runs the algorithm to solve the sudoku
    // Returns true if answer found, false if 
    // no solution is found. Answer stored in sudokuboard
    public static boolean solve(SudokuBoard board, int numOfAnswers) {
	int oldNumOfAnswers = -1;
	while (numOfAnswers != oldNumOfAnswers) {
	    oldNumOfAnswers = numOfAnswers;
	    numOfAnswers = eliminatePossibilities(board, numOfAnswers);
	    if (numOfAnswers == ANSWERS_TO_WIN) {
		return true;
	    }
	    numOfAnswers = eliminateNeighbors(board, numOfAnswers);
	    if (numOfAnswers == ANSWERS_TO_WIN) {
		return true;
	    }
	}
	if (search(board, numOfAnswers)) {
	    return true;
	}
	return false;
    }

    // Loop through the current board to eliminate possibile
    // answers at a given tile that violates rules of sudoku. 
    // Limits search space for later algorithms
    // Return number of answers solved so far
    public static int eliminatePossibilities(SudokuBoard board,
					      int numOfAnswers) {
	// Get initial game states
	int[][] sudoku = board.getGameBoard();
	int[] rows = board.getRows();
	int[] columns = board.getColumns();
	int[] boxes = board.getBoxes();
	
	// Initiate number of new answers to -1 for first iteration
	int numOfNewAnswers = -1;

	// Continue to eliminate until
	// 1) no new answers are found OR
	// 2) sudoku solved
	while (numOfAnswers != ANSWERS_TO_WIN && numOfNewAnswers != 0) {
	    numOfNewAnswers = 0;

	    // Loop through gameboard to eliminate possibility
	    // for each tile
	    for (int row = 0; row < HEIGHT; row++) {
		for (int column = 0; column < WIDTH; column++) {
		    int hashedState = sudoku[row][column];
		    if (!Utility.solved(hashedState)) {
			int boxNum = Utility.getBoxNum(row, column);

			// Eliminate posibilities in terms of numbers
			// Already in this row/column/box
			hashedState |= rows[row];
			hashedState |= columns[column];
			hashedState |= boxes[boxNum];

			int newHashedState = hashedState;

			// If only one possibility remains, then that is
			// the answer for this tile.
			// Write to rows/columns/boxes and hashedState
			if (Utility.onePossibleRemaining(hashedState)) {
			    int answer = Utility.getAnswer(hashedState);
			    int mask = 1 << (answer - 1);
			    newHashedState = Utility.setAnswer(hashedState,
								  answer);
			    rows[row] |= mask;
			    columns[column] |= mask;
			    boxes[boxNum] |= mask;
			    numOfNewAnswers++;
			}
			sudoku[row][column] = newHashedState;
		    }
		}
	    }
	    numOfAnswers += numOfNewAnswers;
	}

	//Store states back to SudokuBoard
	board.setBoard(sudoku);
	board.setRows(rows);
	board.setColumns(columns);
	board.setBoxes(boxes);
	return numOfAnswers;
    }

    //Compare with neighbors in row/column/box to deduct answers
    public static int eliminateNeighbors(SudokuBoard sb, int numOfAnswers) {
	//Load states for use
	int[][] sudoku = sb.getGameBoard();
	int[] rows = sb.getRows();
	int[] columns = sb.getColumns();
	int[] boxes = sb.getBoxes();

	//Loop through entire board, if unsolved check with neighbors
	for (int row = 0; row < HEIGHT; row++) {
	    for (int column = 0; column < WIDTH; column++) {
		int state = sudoku[row][column];
		if (!Utility.solved(state)) {
		    ArrayList<Integer> possibilityList;
		    int newHash;
		    int possibilityBits = state & 0x000001FF;

		    //Within the same row
		    possibilityList = getUnsolvedNeighbors(row, column, ROW, sudoku);
		    newHash = eliminateFromNeighbor(possibilityBits,
						    possibilityList);
		    if (Utility.solved(newHash)) {
			int ans = Utility.getAnswer(newHash);
			int mask = 1 << (ans - 1);
			int boxNum = Utility.getBoxNum(row, column);
			sudoku[row][column] = newHash;
			rows[row] |= mask;
			columns[column] |= mask;
			boxes[boxNum] |= mask;	
			sb.setBoard(sudoku);
			sb.setRows(rows);
			sb.setColumns(columns);
			sb.setBoxes(boxes);
			numOfAnswers = eliminatePossibilities(sb,
							      numOfAnswers + 1);
			sudoku = sb.getGameBoard();
			rows = sb.getRows();
			columns = sb.getColumns();
			boxes = sb.getBoxes();
			break;
		    }

		    //Within the same column
		    possibilityList = getUnsolvedNeighbors(row, column,
							   COLUMN, sudoku);

		    newHash = eliminateFromNeighbor(possibilityBits,
						    possibilityList);
		    if (Utility.solved(newHash)) {
			int ans = Utility.getAnswer(newHash);
			int mask = 1 << (ans - 1);
			int boxNum = Utility.getBoxNum(row, column);
			sudoku[row][column] = newHash;
			rows[row] |= mask;
			columns[column] |= mask;
			boxes[boxNum] |= mask;	
			sb.setBoard(sudoku);
			sb.setRows(rows);
			sb.setColumns(columns);
			sb.setBoxes(boxes);
			numOfAnswers = eliminatePossibilities(sb,
							      numOfAnswers + 1);
			sudoku = sb.getGameBoard();
			rows = sb.getRows();
			columns = sb.getColumns();
			boxes = sb.getBoxes();
			break;
		    }

		    //Within same box
		    possibilityList = getUnsolvedNeighbors(row, column, BOX, sudoku);

		    newHash = eliminateFromNeighbor(possibilityBits,
						    possibilityList);
		    if (Utility.solved(newHash)) {
			int ans = Utility.getAnswer(newHash);
			int mask = 1 << (ans - 1);
			int boxNum = Utility.getBoxNum(row, column);
			sudoku[row][column] = newHash;
			rows[row] |= mask;
			columns[column] |= mask;
			boxes[boxNum] |= mask;
			sb.setBoard(sudoku);
			sb.setRows(rows);
			sb.setColumns(columns);
			sb.setBoxes(boxes);
			numOfAnswers = eliminatePossibilities(sb,
							      numOfAnswers + 1);
			sudoku = sb.getGameBoard();
			rows = sb.getRows();
			columns = sb.getColumns();
			boxes = sb.getBoxes();
			break;
		    }
		}
	    }
	}

	//Save states back to SudokuBoard
	sb.setBoard(sudoku);
	sb.setRows(rows);
	sb.setColumns(columns);
	sb.setBoxes(boxes);
	return numOfAnswers;
    }

    //Assign answer given the set of possible values at neighboring positions.
    //If this posistion is the only possible one to hold an answer in its
    //row/column/box, assign the answer.
    public static int eliminateFromNeighbor(int possibleNums,
					     ArrayList<Integer> possibleList) {
	ArrayList<Integer> possibleNumsArray = Utility.getPossibles(possibleNums);
	int index = 0;
	
	while (!possibleNumsArray.isEmpty() && index < possibleList.size()) {
	    int dif = possibleNums ^ possibleList.get(index);
	    ArrayList<Integer> temp = new ArrayList<Integer>();
	    for (int i = 0; i < possibleNumsArray.size(); i++) {
		int num = possibleNumsArray.get(i);
		if ((dif & (1 << (num - 1))) != 0) {
		    temp.add(num);
		}
	    }
	    possibleNumsArray = temp;
	    index++;
	}
	if (possibleNumsArray.size() == 1) {
	    int ans = Utility.setAnswer(0, possibleNumsArray.get(0));
	    return ans;
	}
	return 0;
    }

    //Return a list of states of unsolved neighbors in row/column/box
    public static ArrayList<Integer> getUnsolvedNeighbors(int row, int column,
							  int type, int[][] sudoku) {
	ArrayList<Integer> possibilityList = new ArrayList<Integer>();
	if (type == ROW) {
	    for (int i = 0; i < WIDTH; i++) {
		if (i != column) {
		    int val = sudoku[row][i];
		    if (!Utility.solved(val)) {
			possibilityList.add(val);
		    }
		}
	    }
	} else if (type == COLUMN) {
	    for (int i = 0; i < HEIGHT; i++) {
		if (i != row) {
		    int val = sudoku[i][column];
		    if (!Utility.solved(val)) {
			possibilityList.add(val);
		    }
		}
	    }
	} else if (type == BOX) {
	    for (int r = (row / 3) * 3; r < 3; r++) {
		for (int c = (column / 3) * 3; c < 3; c++) {
		    if (r != row || c != column) {
			int val = sudoku[r][c];
			if (!Utility.solved(val)) {
			    possibilityList.add(val);
			}
		    }
		}
	    }
	} else {
	    possibilityList = null;
	}
	return possibilityList;
    }

    public static boolean search(SudokuBoard sb, int numOfAnswer) {
	int[][] sudoku = sb.getGameBoard();
	HashMap<Integer, ArrayList<Integer>> sortTemp
	    = new HashMap<Integer, ArrayList<Integer>>();
	Stack<Integer> unsolved = new Stack<Integer>();

	//Sorts all unsolved positions based on number of possible answers
	//Loops through board, add into hashmap based on # of possible answers
	for (int r = 0; r < HEIGHT; r++) {
	    for (int c = 0; c < WIDTH; c++) {
		int state = sudoku[r][c];
		if (!Utility.solved(state)) {
		    int newState = Utility.setCoor(state, r, c);
		    int numPossible = Utility.getNumPossible(newState);
		    if (!sortTemp.containsKey(numPossible)) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(newState);
			sortTemp.put(numPossible, temp);
		    } else {
			sortTemp.get(numPossible).add(newState);
		    }
		}
	    }
	}

	for (int i = 9; i > 0; i--) {
	    if (sortTemp.containsKey(i)) {
		ArrayList<Integer> temp = sortTemp.get(i);
		for (int k = 0; k < temp.size(); k++) {
		    unsolved.push(temp.get(k));
		}
	    }
	}

	//long startTime = System.currentTimeMillis();

	//Executes backtracking search
	Stack<Integer> answer = backTrackSearch(unsolved, new Stack<Integer>(),
						sb.getRows(), sb.getColumns(),
						sb.getBoxes());
	//long endTime = System.currentTimeMillis();

	//System.out.println("BackTrackSearch time: "
	//		   + (endTime-startTime) + "ms");

	if (answer != null) {
	    //Add answers back ot board
	    while (!answer.empty()) {
		int value = answer.pop();
		int r = Utility.getRow(value);
		int c = Utility.getColumn(value);
		sudoku[r][c] = value;
		numOfAnswer++;
	    }
	    if (numOfAnswer == ANSWERS_TO_WIN) {
		sb.setBoard(sudoku);
		return true;
	    }
	}
	return false;
    }
    
    //Backtrack search to solve harder problems
    public static Stack<Integer> backTrackSearch(Stack<Integer> unsolved,
						     Stack<Integer> solved,
						     int[] rows, int[] columns,
						     int[] boxes) {
	if (unsolved.isEmpty()) {
	    return solved;
	}
	int currVal = unsolved.pop();
	int r = Utility.getRow(currVal);
	int c = Utility.getColumn(currVal);
	int boxNum = Utility.getBoxNum(r, c);
	int oldRow = rows[r];
	int oldColumn = columns[c];
	int oldBox = boxes[Utility.getBoxNum(r, c)];
	ArrayList<Integer> possible
	    = Utility.getPossibles(currVal | oldRow | oldColumn | oldBox);
	for (int i = 0; i < possible.size(); i++) {
	    int possibleAns = possible.get(i);
	    int temp = Utility.setAnswer(Utility.setCoor(0, r, c), possibleAns);
	    int mask = 1 << (possibleAns - 1);

	    solved.add(temp);
	    rows[r] |= mask;
	    columns[c] |= mask;
	    boxes[boxNum] |= mask;

	    Stack<Integer> newSolved
		= backTrackSearch(unsolved, solved, rows, columns, boxes);
	    if (newSolved != null) {
		return newSolved;
	    }
	    
	    solved.remove(solved.size() - 1);
	    rows[r] = oldRow;
	    columns[c] = oldColumn;
	    boxes[boxNum] = oldBox;
	}
	unsolved.push(currVal);
	return null;
    }
}