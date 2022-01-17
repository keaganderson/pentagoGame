package temp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public final class PentagoImplementation {

	public static int lastMax;
	public static int lastMin;
	public static int placeCounter;
	public PentagoImplementation() {
		lastMax = 0;
		lastMin = 0;
		placeCounter = 0;
	}
	
	public static void main(final String[] theArgs) {
		PentagoImplementation gameStart = new PentagoImplementation();
		List<String> gameBoard1 = new ArrayList<String>();
		List<String> gameBoard2 = new ArrayList<String>();
		List<String> gameBoard3 = new ArrayList<String>();
		List<String> gameBoard4 = new ArrayList<String>();
		for (int i = 0; i < 9; i++) {
			gameBoard1.add(".");
			gameBoard2.add(".");
			gameBoard3.add(".");
			gameBoard4.add(".");
		}
		List<List<String>> currBoard = new ArrayList<List<String>>();
		currBoard.add(gameBoard1);
		currBoard.add(gameBoard2);
		currBoard.add(gameBoard3);
		currBoard.add(gameBoard4);
		boolean gameOver = false;
		Scanner scanner = new Scanner(System.in);
		try {
			
			File myObj = new File("C:\\Users\\Keagan\\Desktop\\Output.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("C:\\Users\\Keagan\\Desktop\\Output.txt");
			System.out.println("White or Blue?\n");
			myWriter.write("Player Turn:" + "\n");
			String color = scanner.nextLine();
			System.out.println(color + " chosen\n");
			int counter;
			String playerColor;
			String enemyColor;
			if (color.equals("White")) {
				counter = 0;
				playerColor = "W";
				enemyColor = "B";
			} else {
				counter = 1;
				playerColor = "B";
				enemyColor = "W";
			}
			while (!gameOver) {
				if (counter % 2 == 0) {
					System.out.println("Player Turn:");
					myWriter.write("Player Turn:" + "\n");
					String input = scanner.nextLine();
					System.out.println("Move Selected:" + input);
					myWriter.write("Move Selected:" + input + "\n");
					currBoard = calcMove(currBoard, playerColor, input);
				} else {
					System.out.println("Ai Turn:");
					myWriter.write("Ai Turn:" + "\n");
					currBoard = pruningBegin(currBoard, enemyColor);
				}
				int gameConclusion = checkVictory(currBoard);
				if (gameConclusion == 0) {
					System.out.println("Tie Game!");
					myWriter.write("Tie Game!\n");
					gameOver = true;
				} else if (gameConclusion == 1) {
					System.out.println("White Wins!");
					myWriter.write("White Wins!\n");
					gameOver = true;
				} else if (gameConclusion == 2) {
					System.out.println("Blue Wins!");
					myWriter.write("Blue Wins!\n");
					gameOver = true;
				}
				printBoard(currBoard, myWriter);
				counter++;
			}
			myWriter.close();
			scanner.close();
		}catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public static List<List<String>> calcMove(List<List<String>> curr, String turn, String input) {
		curr = placePiece(curr, Character.getNumericValue(input.charAt(0)), Character.getNumericValue(input.charAt(2)), turn);
		Boolean gameOver = false;
		if (checkVictory(curr) != -1) {
			gameOver = true;
		}
		if (!gameOver) {
			curr = rotateBoard(curr, Character.getNumericValue(input.charAt(4)), ""+Character.toUpperCase(input.charAt(5)));
		}
		return curr;
	}
	
	
	public static List<List<String>> pruningBegin(List<List<String>> gameBoard, String turn) { 
		List<List<List<String>>> children = generateChildren(gameBoard, turn);
		int num;
		String alternate;
		List<List<String>> decision = children.get(0);
		if (turn.equals("W")) {
			num = -10000;
			alternate = "B";
		} else {
			num = 10000;
			alternate = "W";
		}
		for (List<List<String>> option : children) {
			int choice = pruningChoice(option, alternate, 0, -10000, 10000);
			if (turn.equals("W") && choice > num) {
				num = choice;
				decision = option;
			} else if (turn.equals("B") && choice < num) {
				num = choice;
				decision = option;
			}
		}
		return decision;
	}
	
	public static int pruningChoice(List<List<String>> gameBoard, String turn, int count, int A, int B) {
		if (count > 2) {
			return utilityCalculation(gameBoard);
		} else if (turn.equals("B")) {
			String alternate = "W";
			List<List<List<String>>> children = generateChildren(gameBoard, turn);
			int round = 0;
			List<Integer> min = new ArrayList<Integer>();
			while (!(A > B) && round < children.size()) {
				int best = pruningChoice(children.get(round), alternate, count + 1, A, B);
				if (best < B) {
					B = best;
				}
				min.add(best);
				round++;
			}
			return Collections.min(min);
		} else {
			String alternate = "B";
			List<List<List<String>>> children = generateChildren(gameBoard, turn);
			int round = 0;
			List<Integer> max = new ArrayList<Integer>();
			while (!(A > B) && round < children.size()) {
				int best = pruningChoice(children.get(round), alternate, count + 1, A, B);
				if (best > A) {
					A = best; 
				}
				max.add(best);
				round++;
			}
			return Collections.max(max);
		}
	}
	
	public static List<List<List<String>>> generateChildren(List<List<String>> current, String turn) {
		List<List<List<String>>> options = new ArrayList<List<List<String>>>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 4; k++) {
					if (current.get(i).get(j).equals(".")) {
						List<List<String>> currentOption = placePiece(current, i + 1, j + 1, turn);
						List<List<String>> option1 = rotateBoard(currentOption, k + 1, "R");
						List<List<String>> option2 = rotateBoard(currentOption, k + 1, "L");
						options.add(option1);
						options.add(option2);
					}
				}
			}
		}
		return options;
	}
	
	public static void printBoard(List<List<String>> board, FileWriter myWriter) {
		try {
		System.out.println("CurrentBoard: ");
		for (int i = 0; i < 3; i++) {
			System.out.println(board.get(0).get(0 + (i * 3)) + board.get(0).get(1 + (i * 3)) + board.get(0).get(2 + (i * 3)) + " " + board.get(1).get(0 + (i * 3)) + board.get(1).get(1 + (i * 3)) + board.get(1).get(2 + (i * 3)));
			myWriter.write(board.get(0).get(0 + (i * 3)) + board.get(0).get(1 + (i * 3)) + board.get(0).get(2 + (i * 3)) + " " + board.get(1).get(0 + (i * 3)) + board.get(1).get(1 + (i * 3)) + board.get(1).get(2 + (i * 3)) + "\n");
		}
		System.out.println();
		for (int i = 0; i < 3; i++) {
			System.out.println(board.get(2).get(0 + (i * 3)) + board.get(2).get(1 + (i * 3)) + board.get(2).get(2 + (i * 3)) + " " + board.get(3).get(0 + (i * 3)) + board.get(3).get(1 + (i * 3)) + board.get(3).get(2 + (i * 3)));
			myWriter.write(board.get(2).get(0 + (i * 3)) + board.get(2).get(1 + (i * 3)) + board.get(2).get(2 + (i * 3)) + " " + board.get(3).get(0 + (i * 3)) + board.get(3).get(1 + (i * 3)) + board.get(3).get(2 + (i * 3)) + "\n");
		}
		System.out.println();
		} catch(IOException e){ 
	
		}
	}
	
	public static List<List<String>> placePiece(List<List<String>> currentBoard, int board, int position, String turn) {
		List<List<String>> gameBoard = new ArrayList<List<String>>(currentBoard);
		if (board < 1 || board > 4 || position < 1 || position > 9) {
			throw new IllegalArgumentException();
		} else if (board == 1) {
			if (!gameBoard.get(0).get(position - 1).equals(".")) {
				throw new IllegalArgumentException();
			}
			List<String> temp = new ArrayList<String>(gameBoard.get(0));
			temp.set(position - 1, turn);
			gameBoard.set(0, temp);;
		} else if (board == 2) {
			if (!gameBoard.get(1).get(position - 1).equals(".")) {
				throw new IllegalArgumentException();
			}
			List<String> temp = new ArrayList<String>(gameBoard.get(1));
			temp.set(position - 1, turn);
			gameBoard.set(1, temp);
		} else if (board == 3) {
			if (!gameBoard.get(2).get(position - 1).equals(".")) {
				throw new IllegalArgumentException();
			}
			List<String> temp = new ArrayList<String>(gameBoard.get(2));
			temp.set(position - 1, turn);
			gameBoard.set(2, temp);
		} else if (board == 4) {
			if (!gameBoard.get(3).get(position - 1).equals(".")) {
				throw new IllegalArgumentException();
			}
			List<String> temp = new ArrayList<String>(gameBoard.get(3));
			temp.set(position - 1, turn);
			gameBoard.set(3, temp);
		}
		return gameBoard;
	}
	
	public static List<List<String>> rotateBoard(List<List<String>> currentBoard, int board, String direction) {
		List<List<String>> gameBoard = new ArrayList<List<String>>(currentBoard);
		if (board < 1 || board > 4 || !(direction.equals("R") || direction.equals("L"))) {
			throw new IllegalArgumentException();
		} else if (board == 1) {
			if (direction.equals("R")) {
				gameBoard.set(0, rotateRight(gameBoard.get(0)));
			} else {
				gameBoard.set(0, rotateLeft(gameBoard.get(0)));
			}
		} else if (board == 2)	{
			if (direction.equals("R")) {
				gameBoard.set(1, rotateRight(gameBoard.get(1)));
			} else {
				gameBoard.set(1, rotateLeft(gameBoard.get(1)));
			}
		} else if (board == 3) {
			if (direction.equals("R")) {
				gameBoard.set(2, rotateRight(gameBoard.get(2)));
			} else {
				gameBoard.set(2, rotateLeft(gameBoard.get(2)));
			}
		} else {
			if (direction.equals("R")) {
				gameBoard.set(3, rotateRight(gameBoard.get(3)));
			} else {
				gameBoard.set(3, rotateLeft(gameBoard.get(3)));
			}
		}
		return gameBoard;
	}
	
	public static List<String> rotateRight(List<String> input) {
		List<String> rotated = new ArrayList<String>();
		rotated.add(input.get(6));
		rotated.add(input.get(3));
		rotated.add(input.get(0));
		rotated.add(input.get(7));
		rotated.add(input.get(4));
		rotated.add(input.get(1));
		rotated.add(input.get(8));
		rotated.add(input.get(5));
		rotated.add(input.get(2));
		return rotated;
	}
	
	public static List<String> rotateLeft(List<String> input) {
		List<String> rotated = new ArrayList<String>();
		rotated.add(input.get(2));
		rotated.add(input.get(5));
		rotated.add(input.get(8));
		rotated.add(input.get(1));
		rotated.add(input.get(4));
		rotated.add(input.get(7));
		rotated.add(input.get(0));
		rotated.add(input.get(3));
		rotated.add(input.get(6));
		return rotated;
	}
	
	public static int utilityCalculation(List<List<String>> gameState) {
		int max = -1000;
		int min = 1000;
		List<Integer> horizontals = horizontalCalc(gameState);
		if (min > horizontals.get(0)) {
			min = horizontals.get(0);
		}
		if (max < horizontals.get(1)) {
			max = horizontals.get(1);
		}
		List<Integer> verticals = verticalCalc(gameState);
		if (min > verticals.get(0)) {
			min = verticals.get(0);
		}
		if (max < verticals.get(1)) {
			max = verticals.get(1);
		}
		List<Integer> diagonals = diagonalCalc(gameState);
		if (min > diagonals.get(0)) {
			min = diagonals.get(0);
		}
		if (max < diagonals.get(1)) {
			max = diagonals.get(1);
		}
		if (min < -4) {
			min = -100;
		}
		if (max > 4) {
			max = 100;
		}
		lastMin = min;
		lastMax = max;
		return min + max;
	}
	
	public static List<Integer> horizontalCalc(List<List<String>>gameState){
		int max = -1000;
		int min = 1000;
		List<String> gameBlock1 = gameState.get(0);
		List<String> gameBlock2 = gameState.get(1);
		List<String> gameBlock3 = gameState.get(2);
		List<String> gameBlock4 = gameState.get(3);
		for (int j = 0; j < 3; j++) {
			String current = null;
			int count = 69;
			for (int i = 0; i < 6; i++) {
				if (i < 3) {
					if (gameBlock1.get(i + (j * 3)).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock1.get(i + (j * 3)); 
					}
				} else {
					if (gameBlock2.get(i + (j * 3) - 3).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock2.get(i + (j * 3) - 3); 
					}
				}
				if (current.equals("W") && count > max) {
					max = count;
				} else if (current.equals("B") && 0 - count < min) {
					min = 0 - count;
				}
			}
		}
		
		for (int j = 0; j < 3; j++) {
			String current = null;
			int count = 69;
			for (int i = 0; i < 6; i++) {
				if (i < 3) {
					if (gameBlock3.get(i + (j * 3)).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock3.get(i + (j * 3)); 
					}
				} else {
					if (gameBlock4.get(i + (j * 3) - 3).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock4.get(i + (j * 3) - 3); 
					}
				}
				if (current.equals("W") && count > max) {
					max = count;
				} else if (current.equals("B") && 0 - count < min) {
					min = 0 - count;
				}
			}
		}
		List<Integer> totals = new ArrayList<Integer>();
		totals.add(min);
		totals.add(max);
		return totals;
	}
	
	public static List<Integer> verticalCalc(List<List<String>>gameState){
		int max = -1000;
		int min = 1000;
		List<String> gameBlock1 = gameState.get(0);
		List<String> gameBlock2 = gameState.get(1);
		List<String> gameBlock3 = gameState.get(2);
		List<String> gameBlock4 = gameState.get(3);
		for (int j = 0; j < 3; j++) {
			String current = null;
			int count = 69;
			for (int i = 0; i < 6; i++) {
				if (i < 3) {
					if (gameBlock1.get(3 * i + (j * 1)).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock1.get(3 * i + (j * 1)); 
					}
				} else {
					if (gameBlock3.get(3 * (i - 3) + (j * 1)).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock3.get(3 * (i - 3) + (j * 1)); 
					}
				}
				if (current.equals("W") && count > max) {
					max = count;
				} else if (current.equals("B") && 0 - count < min) {
					min = 0 - count;
				}
			}
		}
		
		for (int j = 0; j < 3; j++) {
			String current = null;
			int count = 69;
			for (int i = 0; i < 6; i++) {
				if (i < 3) {
					if (gameBlock2.get(3 * i + (j * 1)).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock2.get(3 * i + (j * 1)); 
					}
				} else {
					if (gameBlock4.get(3 * (i - 3) + (j * 1)).equals(current)) {
						count++;
					} else {
						count = 1;
						current = gameBlock4.get(3 * (i - 3) + (j * 1)); 
					}
				}
				if (current.equals("W") && count > max) {
					max = count;
				} else if (current.equals("B") && 0 - count < min) {
					min = 0 - count;
				}
			}
		}
		List<Integer> totals = new ArrayList<Integer>();
		totals.add(min);
		totals.add(max);
		return totals;
	}
	
	public static List<Integer> diagonalCalc(List<List<String>>gameState){
		int max = -1000;
		int min = 1000;
		List<String> gameBlock1 = gameState.get(0);
		List<String> gameBlock2 = gameState.get(1);
		List<String> gameBlock3 = gameState.get(2);
		List<String> gameBlock4 = gameState.get(3);
		List<String> diagonal1 = new ArrayList<String>();
		diagonal1.add(gameBlock1.get(3));
		diagonal1.add(gameBlock1.get(7));
		diagonal1.add(gameBlock3.get(2));
		diagonal1.add(gameBlock4.get(3));
		diagonal1.add(gameBlock4.get(7));
		List<String> diagonal2 = new ArrayList<String>();
		diagonal2.add(gameBlock1.get(1));
		diagonal2.add(gameBlock1.get(5));
		diagonal2.add(gameBlock2.get(6));
		diagonal2.add(gameBlock4.get(1));
		diagonal2.add(gameBlock4.get(5));
		List<String> diagonal3 = new ArrayList<String>();
		diagonal3.add(gameBlock2.get(1));
		diagonal3.add(gameBlock2.get(3));
		diagonal3.add(gameBlock1.get(8));
		diagonal3.add(gameBlock3.get(1));
		diagonal3.add(gameBlock3.get(3));
		List<String> diagonal4 = new ArrayList<String>();
		diagonal4.add(gameBlock2.get(5));
		diagonal4.add(gameBlock2.get(7));
		diagonal4.add(gameBlock4.get(0));
		diagonal4.add(gameBlock3.get(5));
		diagonal4.add(gameBlock3.get(7));
		List<String> diagonal5 = new ArrayList<String>();
		diagonal5.add(gameBlock1.get(0));
		diagonal5.add(gameBlock1.get(4));
		diagonal5.add(gameBlock1.get(8));
		diagonal5.add(gameBlock4.get(0));
		diagonal5.add(gameBlock4.get(4));
		diagonal5.add(gameBlock4.get(8));
		List<String> diagonal6 = new ArrayList<String>();
		diagonal6.add(gameBlock2.get(2));
		diagonal6.add(gameBlock2.get(4));
		diagonal6.add(gameBlock2.get(6));
		diagonal6.add(gameBlock3.get(2));
		diagonal6.add(gameBlock3.get(4));
		diagonal6.add(gameBlock3.get(6));
		List<List<String>> diagonals = new ArrayList<List<String>>();
		diagonals.add(diagonal1);
		diagonals.add(diagonal2);
		diagonals.add(diagonal3);
		diagonals.add(diagonal4);
		diagonals.add(diagonal5);
		diagonals.add(diagonal6);
		for (int j = 0; j < diagonals.size(); j++) {
			String current = null;
			int count = 69;
			for (int i = 0; i < diagonals.get(j).size(); i++) {
				if (diagonals.get(j).get(i).equals(current)) {
					count++;
				} else {
					count = 1;
					current = diagonals.get(j).get(i); 
				}
				if (current.equals("W") && count > max) {
					max = count;
				} else if (current.equals("B") && 0 - count < min) {
					min = 0 - count;
				}
			}
		}
		List<Integer> totals = new ArrayList<Integer>();
		totals.add(min);
		totals.add(max);
		return totals;
	}
	
	
	public static int checkVictory(List<List<String>> board) {
		placeCounter++;
		utilityCalculation(board);
		if (placeCounter > 70 || (lastMax == 100 && lastMin == -100)) {
			return 0;
		} else if (lastMax == 100) {
			return 1;
		} else if (lastMin == -100) {
			return 2;
		}
		return -1;
	}
	
	
}

