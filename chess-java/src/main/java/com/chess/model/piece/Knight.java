package main.java.com.chess.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;

public class Knight extends Piece {


    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        Position position = getPosition();

        if (position == null) {
            return validMoves;
        }

        int[][] knightMoves = {
                {2, 1},
                {1, 2},
                {-1, 2},
                {-2, 1},
                {-2, -1},
                {-1, -2},
                {1, -2},
                {2, -1}
        };

        for (int[] move : knightMoves) {
            Position newPos = position.offset(move[0], move[1]);

            if (newPos != null) {
                Piece pieceAtDestination = board.getPieceAt(newPos);

                if (pieceAtDestination == null || pieceAtDestination.isWhite() != isWhite()) {
                    validMoves.add(newPos);
                }
            }
        }

        return validMoves;
    }

    @Override
    public Piece copy() {
        Knight copy = new Knight(isWhite());
        copy.setPosition(getPosition());
        copy.setMoved(hasMoved());
        return copy;
    }
}

