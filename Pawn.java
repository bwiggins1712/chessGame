package Chess;

import javax.swing.JOptionPane;

public class Pawn extends Piece {
    public Pawn(int row, int col, boolean isBlack) {
        super(row, col, isBlack);
        this.representation = isBlack ? '♟' : '♙'; // Using Unicode chess symbols
    }

    @Override
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        int direction = isBlack ? 1 : -1;
        int startRow = this.row;
        int startCol = this.col;

        // Normal move forward (1 or 2 squares)
        if (endCol == startCol) {
            // Single square forward
            if (endRow == startRow + direction && board.getPiece(endRow, endCol) == null) {
                return true;
            }
            // Double square from starting position
            if ((isBlack && startRow == 1) || (!isBlack && startRow == 6)) {
                if (endRow == startRow + 2 * direction &&
                        board.getPiece(startRow + direction, startCol) == null &&
                        board.getPiece(endRow, endCol) == null) {
                    return true;
                }
            }
        }

        // Capture move (diagonal)
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction) {
            Piece target = board.getPiece(endRow, endCol);
            // Normal capture or en passant (would be added here)
            return target != null && target.isBlack != this.isBlack;
        }

        return false;
    }

    public boolean promotePawn(Board board, int row, int col) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Promote pawn to:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) {
            return false; // User closed dialog
        }

        Piece promotedPiece = switch (choice) {
            case 0 -> new Queen(row, col, isBlack);
            case 1 -> new Rook(row, col, isBlack);
            case 2 -> new Bishop(row, col, isBlack);
            case 3 -> new Knight(row, col, isBlack);
            default -> null;
        };

        if (promotedPiece != null) {
            board.setPiece(row, col, promotedPiece);
            return true;
        }
        return false;
    }
}
