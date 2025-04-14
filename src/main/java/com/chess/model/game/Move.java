package main.java.com.chess.model.game;

import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.piece.Piece;


public class Move {
    private final Position from;
    private final Position to;
    private final Piece movingPiece;
    private final Piece capturedPiece;
    private final boolean isPromotion;
    private final boolean isCastling;
    private final boolean isEnPassant;
    private Piece promotedToPiece; // For pawn promotion


    public Move(Position from, Position to, Piece movingPiece, Piece capturedPiece) {
        this(from, to, movingPiece, capturedPiece, false, false, false);
    }


    public Move(Position from, Position to, Piece movingPiece, Piece capturedPiece,
                boolean isPromotion, boolean isCastling, boolean isEnPassant) {
        if (from == null || to == null || movingPiece == null) {
            throw new IllegalArgumentException("Move must have valid from, to, and movingPiece");
        }
        this.from = from;
        this.to = to;
        this.movingPiece = movingPiece;
        this.capturedPiece = capturedPiece;
        this.isPromotion = isPromotion;
        this.isCastling = isCastling;
        this.isEnPassant = isEnPassant;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isCapture() {
        return capturedPiece != null;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    public Piece getPromotedToPiece() {
        return promotedToPiece;
    }

    public void setPromotedToPiece(Piece promotedToPiece) {
        this.promotedToPiece = promotedToPiece;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(movingPiece.getType()).append(": ")
                .append(from).append(" -> ").append(to);

        if (isCapture()) {
            sb.append(" captures ").append(capturedPiece.getType());
        }

        if (isPromotion) sb.append(" (promotion)");
        if (isCastling) sb.append(" (castling)");
        if (isEnPassant) sb.append(" (en passant)");

        return sb.toString();
    }
}

