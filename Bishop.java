package Chess;

public class Bishop extends Piece {
    public Bishop(int row, int col, boolean isBlack) {
        super(row, col, isBlack);
        this.representation = isBlack ? '♝' : '♗';
    }

    @Override
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        // Bishops move diagonally
        if (Math.abs(endRow - row) != Math.abs(endCol - col)) {
            return false;
        }

        // Check if path is clear
        int rowStep = endRow > row ? 1 : -1;
        int colStep = endCol > col ? 1 : -1;
        int steps = Math.abs(endRow - row);

        for (int i = 1; i < steps; i++) {
            if (board.getPiece(row + i*rowStep, col + i*colStep) != null) {
                return false;
            }
        }

        // Check destination
        Piece target = board.getPiece(endRow, endCol);
        return target == null || target.getIsBlack() != isBlack;
    }
}