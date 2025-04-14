package main.java.com.chess.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;


public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        Position position = getPosition();

        if (position == null) {
            return validMoves;
        }

        int[][] directions = {
                {0, 1},    // Up
                {1, 0},    // Right
                {0, -1},   // Down
                {-1, 0}    // Left
        };

        for (int[] dir : directions) {
            int fileDir = dir[0];
            int rankDir = dir[1];

            Position currentPos = position;

            while (true) {
                currentPos = currentPos.offset(fileDir, rankDir);

                if (currentPos == null) {
                    break;
                }

                Piece pieceAtDestination = board.getPieceAt(currentPos);

                if (pieceAtDestination == null) {
                    validMoves.add(currentPos);
                } else {
                    if (pieceAtDestination.isWhite() != isWhite()) {
                        validMoves.add(currentPos);
                    }
                    break;
                }
            }
        }

        return validMoves;
    }

    @Override
    public Piece copy() {
        Rook copy = new Rook(isWhite());
        copy.setPosition(getPosition());
        copy.setMoved(hasMoved());
        return copy;
    }
}
