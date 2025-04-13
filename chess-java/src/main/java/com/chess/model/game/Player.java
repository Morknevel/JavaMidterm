package main.java.com.chess.model.game;

import java.util.ArrayList;
import java.util.List;
import main.java.com.chess.model.piece.Piece;

public class Player {
    private final boolean isWhite;
    private final List<Piece> capturedPieces;

    public Player(boolean isWhite) {
        this.isWhite = isWhite;
        this.capturedPieces = new ArrayList<>();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public List<Piece> getCapturedPieces() {
        return new ArrayList<>(capturedPieces);
    }

    public void addCapturedPiece(Piece piece) {
        if (piece != null) {
            capturedPieces.add(piece);
        }
    }

    public void clearCapturedPieces() {
        capturedPieces.clear();
    }

    @Override
    public String toString() {
        return (isWhite ? "White" : "Black") + " Player";
    }
}