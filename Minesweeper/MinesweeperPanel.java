import javax.swing.*;
import java.awt.*;
/**
 * @author Ayu
 * @website https://ayu.dev/
 */
public class MinesweeperPanel extends JPanel {
	
	MinesweeperFrame frame;
	JLabel flagDisplay;
	private Tile[][] tiles;
	private boolean inProgress = true;
	
	public MinesweeperPanel(MinesweeperFrame frame) {
		this.frame = frame;
		tiles = new Tile[frame.getModel().getBoard().length][frame.getModel().getBoard()[0].length];
		setLayout(new GridLayout(frame.GridSize,frame.GridSize));
		for(int r = 0; r < frame.getModel().getBoard().length; r++) {
			for(int c = 0; c < frame.getModel().getBoard()[0].length; c++) {
				tiles[r][c] = new Tile(frame,r,c);
				add(tiles[r][c]);
			}
		}
		frame.repaint();
	}
	

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(frame.getModel().isInProgress()) {
			frame.flagDisplay.setText(frame.getModel().getNumOfFlagsRemaining()+" flags remain");
			for(int r = 0; r < frame.getModel().getBoard().length; r++) {
				for(int c = 0; c < frame.getModel().getBoard()[0].length; c++) {
					switch(frame.getModel().getTile(r,c)) {
						case 0: tiles[r][c].setIcon(frame.ZERO_TILE); break;
						case 1: tiles[r][c].setIcon(frame.ONE_TILE); break;
						case 2: tiles[r][c].setIcon(frame.TWO_TILE); break;
						case MinesweeperBoard.FLAG_TILE: tiles[r][c].setIcon(frame.FLAG_TILE); break;
						case MinesweeperBoard.QUESTION_TILE: tiles[r][c].setIcon(frame.QUESTION_TILE); break;
						case 3: tiles[r][c].setIcon(frame.THREE_TILE); break;
						case 4: tiles[r][c].setIcon(frame.FOUR_TILE); break;
						case 5: tiles[r][c].setIcon(frame.FIVE_TILE); break;
						case 6: tiles[r][c].setIcon(frame.SIX_TILE); break;
						case 7: tiles[r][c].setIcon(frame.SEVEN_TILE); break;
						case 8: tiles[r][c].setIcon(frame.EIGHT_TILE); break;
						case MinesweeperBoard.HIDDEN_TILE: tiles[r][c].setIcon(frame.HIDDEN_TILE); break;
						case MinesweeperBoard.MINE_TILE: tiles[r][c].setIcon(frame.MINE_TILE); break;
					}
				}
			}
		} else { 
			int status = frame.getModel().gameStatus();
			for(int r = 0; r < frame.getModel().getBoard().length; r++) {
				for(int c = 0; c < frame.getModel().getBoard()[0].length; c++) {
					switch(frame.getModel().getBoard()[r][c]) {
						case 0: tiles[r][c].setIcon(frame.ZERO_TILE); break;
						case 1: tiles[r][c].setIcon(frame.ONE_TILE); break;
						case 2: tiles[r][c].setIcon(frame.TWO_TILE); break;
						case 3: tiles[r][c].setIcon(frame.THREE_TILE); break;
						case 4: tiles[r][c].setIcon(frame.FOUR_TILE); break;
						case 5: tiles[r][c].setIcon(frame.FIVE_TILE); break;
						case 6: tiles[r][c].setIcon(frame.SIX_TILE); break;
						case 7: tiles[r][c].setIcon(frame.SEVEN_TILE); break;
						case 8: tiles[r][c].setIcon(frame.EIGHT_TILE); break;
						case MinesweeperBoard.MINE_TILE: tiles[r][c].setIcon(frame.MINE_TILE); break;
					}
				}
			}
			if(status == 1 && inProgress) {
				int result = JOptionPane.showConfirmDialog(null, "You win! You flagged all of the mines","Victory!", JOptionPane.DEFAULT_OPTION);
				inProgress = false;
				frame.setVisible(false);
				frame.setVisible(true);
			} else if(status == 0 && inProgress) {
				int result = JOptionPane.showConfirmDialog(null, "You lose! You ran into a mine","Defeat", JOptionPane.DEFAULT_OPTION);
				inProgress = false;
				frame.setVisible(false);
				frame.setVisible(true);
			}
		}
	}
}