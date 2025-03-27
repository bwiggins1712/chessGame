package Chess;

public abstract class Piece {
    protected int row;
    protected int col;
    protected boolean isBlack;
    protected char representation;

    public Piece(int row, int col, boolean isBlack) {
        this.row = row;
        this.col = col;
        this.isBlack = isBlack;
    }

    public abstract boolean isMoveLegal(Board board, int endRow, int endCol);

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getIsBlack() {
        return isBlack;
    }

    public String toString() {
        return String.valueOf(representation);
    }
}