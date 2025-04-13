package main.java.com.chess.model.board;

import main.java.com.chess.model.piece.Piece;

public class Square {
    private final Position position;
    private Piece piece;

    public Square(Position position, Piece piece) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = position;
        this.piece = piece;
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public boolean hasPieceOfColor(boolean isWhite) {
        return piece != null && piece.isWhite() == isWhite;
    }

    @Override
    public String toString() {
        return position.toString() + (piece != null ? ":" + piece : ":empty");
    }
}