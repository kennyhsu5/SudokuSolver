package com.example.sudokusolver;

import java.util.ArrayList;

/*Test methods in sudoku solver */
public class Test {

    //Main method, executes tests
    public static void main(String[] args) {
	//Test Utils helper methods
	test("solved1", testSolved1());
	test("solved2", testSolved2());
	test("solved3", testSolved3());
	test("solved4", testSolved4());
	test("onePossibleRemaining1", testOPR1());
	test("onePossibleRemaining2", testOPR2());
	test("onePossibleRemaining3", testOPR3());
	test("onePossibleRemaining4", testOPR4());
	test("onePossibleRemaining5", testOPR5());
	test("onePossibleRemaining6", testOPR6());
	test("onePossibleRemaining7", testOPR7());
	test("onePossibleRemaining8", testOPR8());
	test("onePossibleRemaining9", testOPR9());
	test("getAnswer1", testGA1());
	test("getAnswer2", testGA2());
	test("getNum1", testGN1());
	test("getNum2", testGN2());
	test("getNum3", testGN3());
	test("getNum4", testGN4());
	test("getNum5", testGN5());
	test("setAnswer1", testSA1());
	test("setAnswer2", testSA2());
	test("setAnswer3", testSA3());
	test("getBoxNum1", testBN1());
	test("getBoxNum2", testBN2());
	test("getBoxNum3", testBN3());
	test("getBoxNum4", testBN4());
	test("getBoxNum5", testBN5());
	test("getNumPossible1", testGNP1());
	test("getNumPossible2", testGNP2());
	test("getNumPossible3", testGNP3());
	test("getNumPossible4", testGNP4());
	test("setCoordinate1", testSC1());
	test("setCoordinate2", testSC2());
	test("setCoordinate3", testSC3());
	test("setCoordinate4", testSC4());
	test("setCoordinate5", testSC5());
	test("getRow1", testGR1());
	test("getRow2", testGR2());
	test("getRow3", testGR3());
	test("getRow4", testGR4());
	test("getRow5", testGR5());
	test("getColumn1", testGC1());
	test("getColumn1", testGC2());
	test("getColumn1", testGC3());
	test("getColumn1", testGC4());

	//Test SudokuBoard setTiles
	test("setTiles1", testST1());
	test("setTiles2", testST2());
	test("setTiles3", testST3());

	//Test SudokuSolver
	test("eliminatePossibilities1", testEP1());
	test("eliminatePossibilities2", testEP2());
	test("eliminatePossibilities3", testEP3());
	test("eliminatePossibilities4", testEP4());
	test("eliminateFromNeighbor1", testEFN1());
	test("eliminateFromNeighbor2", testEFN2());
	test("solveEasy1", testSolveEasy1());
	test("solveMedium1", testSolveMedium1());
	test("solveMedium2", testSolveMedium2());
	test("solveHard1", testSolveHard1());
	test("solveHard2", testSolveHard2());
	test("solveVeryHard1", testSolveVeryHard1());
	test("solveWorldsHardest1", testSolveWorldsHardest1());
	test("solveWorldsHardest2", testSolveWorldsHardest2());
	test("solveWorldsHardest3", testSolveWorldsHardest3());
    }

    //Report whether test succeeded
    public static void test(String title, boolean success) {
	if (success) {
	    System.out.println(title + ": passed");
	} else {
	    System.out.println(title + ": failed");
	}
    }

