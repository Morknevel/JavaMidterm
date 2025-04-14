package main.java.com.chess.model.piece;


import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        Position position = getPosition();

        if (position == null) {
            return validMoves;
        }

        // Bishop moves diagonally
        int[][] directions = {
                {1, 1},    // Up-right
                {1, -1},   // Down-right
                {-1, -1},  // Down-left
                {-1, 1}    // Up-left
        };

        for (int[] dir : directions) {
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
        Bishop copy = new Bishop(isWhite());
        copy.setPosition(getPosition());
        copy.setMoved(hasMoved());
        return copy;
    }
}

