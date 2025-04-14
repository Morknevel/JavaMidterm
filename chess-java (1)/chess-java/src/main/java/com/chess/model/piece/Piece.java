package main.java.com.chess.model.piece;


import java.util.List;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.board.Board;

public abstract class Piece {
    private final boolean isWhite;
    private Position position;
    private boolean hasMoved;


    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        this.hasMoved = false;
    }


    public boolean isWhite() {
        return isWhite;
    }

    public Position getPosition() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }


    public boolean hasMoved() {
        return hasMoved;
    }


    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }


    public abstract List<Position> getValidMoves(Board board);


    public abstract PieceType getType();


    public abstract Piece copy();

    public enum PieceType {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
    }

    @Override
    public String toString() {
        return (isWhite ? "White " : "Black ") + getType();
    }
}
