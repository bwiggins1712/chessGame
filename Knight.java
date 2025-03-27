package Chess;

public class Knight extends Piece {
    public Knight(int row, int col, boolean isBlack) {
        super(row, col, isBlack);
        this.representation = isBlack ? '♞' : '♘';
    }

    @Override
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        // Knights move in L-shapes
        int rowDiff = Math.abs(endRow - row);
        int colDiff = Math.abs(endCol - col);

        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }

        // Check destination
        Piece target = board.getPiece(endRow, endCol);
        return target == null || target.getIsBlack() != isBlack;
    }
}