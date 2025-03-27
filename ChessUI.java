package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessUI extends JFrame {
    private Board board;
    private Piece selectedPiece;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isWhiteTurn = true;
    private boolean gameOver = false;

    public ChessUI() {
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        board = new Board();
        Fen.load("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", board);
    }

    private void setupUI() {
        setTitle("Chess Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) return;

                int cellSize = Math.min(board.getWidth(), board.getHeight()) / 8;
                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;

                if (selectedPiece == null) {
                    // Select piece
                    selectedPiece = board.getPiece(row, col);
                    if (selectedPiece != null && selectedPiece.getIsBlack() == !isWhiteTurn) {
                        selectedRow = row;
                        selectedCol = col;
                        board.setSelectedPosition(row, col);
                        board.repaint();
                    } else {
                        selectedPiece = null;
                    }
                } else {
                    // Attempt move
                    if (selectedPiece.isMoveLegal(board, row, col)) {
                        board.movePiece(selectedRow, selectedCol, row, col);
                        selectedPiece.setPosition(row, col);

                        // Pawn promotion
                        if (selectedPiece instanceof Pawn && (row == 0 || row == 7)) {
                            ((Pawn) selectedPiece).promotePawn(board, row, col);
                        }

                        isWhiteTurn = !isWhiteTurn;

                        if (isGameOver()) {
                            gameOver = true;
                            JOptionPane.showMessageDialog(ChessUI.this,
                                    (isWhiteTurn ? "Black" : "White") + " wins!");
                        }
                    }
                    // Reset selection
                    selectedPiece = null;
                    selectedRow = -1;
                    selectedCol = -1;
                    board.setSelectedPosition(-1, -1);
                    board.repaint();
                }
            }
        });

        add(board, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean isGameOver() {
        // Implement game over logic
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessUI::new);
    }
}
