import java.lang.Math;
import java.util.Scanner;
/**
 * A playable Minesweeper board, containing information on the status of a minesweeper game.
 * Connecting this with the GUI classes allow the game to be played in a JFrame.
 * @see MinesweeperFrame
 * @see MinesweeperPanel
 * @see Tile
 *
 * @author Ayu
 * @website https://ayu.dev/
 */
public class MinesweeperBoard implements java.io.Serializable {
	
	
	/**
	* Contains the board that the player will not see, and carries information like MINE_TILEs and numbered tiles.
	*/
	private int[][] board;
	/**
	* Contains the board that the player will see, holding information like HIDDEN_TILEs and FLAG_TILEs.
	*/
	private int[][] gameboard;
	/**
	* Keeps track of if the current game is in progress or not.
	*/
	private boolean inProgress;
	/**
	* Number of flags placed on the game board (Max NUM_OF_FLAGS is equal to NUM_OF_MINES)
	*/
	private int NUM_OF_FLAGS = 0;
	/**
	* Number of mines on the board
	*/
	private int NUM_OF_MINES;
	/**
	* Variable representation of a MINE_TILE.
	*/
	public static final int MINE_TILE = -1;
	/**
	* Variable representation of a HIDDEN_TILE, indicating the player has not discovered what is under this tile yet.
	*/
	public static final int HIDDEN_TILE = -2;
	/**
	* Variable representation of a FLAG_TILE, indicating the user marked a mine.
	*/
	public static final int FLAG_TILE = -3;
	/**
	* Variable representation of a QUESTION_TILE, indicating the user marked uncertainty.
	*/
	public static final int QUESTION_TILE = -4;
	/**
	* Variable representation of a ZERO_TILE, indicating no mines are adjecent to it.
	*/
	public static final int ZERO_TILE = 0;
	
	public static final double EASY_DIFFICULTY = .05;
	public static final double NORMAL_DIFFICULTY = .1;
	public static final double HARD_DIFFICULTY = .15;
	public static final double EXPERT_DIFFICULTY = .2;
	
	/**
	* Runs a console version of Minesweeper to test functionality (Rows: 50, Cols: 50, Mines: 250)
	*/
	public static void main(String[] args) {
		MinesweeperBoard board = new MinesweeperBoard(50,50,250);
		Scanner input = new Scanner(System.in);
		while(board.isInProgress()) {
			board.printGameBoard();
			System.out.print("Enter a command to flag, question, or reveal a tile (f/q/r row col): ");
			Scanner cmd = new Scanner(input.nextLine());
			String action = cmd.next();
			int row = cmd.nextInt();
			int col = cmd.nextInt();
			switch(action.toLowerCase()) {
				case "f": 
					board.flag(row,col);
					break;
				case "q":
					board.question(row,col);
					break;
				case "r":
					board.reveal(row,col);
					break;
			}
			if(!board.isInProgress()) {
				System.out.println("GAME OVER!!!");
				board.printGameBoard();
				System.exit(0);
			}
		}
	}
	
