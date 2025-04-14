package main.java.com.chess.model.board;

import main.java.com.chess.model.piece.*;

public class Board {
    private final Square[][] squares;

    public Board() {
        this.squares = new Square[8][8];
        initializeEmptyBoard();
        setupInitialPosition();
    }

    public Board(boolean empty) {
        this.squares = new Square[8][8];
        initializeEmptyBoard();
        if (!empty) {
            setupInitialPosition();
        }
    }

    public Board(Board board) {
        this.squares = new Square[8][8];
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position pos = new Position(file, rank);
                squares[rank][file] = new Square(pos, null);
            }
        }
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position pos = new Position(file, rank);
                Piece piece = board.getPieceAt(pos);
                if (piece != null) {
                    Piece newPiece = piece.copy();
                    placePiece(newPiece, pos);
                }
            }
        }
    }

    private void initializeEmptyBoard() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                squares[rank][file] = new Square(new Position(file, rank), null);
            }
        }
    }

    private void setupInitialPosition() {
        for (int file = 0; file < 8; file++) {
            placePiece(new Pawn(true), new Position(file, 1));
            placePiece(new Pawn(false), new Position(file, 6));
        }
        placePiece(new Rook(true), new Position(0, 0));
        placePiece(new Rook(true), new Position(7, 0));
        placePiece(new Rook(false), new Position(0, 7));
        placePiece(new Rook(false), new Position(7, 7));
        placePiece(new Knight(true), new Position(1, 0));
        placePiece(new Knight(true), new Position(6, 0));
        placePiece(new Knight(false), new Position(1, 7));
        placePiece(new Knight(false), new Position(6, 7));
        placePiece(new Bishop(true), new Position(2, 0));
        placePiece(new Bishop(true), new Position(5, 0));
        placePiece(new Bishop(false), new Position(2, 7));
        placePiece(new Bishop(false), new Position(5, 7));
        placePiece(new Queen(true), new Position(3, 0));
        placePiece(new Queen(false), new Position(3, 7));
        placePiece(new King(true), new Position(4, 0));
        placePiece(new King(false), new Position(4, 7));
    }

    public Square getSquare(Position position) {
        return squares[position.getRank()][position.getFile()];
    }

    public Piece getPieceAt(Position position) {
        return getSquare(position).getPiece();
    }

    public void placePiece(Piece piece, Position position) {
        Square square = getSquare(position);
        square.setPiece(piece);
        if (piece != null) {
            piece.setPosition(position);
        }
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        if (piece != null) {
            placePiece(null, from);
            placePiece(piece, to);
            piece.setMoved(true);
        }
    }

    public boolean isValidPosition(Position position) {
        return position != null &&
                position.getFile() >= 0 && position.getFile() < 8 &&
                position.getRank() >= 0 && position.getRank() < 8;
    }

    public Position findKing(boolean isWhite) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position pos = new Position(file, rank);
                Piece piece = getPieceAt(pos);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return pos;
                }
            }
        }
        return null;
    }
}