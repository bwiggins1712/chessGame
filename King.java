package Chess;

public class King extends Piece {
    public King(int row, int col, boolean isBlack) {
        super(row, col, isBlack);
        this.representation = isBlack ? '♚' : '♔';
    }

    @Override
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        // King moves one square in any direction
        int rowDiff = Math.abs(endRow - row);
        int colDiff = Math.abs(endCol - col);

        if (rowDiff > 1 || colDiff > 1) {
            return false;
        }

        // Check destination
        Piece target = board.getPiece(endRow, endCol);
        return target == null || target.getIsBlack() != isBlack;
    }
}