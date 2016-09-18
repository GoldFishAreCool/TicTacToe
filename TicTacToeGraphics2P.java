import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")

public class TicTacToeGraphics2P extends JFrame 
{
	public static final int ROWS = 3; 
	public static final int COLS = 3; 
	
	public static final int CELL_SIZE = 100; 
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS; 
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS; 
	public static final int GRID_WIDTH = 4; 
	public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; 
	
	public static final int CELL_PADDING = CELL_SIZE / 8;
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	public enum GameState 
	{
		PLAYING, DRAW, CROSS_WON, ZERO_WON
	}
	
	private GameState currentState;
	
	public enum SEED 
	{
		EMPTY, CROSS, ZERO
	}
	
	private SEED currentPlayer;
	
	private SEED[][] board;
	private DrawCanvas canvas; 
	private JLabel statusBar; 
	
	public TicTacToeGraphics2P()
	{
		canvas = new DrawCanvas(); 
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT)); 
		
		canvas.addMouseListener(new MouseAdapter()
				{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				int rowSelected = mouseY / CELL_SIZE;
				int colSelected = mouseX / CELL_SIZE; 
				
				if (currentState == GameState.PLAYING) {
		               if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
		                     && colSelected < COLS && board[rowSelected][colSelected] == SEED.EMPTY)
		               {
		            	   board[rowSelected][colSelected] = currentPlayer; 
		            	   updateGame(currentPlayer, rowSelected, colSelected); 
		            	   
		            	   currentPlayer = (currentPlayer == SEED.CROSS) ? SEED.ZERO : SEED.CROSS;
		               }
			}
				else 
				{
					initGame();
				}
				
				repaint();
				}
				});
		  statusBar = new JLabel("  ");
	      statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 11));
	      statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
	 
	      Container cp = getContentPane();
	      cp.setLayout(new BorderLayout());
	      cp.add(canvas, BorderLayout.CENTER);
	      cp.add(statusBar, BorderLayout.PAGE_END);
	      
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	      pack(); 
	      setTitle("Tic Tac Toe"); 
	      setVisible(true);
	      
	      board = new SEED[ROWS][COLS]; 
	      initGame(); 
	}
	
	public void initGame()
	{
		for (int row = 0; row < ROWS; ++row)
		{
			for (int col = 0; col < COLS; ++col)
			{
				board[row][col] = SEED.EMPTY; 
			}
		}
		
		currentState = GameState.PLAYING;
		currentPlayer = SEED.CROSS; 
	}
	
	public void updateGame(SEED theSEED, int rowSelected, int colSelected)
	{
		if (hasWon(theSEED, rowSelected, colSelected))
		{
			currentState = (theSEED == SEED.CROSS) ? GameState.CROSS_WON : GameState.ZERO_WON;
		}
		else if (isDraw())
		{
			currentState = GameState.DRAW; 
		}
	}
	
	public boolean isDraw()
	{
		for (int row = 0; row < ROWS; ++row)
		{
			for (int col = 0; col < COLS; ++col)
			{
				if (board[row][col] == SEED.EMPTY)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasWon(SEED theSEED, int rowSelected, int colSelected)
	{
		return (board[rowSelected][0] == theSEED
	            && board[rowSelected][1] == theSEED
	            && board[rowSelected][2] == theSEED
	            || board[0][colSelected] == theSEED
	            && board[1][colSelected] == theSEED
	            && board[2][colSelected] == theSEED
	            || rowSelected == colSelected 
	            && board[rowSelected][1] == theSEED
	            && board[rowSelected][2] == theSEED
	            || rowSelected + colSelected == 2
	            && board[0][2] == theSEED
	            && board[1][1] == theSEED
	            && board[2][0] == theSEED
	            );	            
	} 
	
	class DrawCanvas extends JPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			setBackground(Color.darkGray); 
			
			g.setColor(Color.BLACK);
			for (int row = 1; row < ROWS; ++row)
			{
				g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF, CANVAS_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH); 
			}
			
			Graphics2D g2d = (Graphics2D)g; 
			g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			for (int row = 0; row < ROWS; ++row) 
			{
	            for (int col = 0; col < COLS; ++col) 
	            {
	               int x1 = col * CELL_SIZE + CELL_PADDING;
	               int y1 = row * CELL_SIZE + CELL_PADDING;
	               if (board[row][col] == SEED.CROSS) 
	               {
	                  g2d.setColor(Color.YELLOW);
	                  int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
	                  int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
	                  g2d.drawLine(x1, y1, x2, y2);
	                  g2d.drawLine(x2, y1, x1, y2);
	               } 
	               else if (board[row][col] == SEED.ZERO) 
	               {
	                  g2d.setColor(Color.GREEN);
	                  g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
	               }
	            }
	         }
			if (currentState == GameState.PLAYING) 
			{
	            statusBar.setForeground(Color.BLACK);
	            if (currentPlayer == SEED.CROSS) 
	            {
	               statusBar.setText("X's Turn");
	            } 
	            else 
	            {
	               statusBar.setText("O's Turn");
	            }
	         } 
			else if (currentState == GameState.DRAW) 
			{
	            statusBar.setForeground(Color.PINK);
	            statusBar.setText("It's a Draw, you both suck! Click to play again.");
	         } 
			else if (currentState == GameState.CROSS_WON) 
			{
	            statusBar.setForeground(Color.PINK);
	            statusBar.setText("'X' Won, fucker! Click to play again.");
	         } 
			else if (currentState == GameState.ZERO_WON) 
			{
	            statusBar.setForeground(Color.PINK);
	            statusBar.setText("'O' Won, sucker! Click to play again.");
	         }
	      }
	   }
	public static void main (String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
				{
				@Override
				public void run()
				{
			new TicTacToeGraphics2P();
				}
	});
	}
}