	/**
	* Creates a MinesweeperBoard
	* @param rows The number of rows for the board to contain
	* @param cols The number of columns for the board to contain
	* @param mines The number of mines for the board to contain
	*/
	public MinesweeperBoard(int rows, int cols, int mines) {
		NUM_OF_MINES = mines;
		board = new int[rows][cols];
		gameboard = new int[rows][cols];
		generateBoard();
	}
	/**
	* Grabs a tile from the gameboard (view)
	* @param row Row for which to grab the tile from
	* @param col Column for which to grab the tile from
	* @return Individual integer value for the gameboard tile at row, col
	*/
	public int getTile(int row, int col) {
		return gameboard[row][col];
	}
	/**
	* Grabs the 2D Integer array board
	* @return board
	*/
	public int[][] getBoard() {
		return board;
	}
	/**
	* Grabs the 2D Integer array gameboard
	* @return gameboard
	*/
	public int[][] getGameBoard() {
		return gameboard;
	}
	/**
	* @return Number of mines on the board
	*/
	public int getNumOfMines() {
		return NUM_OF_MINES;
	}
	/**
	* @return Number of flags placed on the gameboard
	*/
	public int getNumOfFlags() {
		return NUM_OF_FLAGS;
	}
	/**
	* @return Number of flags remaining able to be placed on the gameboard
	*/
	public int getNumOfFlagsRemaining() {
		return NUM_OF_MINES - NUM_OF_FLAGS;
	}
	/**
	* @return True if the game is in progress, false if otherwise.
	*/
	public boolean isInProgress() {
		return inProgress;
	}
	/**
	* @return 1 if the game is won, -1 if the game is still in progress, and 0 if the game is lost.
	*/
	public int gameStatus() {
		for(int r = 0; r < board.length; r++) {
			for(int c = 0; c < board[0].length; c++) {
				if(board[r][c] == MINE_TILE && gameboard[r][c] != FLAG_TILE) {
					// if the board is a mine and the view board is not a flag, then they didnt win or it is in progress
					if(inProgress) return -1;
					return 0;
				} else if(board[r][c] != gameboard[r][c] && gameboard[r][c] != FLAG_TILE) {
					// if the view board is not equal to the final board and the view board is not a flag tile, then they didnt win or the game is in progress
					if(inProgress) return -1;
					return 0;
				}
			}
		}
		return 1;
	}
	/**
	* Prints the board
	*/
	public void printBoard() {
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				if(board[row][col] == MINE_TILE) {
					System.out.print("M ");
				} else {
					System.out.print(board[row][col]+" ");
				}
			}
			System.out.println("");
		}
	}
	/**
	* Prints the gameboard with Row and Column markers, formatted for console.
	*/
	public void printGameBoard() {
		System.out.print("\t");
		if(gameboard[0].length > 9) {
			for(int i = 0; i < board[0].length; i++) {
				if(i > 9) {
					System.out.print((int)(i/10)+" ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println("");
			System.out.print("\t");
		}
		for(int i = 0, count = 0; i < board[0].length; i++, count++) {
			if(count > 9) count = 0;
			System.out.print(count+" ");
		}
		System.out.println("");
		for(int row = 0; row < board.length; row++) {
			System.out.print(row+".\t");
			for(int col = 0; col < board[0].length; col++) {
				if(gameboard[row][col] == HIDDEN_TILE) {
					if(inProgress) {
						System.out.print("- ");
					} else {
						if(board[row][col] == MINE_TILE) {
							System.out.print("M ");
						} else {
							System.out.print("- ");
						}
					}
				} else if(gameboard[row][col] == FLAG_TILE) {
					if(inProgress) {
						System.out.print("F ");
					} else {
						if(board[row][col] == MINE_TILE) {
							System.out.print("M ");
						} else {
							System.out.print("F ");
						}
					}
				} else if(gameboard[row][col] == QUESTION_TILE) {
					if(inProgress) {
						System.out.print("? ");
					} else {
						if(board[row][col] == MINE_TILE) {
							System.out.print("M ");
						} else {
							System.out.print("? ");
						}
					}
				} else if(gameboard[row][col] == ZERO_TILE) {
					System.out.print("  ");
				} else {
					System.out.print(board[row][col]+" ");
				}
			}
			System.out.println("");
		}
	}
	
	/**
	* Flags a tile, indicating a mine resides there. This is a toggle.
	* @param row Row for which to flag/unflag
	* @param col Column for which to flag/unflag
	* @return Integer value of 1,0, or -1 based on whether it flagged, unflagged, or failed.
	*/
	public int flag(int row, int col) {
		if((gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE) && NUM_OF_FLAGS < NUM_OF_MINES) {
			gameboard[row][col] = FLAG_TILE;
			NUM_OF_FLAGS++;
			if(NUM_OF_FLAGS == NUM_OF_MINES) {
				if(gameStatus() == 1) {
					inProgress = false;
				}
			}
			return 1;
		} else if(gameboard[row][col] == FLAG_TILE) {
			gameboard[row][col] = HIDDEN_TILE;
			NUM_OF_FLAGS--;
			return 0;
		}
		return -1;
	}
	
	/**
	* "Questions" a tile, indicating a mine may reside there. This is a toggle.
	* @param row Row for which to question/unquestion
	* @param col Column for which to question/unquestion
	* @return Integer value of 1,0, or -1 based on whether it questioned, unquestioned, or failed.
	*/
	public int question(int row, int col) {
		if(gameboard[row][col] == HIDDEN_TILE) {
			gameboard[row][col] = QUESTION_TILE;
			return 1;
		} else if(gameboard[row][col] == FLAG_TILE) {
			gameboard[row][col] = QUESTION_TILE;
			NUM_OF_FLAGS--;
			return 1;
		} else if(gameboard[row][col] == QUESTION_TILE) {
			gameboard[row][col] = HIDDEN_TILE;
			return 0;
		}
		return -1;
	}
	
	/**
	* Reveals a tile, checking whether or not it is a mine in the process.
	* @param row Row for which to reveal
	* @param col Column for which to reveal
	* @return If the reveal did not reveal a mine, returns true. Else, returns false.
	*/
	public boolean reveal(int row, int col) {
		if(inProgress) {
			if(gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE) {
				if(board[row][col] != MINE_TILE) {
					revealAll(row,col);
					if(gameStatus() == 1) {
						inProgress = false;
					}
					return true;
				} else {
					inProgress = false;
					gameboard[row][col] = MINE_TILE;
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	* Reveals all adjacent tiles to a ZERO_TILE recursively.
	* @param row Row for which tile to reveal
	* @param col Column for which tile to reveal
	*/
	private void revealAll(int row, int col) {
		if(gameboard[row][col] == FLAG_TILE) {
			NUM_OF_FLAGS--;
		}
		gameboard[row][col] = board[row][col];
		if(board[row][col] == ZERO_TILE) {
			int rows = board.length;
			int cols = board[0].length;
			if(row-1 >= 0) {
				row--;
				if(col-1 >= 0) {
					col--;
					if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
					else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
						if(gameboard[row][col] == FLAG_TILE) {
							NUM_OF_FLAGS--;
						}
						gameboard[row][col] = board[row][col];
					}
					col++;
				}
				if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
				else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
					if(gameboard[row][col] == FLAG_TILE) {
						NUM_OF_FLAGS--;
					}
					gameboard[row][col] = board[row][col];
				}
				if(col+1 < cols) {
					col++;
					if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
					else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
						if(gameboard[row][col] == FLAG_TILE) {
							NUM_OF_FLAGS--;
						}
						gameboard[row][col] = board[row][col];
					}
					col--;
				}
				row++;
			}
			
			if(col-1 >= 0) {
				col--;
				if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
				else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
					if(gameboard[row][col] == FLAG_TILE) {
						NUM_OF_FLAGS--;
					}
					gameboard[row][col] = board[row][col];
				}
				col++;
			}
			if(col+1 < cols) {
				col++;
				if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
				else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
					if(gameboard[row][col] == FLAG_TILE) {
						NUM_OF_FLAGS--;
					}
					gameboard[row][col] = board[row][col];
				}
				col--;
			}
			
			if(row+1 < rows) {
				row++;
				if(col-1 >= 0) {
					col--;
					if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
					else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
						if(gameboard[row][col] == FLAG_TILE) {
							NUM_OF_FLAGS--;
						}
						gameboard[row][col] = board[row][col];
					}
					col++;
				}
				if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
				else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
					if(gameboard[row][col] == FLAG_TILE) {
						NUM_OF_FLAGS--;
					}
					gameboard[row][col] = board[row][col];
				}
				if(col+1 < cols) {
					col++;
					if(board[row][col] == ZERO_TILE && gameboard[row][col] != ZERO_TILE) revealAll(row,col);
					else if(board[row][col] != MINE_TILE && (gameboard[row][col] == HIDDEN_TILE || gameboard[row][col] == QUESTION_TILE || gameboard[row][col] == FLAG_TILE)) {
						if(gameboard[row][col] == FLAG_TILE) {
							NUM_OF_FLAGS--;
						}
						gameboard[row][col] = board[row][col];
					}
					col--;
				}
				row--;
			}
		}
	}
	
	/**
	* Generates the starting board and gameboard, and sets inProgress to true.
	*/
	private void generateBoard() {
		int rows = board.length;
		int cols = board[0].length;
		inProgress = true;
		
		// Generation of gameboard
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				gameboard[row][col] = HIDDEN_TILE;
			}
		}
		
		// Generation of board
		for(int i = 0; i < NUM_OF_MINES; i++) {
			int randomRow = (int)(Math.random() * (rows));
			int randomCol = (int)(Math.random() * (cols));
			if(board[randomRow][randomCol] != MINE_TILE) {
				board[randomRow][randomCol] = MINE_TILE;
			} else {
				i--;
			}
		}
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				if(board[row][col] != MINE_TILE) {
					int mines = 0;
					
					if(row-1 >= 0) {
						if(col-1 >= 0) {
							if(board[row-1][col-1] == MINE_TILE) mines++;
						}
						if(board[row-1][col] == MINE_TILE) mines++;
						if(col+1 < cols) {
							if(board[row-1][col+1] == MINE_TILE) mines++;
						}
					}
					
					if(col-1 >= 0) {
						if(board[row][col-1] == MINE_TILE) mines++;
					}
					if(board[row][col] == MINE_TILE) mines++;
					if(col+1 < cols) {
						if(board[row][col+1] == MINE_TILE) mines++;
					}
					
					if(row+1 < rows) {
						if(col-1 >= 0) {
							if(board[row+1][col-1] == MINE_TILE) mines++;
						}
						if(board[row+1][col] == MINE_TILE) mines++;
						if(col+1 < cols) {
							if(board[row+1][col+1] == MINE_TILE) mines++;
						}
					}
					
					board[row][col] = mines;
				}
			}
		}
	}
	
}
	