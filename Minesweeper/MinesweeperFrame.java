import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.io.*;
import java.io.File;
/**
 * The JFrame UI for the Minesweeper game.
 * @author Ayu
 * @website https://ayu.dev/
 */
public class MinesweeperFrame extends JFrame {
	
	MinesweeperBoard board;
	MinesweeperPanel panel;
	JLabel flagDisplay;
	JMenuBar menuBar;
	JMenuItem newGameEasy;
	JMenuItem newGameNormal;
	JMenuItem newGameHard;
	JMenuItem newGameExpert;
	public final int GridSize = 25;
	private final String title = "Minesweeper v2.0 Java Edition by Ayu";
	// Sets the amount of mines to 10% of the total tiles. This can be any integer from 0 to Math.pow(GridSize,2)
	private int Mines = (int)((Math.pow(GridSize,2))*.1);
	private final int resolution = 30;
	ImageIcon HIDDEN_TILE = new ImageIcon(new ImageIcon("assets/hidden.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon ZERO_TILE = new ImageIcon(new ImageIcon("assets/0.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon ONE_TILE = new ImageIcon(new ImageIcon("assets/1.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon TWO_TILE = new ImageIcon(new ImageIcon("assets/2.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon THREE_TILE = new ImageIcon(new ImageIcon("assets/3.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon FOUR_TILE = new ImageIcon(new ImageIcon("assets/4.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon FIVE_TILE = new ImageIcon(new ImageIcon("assets/5.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon SIX_TILE = new ImageIcon(new ImageIcon("assets/6.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon SEVEN_TILE = new ImageIcon(new ImageIcon("assets/7.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon EIGHT_TILE = new ImageIcon(new ImageIcon("assets/8.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon FLAG_TILE = new ImageIcon(new ImageIcon("assets/flag.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon QUESTION_TILE = new ImageIcon(new ImageIcon("assets/question.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	ImageIcon MINE_TILE = new ImageIcon(new ImageIcon("assets/mine.png").getImage().getScaledInstance(resolution,resolution,Image.SCALE_AREA_AVERAGING));
	
	public MinesweeperFrame() {
		setTitle(title);
		int FrameSize = 830;
		setSize(FrameSize,FrameSize);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		board = new MinesweeperBoard(GridSize,GridSize,Mines);
		panel = new MinesweeperPanel(this);
		
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_A);
		JMenu newGame = new JMenu("New Game");
		newGameEasy = new JMenuItem("Easy");
		newGameNormal = new JMenuItem("Normal");
		newGameHard = new JMenuItem("Hard");
		newGameExpert = new JMenuItem("Expert");
		newGameEasy.addActionListener(new JMenuItemHandler());
		newGameNormal.addActionListener(new JMenuItemHandler());
		newGameHard.addActionListener(new JMenuItemHandler());
		newGameExpert.addActionListener(new JMenuItemHandler());
		newGame.add(newGameEasy);
		newGame.add(newGameNormal);
		newGame.add(newGameHard);
		newGame.add(newGameExpert);
		JMenuItem saveGame = new JMenuItem("Save Game");
		JMenuItem loadGame = new JMenuItem("Load Game");
		JMenuItem quit = new JMenuItem("Quit");
		saveGame.addActionListener(new JMenuItemHandler());
		loadGame.addActionListener(new JMenuItemHandler());
		quit.addActionListener(new JMenuItemHandler());
		file.add(newGame);
		file.add(saveGame);
		file.add(loadGame);
		file.add(quit);
		menuBar.add(file);
		
		
		flagDisplay = new JLabel(board.getNumOfFlagsRemaining()+" flags remain",SwingConstants.CENTER);
		flagDisplay.setForeground(Color.BLUE);
		
		this.setJMenuBar(menuBar);
		
		add(flagDisplay, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public MinesweeperBoard getModel() {
		return board;
	}
	
	public void newGame(double difficulty) {
		this.setVisible(false);
		Mines = (int)((Math.pow(GridSize,2))*difficulty);
		/**
		if(difficulty == board.EASY_DIFFICULTY) setTitle(title+" (Difficulty: Easy)");
		else if(difficulty == board.NORMAL_DIFFICULTY) setTitle(title+" (Difficulty: Normal)");
		else if(difficulty == board.HARD_DIFFICULTY) setTitle(title+" (Difficulty: Hard)");
		else if(difficulty == board.EXPERT_DIFFICULTY) setTitle(title+" (Difficulty: Expert)");
		else setTitle(title+" (Difficulty: Unrecognized)");
		*/
		this.remove(panel);
		this.remove(flagDisplay);
		board = new MinesweeperBoard(GridSize,GridSize,Mines);
		panel = new MinesweeperPanel(this);
		flagDisplay = new JLabel(board.getNumOfFlagsRemaining()+" flags remain",SwingConstants.CENTER);
		flagDisplay.setForeground(Color.BLUE);
		this.add(flagDisplay, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void loadGame(String path) {
		try {
			ObjectInputStream loadInput = new ObjectInputStream(new FileInputStream(path));
			board = (MinesweeperBoard) loadInput.readObject();
			this.setVisible(false);
			this.remove(panel);
			this.remove(flagDisplay);
			panel = new MinesweeperPanel(this);
			flagDisplay = new JLabel(board.getNumOfFlagsRemaining()+" flags remain",SwingConstants.CENTER);
			flagDisplay.setForeground(Color.BLUE);
			this.add(flagDisplay, BorderLayout.NORTH);
			this.add(panel, BorderLayout.CENTER);
			this.setVisible(true);
			this.repaint();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "The file you selected was not a valid save file","Error: Load failed", JOptionPane.DEFAULT_OPTION);
		}
	}
	public void saveGame(String path) {
		try {
			ObjectOutputStream saveOutput = new ObjectOutputStream(new FileOutputStream(path));
			saveOutput.writeObject(board);
			saveOutput.flush();
			saveOutput.close();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "The file path/name you specified could not be saved to","Error: Save failed", JOptionPane.DEFAULT_OPTION);
		}
	}
	
	private class JMenuItemHandler implements ActionListener {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DATA Files", "dat");
		public void actionPerformed(ActionEvent e) {
			switch(((JMenuItem)(e.getSource())).getText()) {
				case "Easy": newGame(board.EASY_DIFFICULTY); break;
				case "Normal": newGame(board.NORMAL_DIFFICULTY); break;
				case "Hard": newGame(board.HARD_DIFFICULTY); break;
				case "Expert": newGame(board.EXPERT_DIFFICULTY); break;
				case "Save Game": 
				JFileChooser fileSaver = new JFileChooser(); 
				fileSaver.setFileFilter(filter);
				fileSaver.setAcceptAllFileFilterUsed(false);
				if (fileSaver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
					saveGame(fileSaver.getSelectedFile().getAbsolutePath());
				break;
				case "Load Game":
				JFileChooser fileOpener = new JFileChooser(); 
				fileOpener.setFileFilter(filter);
				fileOpener.setAcceptAllFileFilterUsed(false);
				if (fileOpener.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
					loadGame(fileOpener.getSelectedFile().getAbsolutePath());
				break;
				case "Quit": System.exit(0); break;
			}
		}
	}

}
