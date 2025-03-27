package Chess;

public class Rook extends Piece {
    public Rook(int row, int col, boolean isBlack) {
        super(row, col, isBlack);
        this.representation = isBlack ? '♜' : '♖';
    }

    @Override
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        // Rooks move in straight lines
        if (row != endRow && col != endCol) {
            return false; // Not moving in a straight line
        }

        // Check if path is clear
        if (row == endRow) { // Horizontal move
            int colStep = endCol > col ? 1 : -1;
            for (int c = col + colStep; c != endCol; c += colStep) {
                if (board.getPiece(row, c) != null) {
                    return false;
                }
            }
        } else { // Vertical move
            int rowStep = endRow > row ? 1 : -1;
            for (int r = row + rowStep; r != endRow; r += rowStep) {
                if (board.getPiece(r, col) != null) {
                    return false;
                }
            }
        }

        // Check destination
        Piece target = board.getPiece(endRow, endCol);
        return target == null || target.getIsBlack() != isBlack;
    }
}