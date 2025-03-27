package Chess;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class AIPlayer {
    private int difficulty;
    private boolean isBlack;
    private Random random = new Random();

    public AIPlayer(int difficulty, boolean isBlack) {
        this.difficulty = Math.max(1, Math.min(10, difficulty));
        this.isBlack = isBlack;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Piece makeMove(Board board) {
        List<Move> possibleMoves = getAllPossibleMoves(board);

        if (possibleMoves.isEmpty()) return null;

        Move selectedMove = findBestMove(board, possibleMoves);
        return executeMove(board, selectedMove);
    }

    private List<Move> getAllPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getIsBlack() == isBlack) {
                    for (int targetRow = 0; targetRow < 8; targetRow++) {
                        for (int targetCol = 0; targetCol < 8; targetCol++) {
                            if (piece.isMoveLegal(board, targetRow, targetCol)) {
                                moves.add(new Move(row, col, targetRow, targetCol));
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    private Move findBestMove(Board board, List<Move> possibleMoves) {
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        // First pass: look for king captures
        for (Move move : possibleMoves) {
            Piece target = board.getPiece(move.endRow, move.endCol);
            if (target instanceof King) {
                return move; // Immediately take the king if possible
            }
        }

        // Second pass: evaluate all other moves
        for (Move move : possibleMoves) {
            int score = evaluateMove(board, move);

            // Add some randomness based on difficulty
            if (difficulty < 10) {
                score += random.nextInt(difficulty * 2);
            }

            if (score > bestScore || bestMove == null) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int evaluateMove(Board board, Move move) {
        int score = 0;
        Piece piece = board.getPiece(move.startRow, move.startCol);
        Piece target = board.getPiece(move.endRow, move.endCol);

        // Material value (prioritize captures)
        if (target != null) {
            score += getPieceValue(target) * 5; // Increased multiplier for captures
        }

        // King safety (move towards opponent king)
        if (!isBlack) { // If AI is white, attack black king
            int[] blackKingPos = findKingPosition(board, true);
            if (blackKingPos != null) {
                int distanceToKing = Math.abs(move.endRow - blackKingPos[0]) +
                        Math.abs(move.endCol - blackKingPos[1]);
                score += (8 - distanceToKing) * 2; // Closer to king = better
            }
        } else { // If AI is black, attack white king
            int[] whiteKingPos = findKingPosition(board, false);
            if (whiteKingPos != null) {
                int distanceToKing = Math.abs(move.endRow - whiteKingPos[0]) +
                        Math.abs(move.endCol - whiteKingPos[1]);
                score += (8 - distanceToKing) * 2;
            }
        }

        // Center control
        if (isCenterSquare(move.endRow, move.endCol)) {
            score += 3;
        }

        // Piece development (encourage moving pieces from starting position)
        if ((piece instanceof Pawn || piece instanceof Knight) &&
                (move.startRow == (isBlack ? 6 : 1) || move.startCol == 0 || move.startCol == 7)) {
            score += 2;
        }

        // Pawn promotion potential
        if (piece instanceof Pawn && (move.endRow == 0 || move.endRow == 7)) {
            score += 8;
        }

        return score;
    }

    private int[] findKingPosition(Board board, boolean isBlack) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.getIsBlack() == isBlack) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    private int getPieceValue(Piece piece) {
        if (piece instanceof Pawn) return 1;
        if (piece instanceof Knight || piece instanceof Bishop) return 3;
        if (piece instanceof Rook) return 5;
        if (piece instanceof Queen) return 9;
        if (piece instanceof King) return 100; // King has high value but should never be captured
        return 0;
    }

    private boolean isCenterSquare(int row, int col) {
        return (row >= 3 && row <= 4 && col >= 3 && col <= 4);
    }

    private Piece executeMove(Board board, Move move) {
        Piece piece = board.getPiece(move.startRow, move.startCol);
        Piece captured = board.movePiece(move.startRow, move.startCol, move.endRow, move.endCol);
        piece.setPosition(move.endRow, move.endCol);

        if (piece instanceof Pawn && (move.endRow == 0 || move.endRow == 7)) {
            ((Pawn) piece).promotePawn(board, move.endRow, move.endCol);
        }

        return captured;
    }

    private static class Move {
        int startRow, startCol, endRow, endCol;

        Move(int startRow, int startCol, int endRow, int endCol) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }
    }
}