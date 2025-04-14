package main.java.com.chess.model.rules;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.piece.King;
import main.java.com.chess.model.piece.Pawn;
import main.java.com.chess.model.piece.Piece;
import main.java.com.chess.model.piece.Rook;

public class MoveValidator {

    public boolean isValidMove(Board board, Position from, Position to) {
        return isValidMove(board, from, to, null);
    }


    public boolean isValidMove(Board board, Position from, Position to, Position lastPawnDoubleMovePosition) {
        // Check if positions are valid
        if (!board.isValidPosition(from) || !board.isValidPosition(to)) {
            return false;
        }

        // Check if there is a piece to move
        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            return false;
        }

        // Check if destination has a piece of the same color
        Piece destPiece = board.getPieceAt(to);
        if (destPiece != null && destPiece.isWhite() == piece.isWhite()) {
            return false;
        }

        // Check for castling
        if (isCastlingMove(board, from, to)) {
            return isCastlingValid(board, from, to);
        }

        // Check for en passant
        if (isEnPassantMove(board, from, to, lastPawnDoubleMovePosition)) {
            return isEnPassantValid(board, from, to, lastPawnDoubleMovePosition);
        }

        // Check if the move is valid for this piece type
        List<Position> validMoves = piece.getValidMoves(board);
        if (!validMoves.contains(to)) {
            return false;
        }

        // Check if the move would leave the king in check
        if (wouldBeInCheckAfterMove(board, from, to, piece.isWhite())) {
            return false;
        }

        return true;
    }

    public boolean isInCheck(Board board, boolean isWhite) {
        // Find the king
        Position kingPosition = board.findKing(isWhite);
        if (kingPosition == null) {
            return false;
        }

        // Check if any opponent piece can attack the king
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position pos = new Position(file, rank);
                Piece piece = board.getPieceAt(pos);

                if (piece != null && piece.isWhite() != isWhite) {
                    // Check if this piece can attack the king
                    List<Position> validMoves = piece.getValidMoves(board);
                    if (validMoves.contains(kingPosition)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean hasValidMoves(Board board, boolean isWhite, Position lastPawnDoubleMovePosition) {
        for (int fromRank = 0; fromRank < 8; fromRank++) {
            for (int fromFile = 0; fromFile < 8; fromFile++) {
                Position from = new Position(fromFile, fromRank);
                Piece piece = board.getPieceAt(from);

                if (piece != null && piece.isWhite() == isWhite) {
                    List<Position> possibleMoves = piece.getValidMoves(board);

                    for (Position to : possibleMoves) {
                        if (isValidMove(board, from, to, lastPawnDoubleMovePosition)) {
                            return true;
                        }
                    }

                    // Check for castling
                    if (piece instanceof King && !piece.hasMoved()) {
                        Position kingsideCastling = new Position(from.getFile() + 2, from.getRank());
                        Position queensideCastling = new Position(from.getFile() - 2, from.getRank());

                        if (isCastlingValid(board, from, kingsideCastling)) {
                            return true;
                        }

                        if (isCastlingValid(board, from, queensideCastling)) {
                            return true;
                        }
                    }

                    // Check for en passant
                    if (piece instanceof Pawn && lastPawnDoubleMovePosition != null) {
                        int direction = piece.isWhite() ? 1 : -1;
                        Position leftCapture = from.offset(-1, direction);
                        Position rightCapture = from.offset(1, direction);

                        if (leftCapture != null && isEnPassantValid(board, from, leftCapture, lastPawnDoubleMovePosition)) {
                            return true;
                        }

                        if (rightCapture != null && isEnPassantValid(board, from, rightCapture, lastPawnDoubleMovePosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    private boolean wouldBeInCheckAfterMove(Board board, Position from, Position to, boolean isWhite) {
        // Create a deep copy of the board
        Board tempBoard = new Board(board);

        // Make the move on the temporary board
        tempBoard.movePiece(from, to);

        // Check if the king is in check
        return isInCheck(tempBoard, isWhite);
    }


    public boolean isCastlingMove(Board board, Position from, Position to) {
        Piece piece = board.getPieceAt(from);

        // Must be a king
        if (!(piece instanceof King)) {
            return false;
        }

        // Must be a two-square horizontal move
        return from.getRank() == to.getRank() && Math.abs(to.getFile() - from.getFile()) == 2;
    }

    public boolean isCastlingValid(Board board, Position from, Position to) {
        Piece king = board.getPieceAt(from);

        // Must be a king
        if (!(king instanceof King)) {
            return false;
        }

        // King must not have moved
        if (king.hasMoved()) {
            return false;
        }

        // King must not be in check
        if (isInCheck(board, king.isWhite())) {
            return false;
        }

        // Determine the rook position and squares between king and rook
        int rookFile;
        List<Position> squaresBetween = new ArrayList<>();

        // Kingside castling
        if (to.getFile() > from.getFile()) {
            rookFile = 7;
            for (int file = from.getFile() + 1; file < rookFile; file++) {
                squaresBetween.add(new Position(file, from.getRank()));
            }
        }
        // Queenside castling
        else {
            rookFile = 0;
            for (int file = from.getFile() - 1; file > rookFile; file--) {
                squaresBetween.add(new Position(file, from.getRank()));
            }
        }

        Position rookPos = new Position(rookFile, from.getRank());
        Piece rook = board.getPieceAt(rookPos);

        // Check if there is a rook that hasn't moved
        if (!(rook instanceof Rook) || rook.hasMoved() || rook.isWhite() != king.isWhite()) {
            return false;
        }

        // Check if squares between king and rook are empty
        for (Position pos : squaresBetween) {
            if (board.getPieceAt(pos) != null) {
                return false;
            }
        }

        // Check if the king passes through or ends up in check
        for (Position pos : squaresBetween) {
            if (wouldBeInCheckAfterMove(board, from, pos, king.isWhite())) {
                return false;
            }
        }

        // Check if the final position would put the king in check
        if (wouldBeInCheckAfterMove(board, from, to, king.isWhite())) {
            return false;
        }

        return true;
    }


    public boolean isEnPassantMove(Board board, Position from, Position to, Position lastPawnDoubleMovePosition) {
        if (lastPawnDoubleMovePosition == null) {
            return false;
        }

        Piece piece = board.getPieceAt(from);

        // Must be a pawn
        if (!(piece instanceof Pawn)) {
            return false;
        }

        // Must be a diagonal move to an empty square
        if (Math.abs(to.getFile() - from.getFile()) != 1 || Math.abs(to.getRank() - from.getRank()) != 1) {
            return false;
        }

        if (board.getPieceAt(to) != null) {
            return false;
        }

        // The square of the captured pawn must be the last double move position
        return to.getFile() == lastPawnDoubleMovePosition.getFile() &&
                from.getRank() == lastPawnDoubleMovePosition.getRank();
    }


    public boolean isEnPassantValid(Board board, Position from, Position to, Position lastPawnDoubleMovePosition) {
        if (!isEnPassantMove(board, from, to, lastPawnDoubleMovePosition)) {
            return false;
        }

        // Create a temporary board to check if the move would leave the king in check
        Board tempBoard = new Board(board);

        // Move the pawn
        tempBoard.movePiece(from, to);

        // Remove the captured pawn
        Position capturedPawnPos = new Position(to.getFile(), from.getRank());
        tempBoard.placePiece(null, capturedPawnPos);

        // Check if the king is in check
        Piece piece = board.getPieceAt(from);
        return !isInCheck(tempBoard, piece.isWhite());
    }
}