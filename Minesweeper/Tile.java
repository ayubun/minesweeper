import javax.swing.*;
import java.awt.event.*;
/**
 * @author Ayu
 * @website https://ayu.dev/
 */
public class Tile extends JButton {
	
	MinesweeperFrame frame;
	private final int row, col;
	
	public Tile(MinesweeperFrame frame, int row, int col) {
		super();
		addMouseListener(new MouseHandler());
		this.frame = frame;
		this.row = row;
		this.col = col;
		setIcon(frame.HIDDEN_TILE);
	}
	private class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(frame.getModel().isInProgress()) {
				if(e.getButton() == 3) {
					if(frame.getModel().getTile(row,col) == frame.getModel().HIDDEN_TILE) {
						frame.getModel().flag(row,col);
					} else if(frame.getModel().getTile(row,col) == frame.getModel().FLAG_TILE) {
						frame.getModel().question(row,col);
					} else if(frame.getModel().getTile(row,col) == frame.getModel().QUESTION_TILE) {
						frame.getModel().question(row,col);
					}
				} else if(e.getButton() == 1) {
					if(frame.getModel().getTile(row,col) == frame.getModel().HIDDEN_TILE || frame.getModel().getTile(row,col) == frame.getModel().QUESTION_TILE) {
						frame.getModel().reveal(row,col);
					}
				}
				frame.repaint();
			}
			
		}
	}
}
