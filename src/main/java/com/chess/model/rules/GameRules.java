package main.java.com.chess.model.rules;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.piece.King;
import main.java.com.chess.model.piece.Piece;

class GameRules {
    private final MoveValidator moveValidator;


    public GameRules() {
        this.moveValidator = new MoveValidator();
    }

    public boolean isCheckmate(Board board, boolean isWhite, Position lastPawnDoubleMovePosition) {
        return moveValidator.isInCheck(board, isWhite) &&
                !moveValidator.hasValidMoves(board, isWhite, lastPawnDoubleMovePosition);
    }


    public boolean isStalemate(Board board, boolean isWhite, Position lastPawnDoubleMovePosition) {
        return !moveValidator.isInCheck(board, isWhite) &&
                !moveValidator.hasValidMoves(board, isWhite, lastPawnDoubleMovePosition);
    }


    public boolean isCastlingMove(Board board, Position from, Position to) {
        return moveValidator.isCastlingMove(board, from, to);
    }


    public boolean isPawnPromotion(Board board, Position from, Position to) {
        Piece piece = board.getPieceAt(from);

        if (piece == null || piece.getType() != Piece.PieceType.PAWN) {
            return false;
        }

        int promotionRank = piece.isWhite() ? 7 : 0;
        return to.getRank() == promotionRank;
    }
}