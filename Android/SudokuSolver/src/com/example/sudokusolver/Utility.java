package com.example.sudokusolver;

import java.util.ArrayList;
import java.util.Collections;

//Utility functions to hash/access game state of each tile
public final class Utility {

    //Check if tile is solved (only one possibility remaining)
    //Left-most bit, 0 = not solved, 1 = solved
    public static boolean solved(int state) {
	if ((state & 0x80000000) != 0) {
	    return true;
	}
	return false;
    }

    //Check if only one possible answer remains for given tile
    public static boolean onePossibleRemaining(int state) {
	int possibility = state & 0x000001FF;
	if (possibility == 0x000001FE || possibility == 0x000001FD
	    || possibility == 0x000001FB || possibility == 0x000001F7
	    || possibility == 0x000001EF || possibility == 0x000001DF
	    || possibility == 0x000001BF || possibility == 0x0000017F
	    || possibility == 0x000000FF) {
	    return true;
	}
	return false;
    }

    //Get answer of this tile,
    // if solved bit = true, return answer
    // else find answer in possibility bits
    public static int getAnswer(int state) {
	if (solved(state)) {
	    return getNum(state);
	}
	int possibility = state & 0x000001FF;
	int ans;
	switch(possibility) {
	    case 0x000001FE:
		ans = 1;
		break;
	    case 0x000001FD:
	        ans = 2;
		break;
	    case 0x000001FB:
	        ans = 3;
		break;
	    case 0x000001F7:
	        ans = 4;
		break;
	    case 0x000001EF:
	        ans = 5;
		break;
	    case 0x000001DF:
	        ans = 6;
		break;
	    case 0x000001BF:
	        ans = 7;
		break;
	    case 0x0000017F:
	        ans = 8;
		break;
	    case 0x000000FF:
	        ans = 9;
		break;
	    default:
	        ans = 0;
		break;
	}
	return ans;
    }

    //Set answer to argument and solved bit to true
    //Return new hashed state
    public static int setAnswer(int state, int num) {
	if (num > 9 || num <= 0) {
	    return -1;
	}
	return ((num << 27) | state) | 0x80000000;
    }

    //return number at this tile if solved.
    //returns 0 if not solved
    //bits 2-5 from left side.
    public static int getNum(int state) {
	if (!solved(state)) {
	    return -1;
	}
	int ans = (state >>> 27) & 0x0000000F;
	if (ans > 9 || ans <= 0) {
	    return -1;
	}
	return ans;
    }

    //Return a list of possible numbers for this position
    public static ArrayList<Integer> getPossibles(int state) {
	int possibility = state & 0x000001FF;
	int num = 0;
	ArrayList<Integer> ans = new ArrayList<Integer>();
	while (num < 9) {
	    if ((possibility & (1 << num)) == 0) {
		ans.add(num + 1);
	    }
	    num++;
	}
	return ans;
    }

    //Return which box the tile exist in given the row and column
    public static int getBoxNum(int row, int column) {
	int width = 3;
	int boxRow = row / 3;
	int boxColumn = column / 3;
	return boxRow * width + boxColumn;
    }

    //return number of possible answers at this position
    public static int getNumPossible(int state) {
	int possibility = state & 0x000001FF;
	int ans = 0;
	for (int num = 0; num < 9; num++) {
	    if ((possibility & (1 << num)) == 0) {
		ans++;
	    }
	}
	return ans;
    }

    //Sets the row/column of this position
    //Row = bits 10-13 from right
    //Column = bits 14-17 from right
    public static int setCoor(int state, int row, int column) {
	if (row > 9 || row < 0 || column > 9 || column < 0) {
	    return 0;
	}
	return state | (row << 9) | (column << 13);
    }

    //USE ONLY IN SEARCH
    //Return the row number of this position
    public static int getRow(int state) {
	int ans = (state >>> 9) & 0x0000000F;
	if (ans > 8 || ans < 0) {
	    return -1;
	}
	return ans;
    }

    //USE ONLY IN SEARCH
    //Return the column number of this position
    public static int getColumn(int state) {
	int ans = (state >>> 13) & 0x0000000F;
	if (ans > 8 || ans < 0) {
	    return -1;
	}
	return ans;
    }
    
    public static void printBoard(SudokuBoard sb) {
    	int[][] board = sb.getGameBoard();
    	for (int row = 0; row < 9; row++) {
    	    String line = "";
    	    if (row != 0 && row % 3 == 0) {
    		System.out.println("--------------------");
    	    }
    	    for (int column = 0; column < 9; column++) {
    		if (column == 0) {
    		    line += "|";
    		}
    		if (column != 0 && column % 3 == 0) {
    		    line += "|";
    		}
    		int ans = Utility.getAnswer(board[row][column]);
    		if (ans == 0) {
    		    line += " |";
    		} else {
    		    line += ans + "|";
    		}
    	    }
    	    System.out.println(line);
    	}
        }
}