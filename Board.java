package Chess;

import java.awt.*;
import javax.swing.*;

public class Board extends JPanel {
    private Piece[][] board;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public Board() {
        this.board = new Piece[8][8];
    }

    public Piece getPiece(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            return board[row][col];
        }
        return null;
    }

    public void setPiece(int row, int col, Piece piece) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            board[row][col] = piece;
            if (piece != null) {
                piece.setPosition(row, col);
            }
        }
    }

    public Piece movePiece(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = getPiece(startRow, startCol);
        if (piece == null) return null;

        Piece captured = getPiece(endRow, endCol);
        setPiece(endRow, endCol, piece);
        setPiece(startRow, startCol, null);
        return captured;
    }

    public void setSelectedPosition(int row, int col) {
        this.selectedRow = row;
        this.selectedCol = col;
    }

    public void reset() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        selectedRow = -1;
        selectedCol = -1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth(), getHeight()) / 8;
        Font pieceFont = new Font("Arial", Font.PLAIN, cellSize - 10);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == selectedRow && col == selectedCol) {
                    g.setColor(Color.YELLOW);
                } else if ((row + col) % 2 == 0) {
                    g.setColor(new Color(240, 217, 181));
                } else {
                    g.setColor(new Color(181, 136, 99));
                }
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

                Piece piece = board[row][col];
                if (piece != null) {
                    g.setColor(piece.getIsBlack() ? Color.BLACK : Color.WHITE);
                    g.setFont(pieceFont);
                    g.drawString(piece.toString(),
                            col * cellSize + cellSize / 4,
                            row * cellSize + cellSize * 3 / 4);
                }
            }
        }
    }
}