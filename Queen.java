

package Chess;

public class Queen extends Piece {
    public Queen(int row, int col, boolean isBlack) {
        super(row, col, isBlack);
        this.representation = isBlack ? '♛' : '♕';
    }

    @Override
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        // Queen moves like rook + bishop
        int rowDiff = Math.abs(endRow - row);
        int colDiff = Math.abs(endCol - col);

        // Straight movement (rook-like)
        if (row == endRow || col == endCol) {
            if (row == endRow) { // Horizontal
                int colStep = endCol > col ? 1 : -1;
                for (int c = col + colStep; c != endCol; c += colStep) {
                    if (board.getPiece(row, c) != null) return false;
                }
            } else { // Vertical
                int rowStep = endRow > row ? 1 : -1;
                for (int r = row + rowStep; r != endRow; r += rowStep) {
                    if (board.getPiece(r, col) != null) return false;
                }
            }
        }
        // Diagonal movement (bishop-like)
        else if (rowDiff == colDiff) {
            int rowStep = endRow > row ? 1 : -1;
            int colStep = endCol > col ? 1 : -1;
            for (int i = 1; i < rowDiff; i++) {
                if (board.getPiece(row + i*rowStep, col + i*colStep) != null) {
                    return false;
                }
            }
        } else {
            return false;
        }

        // Check destination
        Piece target = board.getPiece(endRow, endCol);
        return target == null || target.getIsBlack() != isBlack;
    }
}