package main.java.com.chess.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;

public class Queen extends Piece {


    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        Position position = getPosition();

        if (position == null) {
            return validMoves;
        }

        // Queen moves like a rook and bishop combined

        // Horizontal and vertical movements (like a rook)
        int[][] rookDirections = {
                {0, 1},    // Up
                {1, 0},    // Right
                {0, -1},   // Down
                {-1, 0}    // Left
        };

        for (int[] dir : rookDirections) {
            int fileDir = dir[0];
            int rankDir = dir[1];

            Position currentPos = position;

            while (true) {
                currentPos = currentPos.offset(fileDir, rankDir);

                if (currentPos == null) {
                    break; // Off the board
                }

                Piece pieceAtDestination = board.getPieceAt(currentPos);

                if (pieceAtDestination == null) {
                    // Empty square, can move
                    validMoves.add(currentPos);
                } else {
                    // Square has a piece
                    if (pieceAtDestination.isWhite() != isWhite()) {
                        // Can capture opponent's piece
                        validMoves.add(currentPos);
                    }
                    break; // Can't move further in this direction
                }
            }
        }

        // Diagonal movements (like a bishop)
        int[][] bishopDirections = {
                {1, 1},    // Up-right
                {1, -1},   // Down-right
                {-1, -1},  // Down-left
                {-1, 1}    // Up-left
        };

        for (int[] dir : bishopDirections) {
            int fileDir = dir[0];
            int rankDir = dir[1];

            Position currentPos = position;

            while (true) {
                currentPos = currentPos.offset(fileDir, rankDir);

                if (currentPos == null) {
                    break; // Off the board
                }

                Piece pieceAtDestination = board.getPieceAt(currentPos);

                if (pieceAtDestination == null) {
                    // Empty square, can move
                    validMoves.add(currentPos);
                } else {
                    // Square has a piece
                    if (pieceAtDestination.isWhite() != isWhite()) {
                        // Can capture opponent's piece
                        validMoves.add(currentPos);
                    }
                    break; // Can't move further in this direction
                }
            }
        }

        return validMoves;
    }

    @Override
    public Piece copy() {
        Queen copy = new Queen(isWhite());
        copy.setPosition(getPosition());
        copy.setMoved(hasMoved());
        return copy;
    }
}