    //Tests for Utils methods
    public static boolean testSolved1() {
	if (!Utility.solved(0)) {
	    return true;
	}
	return false;
    }
    public static boolean testSolved2() {
	if (Utility.solved(-2147483648)) {
	    return true;
	}
	return false;
    }
    public static boolean testSolved3() {
	if (Utility.solved(-251622908)) {
	    return true;
	}
	return false;
    }
    public static boolean testSolved4() {
	if (!Utility.solved(1090553872)) {
	    return true;
	}
	return false;
    }
    public static boolean testOPR1() {
	if (Utility.onePossibleRemaining(0)) {
	    return false;
	}
	return true;
    }
    public static boolean testOPR2() {
	if (Utility.onePossibleRemaining(510)) {
	    return true;
	}
	return false;
    }
    public static boolean testOPR3() {
	if (Utility.onePossibleRemaining(511)) {
	    return false;
	}
	return true;
    }
    public static boolean testOPR4() {
	if (Utility.onePossibleRemaining(-2147483137)) {
	    return false;
	}
	return true;
    }
    public static boolean testOPR5() {
	if (Utility.onePossibleRemaining(-2147483145)) {
	    return true;
	}
	return false;
    }
    public static boolean testOPR6() {
	if (Utility.onePossibleRemaining(439)) {
	    return false;
	}
	return true;
    }
    public static boolean testOPR7() {
	if (Utility.onePossibleRemaining(183)) {
	    return false;
	}
	return true;
    }
    public static boolean testOPR8() {
	if (Utility.onePossibleRemaining(447)) {
	    return true;
	}
	return false;
    }
    public static boolean testOPR9() {
	if (Utility.onePossibleRemaining(255)) {
	    return true;
	}
	return false;
    }
    public static boolean testGA1() {
	if (Utility.getAnswer(447) == 7) {
	    return true;
	}
	return false;
    }
    public static boolean testGA2() {
	if (Utility.getAnswer(-2013265473) == 1
	    && Utility.getAnswer(1073742333) == 2
	    && Utility.getAnswer(1049083) == 3
	    && Utility.getAnswer(503) == 4
	    && Utility.getAnswer(495) == 5
	    && Utility.getAnswer(479) == 6
	    && Utility.getAnswer(447) == 7
	    && Utility.getAnswer(383) == 8
	    && Utility.getAnswer(255) == 9
	    && Utility.getAnswer(510) == 1) {
	    return true;
	}
	return false;
    }
    public static boolean testGN1() {
	if (Utility.getNum(0) == -1 && Utility.getNum(1) == -1) {
	    return true;
	}
	return false;
    }
    public static boolean testGN2() {
	if (Utility.getNum(-2013265920) == 1) {
	    return true;
	}
	return false;
    }
    public static boolean testGN3() {
	if (Utility.getNum(402653199) == -1) {
	    return true;
	}
	return false;
    }
    public static boolean testGN4() {
	if (Utility.getNum(1073741824) == -1) {
	    return true;
	}
	return false;
    }
    public static boolean testGN5() {
	if (Utility.getNum(-1342177280) == 6) {
	    return true;
	}
	return false;
    }
    public static boolean testSA1() {
	if (Utility.getNum(Utility.setAnswer(0, 5)) == 5) {
	    return true;
	}
	return false;
    }
    public static boolean testSA2() {
	if (Utility.getNum(Utility.setAnswer(123, 10)) == -1
	    && Utility.getNum(Utility.setAnswer(-1, -10)) == -1) {
	    return true;
	}
	return false;
    }
    public static boolean testSA3() {
	if (Utility.getNum(Utility.setAnswer(10, 9)) == 9
	    && Utility.getNum(Utility.setAnswer(9, 0)) == -1) {
	    return true;
	}
	return false;
    }
    public static boolean testBN1() {
	if (Utility.getBoxNum(0,0) == 0) {
	    return true;
	}
	return false;
    }
    public static boolean testBN2() {
	if (Utility.getBoxNum(2, 2) == 0) {
	    return true;
	}
	return false;
    }
    public static boolean testBN3() {
	if (Utility.getBoxNum(8,8) == 8) {
	    return true;
	}
	return false;
    }
    public static boolean testBN4() {
	if (Utility.getBoxNum(5, 5) == 4) {
	    return true;
	}
	return false;
    }
    public static boolean testBN5() {
	if (Utility.getBoxNum(8,0) == 6) {
	    return true;
	}
	return false;
    }
    public static boolean testGNP1() {
	if (Utility.getNumPossible(495) == 1) {
	    return true;
	}
	return false;
    }
    public static boolean testGNP2() {
	if (Utility.getNumPossible(511) == 0) {
	    return true;
	}
	return false;
    }
    public static boolean testGNP3() {
	if (Utility.getNumPossible(0) == 9) {
	    return true;
	}
	return false;
    }
    public static boolean testGNP4() {
	if (Utility.getNumPossible(254) == 2) {
	    return true;
	}
	return false;
    }
    public static boolean testSC1() {
	if (Utility.setCoor(0, 1, 1) == 8704) {
	    return true;
	}
	return false;
    }
    public static boolean testSC2() {
	if (Utility.setCoor(0, 3, 5) == 42496) {
	    return true;
	}
	return false;
    }
    public static boolean testSC3() {
	if (Utility.setCoor(256, 2, 1) == 9472) {
	    return true;
	}
	return false;
    }
    public static boolean testSC4() {
	if (Utility.setCoor(256, 0, 0) == 256) {
	    return true;
	}
	return false;
    }
    public static boolean testSC5() {
	if (Utility.setCoor(0, 8, 8) == 69632) {
	    return true;
	}
	return false;
    }
    public static boolean testGR1() {
	if (Utility.getRow(768) == 1) {
	    return true;
	}
	return false;
    }
    public static boolean testGR2() {
	if (Utility.getRow(512) == 1) {
	    return true;
	}
	return false;
    }
    public static boolean testGR3() {
	if (Utility.getRow(4096) == 8) {
	    return true;
	}
	return false;
    }
    public static boolean testGR4() {
	if (Utility.getRow(-2013261314) == 8) {
	    return true;
	}
	return false;
    }
    public static boolean testGR5() {
	if (Utility.getRow(-2013265410) == 0) {
	    return true;
	}
	return false;
    }
    public static boolean testGC1() {
	if (Utility.getColumn(8192) == 1) {
	    return true;
	}
	return false;
    }
    public static boolean testGC2() {
	if (Utility.getColumn(0) == 0) {
	    return true;
	}
	return false;
    }
    public static boolean testGC3() {
	if (Utility.getColumn(73728) == -1) {
	    return true;
	}
	return false;
    }
    public static boolean testGC4() {
	if (Utility.getColumn(40967) == 5) {
	    return true;
	}
	return false;
    }
    //Tests for SudokuBoard setTiles
    public static boolean testST1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 0, 9);
	if (sb.getRows()[0] == 256
	    && sb.getColumns()[0] == 256
	    && sb.getGameBoard()[0][0] == -939524096
	    && sb.getBoxes()[0] == 256) {
	    return true;
	}
	return false;
    }
    public static boolean testST2() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(2, 2, 5);
	if (sb.getRows()[2] == 16
	    && sb.getColumns()[2] == 16
	    && sb.getGameBoard()[2][2] == -1476395008
	    && sb.getBoxes()[0] == 16) {
	    return true;
	}
	return false;
    }
    public static boolean testST3() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(3, 4, 10);
	if (sb.getRows()[3] == 0
	    && sb.getColumns()[4] == 0
	    && sb.getGameBoard()[3][4] == 0
	    && sb.getBoxes()[4] == 0) {
	    return true;
	}
	return false;
    }
    
    //Tests for sudoku solver
    public static boolean testEP1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 1, 3);
	sb.setTile(0, 2, 6);
	sb.setTile(0, 3, 1);
	sb.setTile(0, 4, 2);
	sb.setTile(0, 5, 4);
	sb.setTile(0, 6, 5);
	sb.setTile(0, 7, 7);
	sb.setTile(0, 8, 8);
	sb.setTile(1, 0, 1);
	sb.setTile(1, 1, 4);
	sb.setTile(1, 2, 7);
	sb.setTile(2, 0, 2);
	sb.setTile(2, 1, 5);
	sb.setTile(2, 2, 8);
	sb.setTile(3, 0, 3);
	sb.setTile(4, 0, 4);
	sb.setTile(5, 0, 5);
	sb.setTile(6, 0, 6);
	sb.setTile(7, 0, 7);
	sb.setTile(8, 0, 8);
	SudokuSolver.eliminatePossibilities(sb, 20);
	if (sb.getRows()[0] == 511
	    && sb.getColumns()[0] == 511
	    && Utility.getAnswer(sb.getGameBoard()[0][0]) == 9
	    && sb.getBoxes()[0] == 511) {
	    return true;
	}
	return false;
    }
    public static boolean testEP2() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 2, 9);
	sb.setTile(0, 3, 2);
	sb.setTile(0, 5, 7);
	sb.setTile(1, 1, 6);
	sb.setTile(2, 0, 8);
	sb.setTile(2, 2, 5);
	sb.setTile(4, 0, 3);
	sb.setTile(7, 0, 4);
	SudokuSolver.eliminatePossibilities(sb, 8);
	if (sb.getRows()[0] == 323
	    && sb.getColumns()[0] == 141
	    && Utility.getAnswer(sb.getGameBoard()[0][0]) == 1
	    && sb.getBoxes()[0] == 433) {
	    return true;
	}
	return false;
    }
    public static boolean testEP3() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(1, 4, 5);
	sb.setTile(3, 4, 1);
	sb.setTile(3, 5, 2);
	sb.setTile(4, 1, 7);
	sb.setTile(4, 7, 6);
	sb.setTile(5, 3, 3);
	sb.setTile(5, 5, 4);
	sb.setTile(7, 4, 8);
	SudokuSolver.eliminatePossibilities(sb, 8);
	if (Utility.getAnswer(sb.getGameBoard()[4][4]) == 9) {
	    return true;
	}
	return false;
    }
    public static boolean testEP4() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(1, 7, 5);
	sb.setTile(2, 7, 4);
	sb.setTile(3, 7, 3);
	sb.setTile(4, 7, 2);
	sb.setTile(5, 7, 1);
	sb.setTile(6, 7, 6);
	sb.setTile(8, 7, 7);
	sb.setTile(3, 8, 9);
	sb.setTile(6, 8, 4);
	sb.setTile(7, 8, 1);
	sb.setTile(6, 6, 2);
	sb.setTile(7, 6, 5);
	sb.setTile(8, 6, 3);
	sb.setTile(7, 1, 6);
	sb.setTile(7, 2, 7);
	sb.setTile(7, 3, 4);
	sb.setTile(7, 4, 3);
	sb.setTile(7, 5, 2);
	sb.setTile(8, 0, 1);
	sb.setTile(8, 1, 2);
	sb.setTile(8, 2, 4);
	sb.setTile(8, 3, 5);
	sb.setTile(8, 4, 6);
	SudokuSolver.eliminatePossibilities(sb, 23);
	if (Utility.getAnswer(sb.getGameBoard()[8][8]) == 8
	    && Utility.getAnswer(sb.getGameBoard()[7][7]) == 9
	    && Utility.getAnswer(sb.getGameBoard()[8][5]) == 9
	    && Utility.getAnswer(sb.getGameBoard()[7][0]) == 8
	    && Utility.getAnswer(sb.getGameBoard()[0][7]) == 8
	    && Utility.getAnswer(sb.getGameBoard()[0][8]) == 0) {
	    return true;
	}
	return false;
    }
    public static boolean testSolveEasy1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 0, 3);
	sb.setTile(0, 3, 9);
	sb.setTile(0, 4, 1);
	sb.setTile(0, 7, 7);
	sb.setTile(1, 2, 9);
	sb.setTile(1, 7, 6);
	sb.setTile(2, 2, 4);
	sb.setTile(2, 5, 2);
	sb.setTile(2, 8, 5);
	sb.setTile(3, 0, 6);
	sb.setTile(3, 3, 8);
	sb.setTile(3, 6, 7);
	sb.setTile(4, 0, 1);
	sb.setTile(4, 4, 6);
	sb.setTile(4, 8, 4);
	sb.setTile(5, 2, 5);
	sb.setTile(5, 5, 4);
	sb.setTile(5, 8, 6);
	sb.setTile(6, 0, 7);
	sb.setTile(6, 3, 2);
	sb.setTile(6, 6, 9);
	sb.setTile(7, 1, 6);
	sb.setTile(7, 6, 1);
	sb.setTile(8, 1, 9);
	sb.setTile(8, 4, 3);
	sb.setTile(8, 5, 7);
	sb.setTile(8, 8, 2);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 27)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveEasy1 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testSolveHard1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 1, 5);
	sb.setTile(0, 3, 6);
	sb.setTile(0, 8, 1);
	sb.setTile(1, 0, 4);
	sb.setTile(1, 4, 5);
	sb.setTile(1, 7, 3);
	sb.setTile(2, 5, 2);
	sb.setTile(2, 6, 7);
	sb.setTile(3, 0, 6);
	sb.setTile(3, 3, 8);
	sb.setTile(3, 6, 3);
	sb.setTile(4, 1, 2);
	sb.setTile(4, 7, 7);
	sb.setTile(5, 2, 7);
	sb.setTile(5, 5, 1);
	sb.setTile(5, 8, 8);
	sb.setTile(6, 2, 2);
	sb.setTile(6, 3, 5);
	sb.setTile(7, 1, 4);
	sb.setTile(7, 4, 2);
	sb.setTile(7, 8, 6);
	sb.setTile(8, 0, 5);
	sb.setTile(8, 5, 6);
	sb.setTile(8, 7, 1);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 24)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveHard1 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testSolveHard2() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 0, 9);
	sb.setTile(0, 1, 5);
	sb.setTile(0, 7, 1);
	sb.setTile(1, 2, 4);
	sb.setTile(1, 3, 2);
	sb.setTile(1, 7, 9);
	sb.setTile(2, 4, 3);
	sb.setTile(2, 6, 8);
	sb.setTile(3, 1, 4);
	sb.setTile(3, 6, 6);
	sb.setTile(4, 0, 2);
	sb.setTile(4, 3, 8);
	sb.setTile(4, 5, 3);
	sb.setTile(4, 8, 7);
	sb.setTile(5, 2, 7);
	sb.setTile(5, 7, 4);
	sb.setTile(6, 2, 6);
	sb.setTile(6, 4, 1);
	sb.setTile(7, 1, 8);
	sb.setTile(7, 5, 5);
	sb.setTile(7, 6, 1);
	sb.setTile(8, 1, 3);
	sb.setTile(8, 7, 8);
	sb.setTile(8, 8, 9);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 24)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveHard2 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testSolveMedium1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 2, 3);
	sb.setTile(0, 5, 4);
	sb.setTile(0, 7, 6);
	sb.setTile(1, 0, 4);
	sb.setTile(1, 4, 5);
	sb.setTile(1, 6, 1);
	sb.setTile(2, 1, 5);
	sb.setTile(2, 3, 1);
	sb.setTile(2, 8, 2);
	sb.setTile(3, 0, 2);
	sb.setTile(3, 6, 5);
	sb.setTile(4, 1, 8);
	sb.setTile(4, 7, 9);
	sb.setTile(5, 2, 9);
	sb.setTile(5, 8, 8);
	sb.setTile(6, 0, 5);
	sb.setTile(6, 5, 2);
	sb.setTile(6, 7, 3);
	sb.setTile(7, 2, 8);
	sb.setTile(7, 4, 9);
	sb.setTile(7, 8, 4);
	sb.setTile(8, 1, 1);
	sb.setTile(8, 3, 7);
	sb.setTile(8, 6, 2);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 24)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveMedium1 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testSolveMedium2() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 1, 7);
	sb.setTile(0, 3, 8);
	sb.setTile(0, 4, 9);
	sb.setTile(0, 8, 4);
	sb.setTile(1, 1, 6);
	sb.setTile(1, 5, 5);
	sb.setTile(2, 2, 3);
	sb.setTile(2, 6, 2);
	sb.setTile(3, 0, 6);
	sb.setTile(3, 3, 2);
	sb.setTile(3, 6, 4);
	sb.setTile(3, 7, 7);
	sb.setTile(4, 4, 8);
	sb.setTile(5, 1, 8);
	sb.setTile(5, 2, 1);
	sb.setTile(5, 5, 9);
	sb.setTile(5, 8, 3);
	sb.setTile(6, 2, 5);
	sb.setTile(6, 6, 3);
	sb.setTile(7, 3, 5);
	sb.setTile(7, 7, 6);
	sb.setTile(8, 0, 4);
	sb.setTile(8, 4, 3);
	sb.setTile(8, 5, 8);
	sb.setTile(8, 7, 1);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 25)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveMedium2 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testSolveVeryHard1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 0, 6);
	sb.setTile(0, 4, 8);
	sb.setTile(0, 5, 7);
	sb.setTile(1, 2, 4);
	sb.setTile(1, 7, 6);
	sb.setTile(2, 1, 5);
	sb.setTile(2, 3, 6);
	sb.setTile(2, 6, 4);
	sb.setTile(3, 0, 7);
	sb.setTile(3, 4, 9);
	sb.setTile(3, 7, 2);
	sb.setTile(4, 0, 2);
	sb.setTile(4, 8, 5);
	sb.setTile(5, 1, 6);
	sb.setTile(5, 4, 3);
	sb.setTile(5, 8, 9);
	sb.setTile(6, 2, 9);
	sb.setTile(6, 5, 6);
	sb.setTile(6, 7, 5);
	sb.setTile(7, 1, 8);
	sb.setTile(7, 6, 1);
	sb.setTile(8, 3, 3);
	sb.setTile(8, 4, 2);
	sb.setTile(8, 8, 4);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 24)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveVeryHard1 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testSolveWorldsHardest1() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 0, 8);
	sb.setTile(1, 2, 3);
	sb.setTile(1, 3, 6);
	sb.setTile(2, 1, 7);
	sb.setTile(2, 4, 9);
	sb.setTile(2, 6, 2);
	sb.setTile(3, 1, 5);
	sb.setTile(3, 5, 7);
	sb.setTile(4, 4, 4);
	sb.setTile(4, 5, 5);
	sb.setTile(4, 6, 7);
	sb.setTile(5, 3, 1);
	sb.setTile(5, 7, 3);
	sb.setTile(6, 2, 1);
	sb.setTile(6, 7, 6);
	sb.setTile(6, 8, 8);
	sb.setTile(7, 2, 8);
	sb.setTile(7, 3, 5);
	sb.setTile(7, 7, 1);
	sb.setTile(8, 1, 9);
	sb.setTile(8, 6, 4);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 21)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveWorldsHardest1 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
   public static boolean testSolveWorldsHardest2() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 0, 8);
	sb.setTile(0, 1, 5);
	sb.setTile(0, 5, 2);
	sb.setTile(0, 6, 4);
	sb.setTile(1, 0, 7);
	sb.setTile(1, 1, 2);
	sb.setTile(1, 8, 9);
	sb.setTile(2, 2, 4);
	sb.setTile(3, 3, 1);
	sb.setTile(3, 5, 7);
	sb.setTile(3, 8, 2);
	sb.setTile(4, 0, 3);
	sb.setTile(4, 2, 5);
	sb.setTile(4, 6, 9);
	sb.setTile(5, 1, 4);
	sb.setTile(6, 4, 8);
	sb.setTile(6, 7, 7);
	sb.setTile(7, 1, 1);
	sb.setTile(7, 2, 7);
	sb.setTile(8, 4, 3);
	sb.setTile(8, 5, 6);
	sb.setTile(8, 7, 4);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 22)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveWorldsHardest2 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
   }
    public static boolean testSolveWorldsHardest3() {
	SudokuBoard sb = new SudokuBoard();
	sb.setTile(0, 2, 5);
	sb.setTile(0, 3, 3);
	sb.setTile(1, 0, 8);
	sb.setTile(1, 7, 2);
	sb.setTile(2, 1, 7);
	sb.setTile(2, 4, 1);
	sb.setTile(2, 6, 5);
	sb.setTile(3, 0, 4);
	sb.setTile(3, 5, 5);
	sb.setTile(3, 6, 3);
	sb.setTile(4, 1, 1);
	sb.setTile(4, 4, 7);
	sb.setTile(4, 8, 6);
	sb.setTile(5, 2, 3);
	sb.setTile(5, 3, 2);
	sb.setTile(5, 7, 8);
	sb.setTile(6, 1, 6);
	sb.setTile(6, 3, 5);
	sb.setTile(6, 8, 9);
	sb.setTile(7, 2, 4);
	sb.setTile(7, 7, 3);
	sb.setTile(8, 5, 9);
	sb.setTile(8, 6, 7);
	long startTime = System.currentTimeMillis();
	if (SudokuSolver.solve(sb, 23)) {
	    long endTime = System.currentTimeMillis();
	    System.out.println("SolveWorldsHardest3 - Total execution time: "
			       + (endTime-startTime) + "ms");
	    return true;
	}
	return false;
    }
    public static boolean testEFN1() {
	int val = 504;
	ArrayList<Integer> temp = new ArrayList<Integer>();
	temp.add(508);
	temp.add(508);
	int ans = -1744830464;
	if (SudokuSolver.eliminateFromNeighbor(val, temp) == ans) {
	    return true;
	}
	return false;
    }
    public static boolean testEFN2() {
	int val = 347;
	ArrayList<Integer> temp = new ArrayList<Integer>();
	temp.add(351);
	temp.add(348);
	temp.add(333);
	int ans = -1744830464;
	if (SudokuSolver.eliminateFromNeighbor(val, temp) == ans) {
	    return true;
	}
	return false;
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