package main.java.com.chess.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        Position position = getPosition();

        if (position == null) {
            return validMoves;
        }

        // King can move one square in any direction
        int[] offsets = {-1, 0, 1};

        for (int fileOffset : offsets) {
            for (int rankOffset : offsets) {
                // Skip the current position (0,0 offset)
                if (fileOffset == 0 && rankOffset == 0) {
                    continue;
                }

                Position newPos = position.offset(fileOffset, rankOffset);

                if (newPos != null) {
                    Piece pieceAtDestination = board.getPieceAt(newPos);

                    // Can move if the square is empty or has an opponent's piece
                    if (pieceAtDestination == null || pieceAtDestination.isWhite() != isWhite()) {
                        validMoves.add(newPos);
                    }
                }
            }
        }

        // Castling logic
        if (!hasMoved()) {
            // Kingside castling
            Position kingsideRookPos = new Position(7, position.getRank());
            Piece kingsideRook = board.getPieceAt(kingsideRookPos);

            if (kingsideRook instanceof Rook && !kingsideRook.hasMoved()) {
                boolean pathClear = true;

                // Check if squares between king and rook are empty
                for (int file = position.getFile() + 1; file < 7; file++) {
                    if (board.getPieceAt(new Position(file, position.getRank())) != null) {
                        pathClear = false;
                        break;
                    }
                }

                if (pathClear) {
                    validMoves.add(new Position(position.getFile() + 2, position.getRank()));
                }
            }

            // Queenside castling
            Position queensideRookPos = new Position(0, position.getRank());
            Piece queensideRook = board.getPieceAt(queensideRookPos);

            if (queensideRook instanceof Rook && !queensideRook.hasMoved()) {
                boolean pathClear = true;

                // Check if squares between king and rook are empty
                for (int file = position.getFile() - 1; file > 0; file--) {
                    if (board.getPieceAt(new Position(file, position.getRank())) != null) {
                        pathClear = false;
                        break;
                    }
                }

                if (pathClear) {
                    validMoves.add(new Position(position.getFile() - 2, position.getRank()));
                }
            }
        }

        return validMoves;
    }

    @Override
    public Piece copy() {
        King copy = new King(isWhite());
        copy.setPosition(getPosition());
        copy.setMoved(hasMoved());
        return copy;
    }
}
