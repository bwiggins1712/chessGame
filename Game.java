package Chess;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {
    private boolean isWhiteTurn = true;
    private boolean gameOver = false;
    private Board board;
    private JFrame frame;
    private int[] selectedPosition = null;
    private AIPlayer aiPlayer;
    private boolean vsAI = false;
    private Timer aiTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Object[] options = {"Human", "AI"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Play against human or AI?",
                    "Game Mode",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            Game game = new Game();
            if (choice == 1) {
                String input = JOptionPane.showInputDialog("Select AI difficulty (1-10):");
                try {
                    int difficulty = Integer.parseInt(input);
                    game.initAI(Math.max(1, Math.min(10, difficulty)));
                } catch (NumberFormatException e) {
                    game.initAI(5);
                }
            }
            game.initialize();
        });
    }

    private void initialize() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        Fen.load("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", board);

        frame.getContentPane().add(board);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver || (vsAI && !isWhiteTurn)) return;

                int cellSize = Math.min(board.getWidth(), board.getHeight()) / 8;
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                if (selectedPosition == null) {
                    Piece piece = board.getPiece(row, col);
                    if (piece != null && piece.getIsBlack() == !isWhiteTurn) {
                        selectedPosition = new int[]{row, col};
                        board.setSelectedPosition(row, col);
                        board.repaint();
                    }
                } else {
                    int startRow = selectedPosition[0];
                    int startCol = selectedPosition[1];
                    Piece piece = board.getPiece(startRow, startCol);

                    if (piece != null && piece.isMoveLegal(board, row, col)) {
                        Piece captured = board.movePiece(startRow, startCol, row, col);

                        // Check for king capture immediately
                        if (captured instanceof King) {
                            gameOver = true;
                            String winner = isWhiteTurn ? "White" : "Black";
                            JOptionPane.showMessageDialog(frame, winner + " wins by capturing the king!");
                            promptNewGame();
                            return;
                        }

                        // Handle pawn promotion only if game isn't over
                        if (piece instanceof Pawn && (row == 0 || row == 7)) {
                            ((Pawn) piece).promotePawn(board, row, col);
                        }

                        isWhiteTurn = !isWhiteTurn;
                        board.repaint();

                        // Check for other win conditions
                        if (isGameOver()) {
                            gameOver = true;
                            JOptionPane.showMessageDialog(frame, (isWhiteTurn ? "Black" : "White") + " wins!");
                            promptNewGame();
                        }
                    }
                    selectedPosition = null;
                    board.setSelectedPosition(-1, -1);
                    board.repaint();
                }
            }
        });
    }

    private boolean isGameOver() {
        boolean whiteKingFound = false;
        boolean blackKingFound = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King) {
                    if (piece.getIsBlack()) {
                        blackKingFound = true;
                    } else {
                        whiteKingFound = true;
                    }
                }
            }
        }
        return !whiteKingFound || !blackKingFound;
    }

    private void promptNewGame() {
        int choice = JOptionPane.showOptionDialog(frame,
                "Would you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Play Again", "Quit"},
                "Play Again");

        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        isWhiteTurn = true;
        gameOver = false;
        selectedPosition = null;
        board.reset();
        Fen.load("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", board);

        if (vsAI) {
            aiTimer.stop();
            initAI(aiPlayer.getDifficulty());
        }

        board.repaint();
    }

    private void initAI(int difficulty) {
        aiPlayer = new AIPlayer(difficulty, true);
        vsAI = true;
        aiTimer = new Timer(1000, e -> {
            if (!isWhiteTurn && vsAI && !gameOver) {
                SwingUtilities.invokeLater(() -> {
                    Piece captured = aiPlayer.makeMove(board);

                    if (captured instanceof King) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(frame, "Black wins by capturing the king!");
                        promptNewGame();
                        return;
                    }

                    isWhiteTurn = true;
                    board.repaint();

                    if (isGameOver()) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(frame, "White wins!");
                        promptNewGame();
                    }
                });
            }
        });
        aiTimer.start();
    }
}