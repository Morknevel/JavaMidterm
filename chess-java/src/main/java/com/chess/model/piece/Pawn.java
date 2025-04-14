package main.java.com.chess.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;


public class Pawn extends Piece {


    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        Position position = getPosition();

        if (position == null) {
            return validMoves;
        }

        int direction = isWhite() ? 1 : -1;

        Position oneForward = position.offset(0, direction);
        if (oneForward != null && board.getPieceAt(oneForward) == null) {
            validMoves.add(oneForward);

            if (!hasMoved()) {
                Position twoForward = position.offset(0, 2 * direction);
                if (twoForward != null && board.getPieceAt(twoForward) == null) {
                    validMoves.add(twoForward);
                }
            }
        }

        Position captureLeft = position.offset(-1, direction);
        if (captureLeft != null) {
            Piece pieceLeft = board.getPieceAt(captureLeft);
            if (pieceLeft != null && pieceLeft.isWhite() != isWhite()) {
                validMoves.add(captureLeft);
            }
        }

        Position captureRight = position.offset(1, direction);
        if (captureRight != null) {
            Piece pieceRight = board.getPieceAt(captureRight);
            if (pieceRight != null && pieceRight.isWhite() != isWhite()) {
                validMoves.add(captureRight);
            }
        }


        return validMoves;
    }

    @Override
    public Piece copy() {
        Pawn copy = new Pawn(isWhite());
        copy.setPosition(getPosition());
        copy.setMoved(hasMoved());
        return copy;
    }
}

