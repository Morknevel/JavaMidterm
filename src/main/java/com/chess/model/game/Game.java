package main.java.com.chess.model.game;

import java.util.ArrayList;
import java.util.List;
import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.piece.Piece;
import main.java.com.chess.model.piece.Queen;
import main.java.com.chess.model.rules.MoveValidator;

public class Game {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean isWhiteTurn;
    private GameState gameState;
    private MoveValidator moveValidator;
    private List<Move> moveHistory;
    private Position lastPawnDoubleMovePosition;

    public Game() {
        this.board = new Board();
        this.whitePlayer = new Player(true);
        this.blackPlayer = new Player(false);
        this.isWhiteTurn = true;
        this.gameState = GameState.IN_PROGRESS;
        this.moveValidator = new MoveValidator();
        this.moveHistory = new ArrayList<>();
        this.lastPawnDoubleMovePosition = null;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return isWhiteTurn ? whitePlayer : blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    public Position getLastPawnDoubleMovePosition() {
        return lastPawnDoubleMovePosition;
    }

    public boolean makeMove(Position from, Position to) {
        if (gameState != GameState.IN_PROGRESS && gameState != GameState.CHECK) {
            return false;
        }
        Piece piece = board.getPieceAt(from);
        if (piece == null || piece.isWhite() != isWhiteTurn) {
            return false;
        }
        if (!moveValidator.isValidMove(board, from, to, lastPawnDoubleMovePosition)) {
            return false;
        }
        Piece capturedPiece = board.getPieceAt(to);
        boolean isPromotion = false;
        boolean isCastling = false;
        boolean isEnPassant = false;
        if (moveValidator.isCastlingMove(board, from, to)) {
            isCastling = true;
            executeCastling(from, to);
            Move castlingMove = new Move(from, to, piece, null, false, true, false);
            moveHistory.add(castlingMove);
        } else if (moveValidator.isEnPassantMove(board, from, to, lastPawnDoubleMovePosition)) {
            isEnPassant = true;
            executeEnPassant(from, to);
            Position capturedPawnPos = new Position(to.getFile(), from.getRank());
            Piece capturedPawn = board.getPieceAt(capturedPawnPos);
            Move enPassantMove = new Move(from, to, piece, capturedPawn, false, false, true);
            moveHistory.add(enPassantMove);
        } else {
            if (piece.getType() == Piece.PieceType.PAWN) {
                int promotionRank = piece.isWhite() ? 7 : 0;
                if (to.getRank() == promotionRank) {
                    isPromotion = true;
                    Queen promotedQueen = new Queen(piece.isWhite());
                    Move promotionMove = new Move(from, to, piece, capturedPiece, true, false, false);
                    promotionMove.setPromotedToPiece(promotedQueen);
                    moveHistory.add(promotionMove);
                    executePromotion(from, to, promotedQueen);
                } else {
                    int startRank = piece.isWhite() ? 1 : 6;
                    if (from.getRank() == startRank && Math.abs(to.getRank() - from.getRank()) == 2) {
                        lastPawnDoubleMovePosition = to;
                    } else {
                        lastPawnDoubleMovePosition = null;
                    }
                    executeMove(from, to, capturedPiece);
                }
            } else {
                lastPawnDoubleMovePosition = null;
                executeMove(from, to, capturedPiece);
            }
        }
        updateGameState();
        isWhiteTurn = !isWhiteTurn;
        return true;
    }

    private void executeMove(Position from, Position to, Piece capturedPiece) {
        Piece piece = board.getPieceAt(from);
        if (capturedPiece != null) {
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.addCapturedPiece(capturedPiece);
        }
        if (!isSpecialMoveInProgress()) {
            Move move = new Move(from, to, piece, capturedPiece);
            moveHistory.add(move);
        }
        board.movePiece(from, to);
    }

    private void executeCastling(Position kingFrom, Position kingTo) {
        board.movePiece(kingFrom, kingTo);
        int rookFile;
        int rookDestFile;
        if (kingTo.getFile() > kingFrom.getFile()) {
            rookFile = 7;
            rookDestFile = kingTo.getFile() - 1;
        } else {
            rookFile = 0;
            rookDestFile = kingTo.getFile() + 1;
        }
        Position rookFrom = new Position(rookFile, kingFrom.getRank());
        Position rookTo = new Position(rookDestFile, kingFrom.getRank());
        board.movePiece(rookFrom, rookTo);
    }

    private void executeEnPassant(Position from, Position to) {
        board.movePiece(from, to);
        Position capturedPawnPos = new Position(to.getFile(), from.getRank());
        Piece capturedPawn = board.getPieceAt(capturedPawnPos);
        if (capturedPawn != null) {
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.addCapturedPiece(capturedPawn);
            board.placePiece(null, capturedPawnPos);
        }
    }

    private void executePromotion(Position from, Position to, Piece promotedPiece) {
        Piece capturedPiece = board.getPieceAt(to);
        if (capturedPiece != null) {
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.addCapturedPiece(capturedPiece);
        }
        board.placePiece(null, from);
        board.placePiece(promotedPiece, to);
    }

    private boolean isSpecialMoveInProgress() {
        if (moveHistory.isEmpty()) {
            return false;
        }
        Move lastMove = moveHistory.get(moveHistory.size() - 1);
        return lastMove.isCastling() || lastMove.isEnPassant() || lastMove.isPromotion();
    }

    private void updateGameState() {
        boolean isInCheck = moveValidator.isInCheck(board, !isWhiteTurn);
        boolean hasValidMoves = moveValidator.hasValidMoves(board, !isWhiteTurn, lastPawnDoubleMovePosition);
        if (isInCheck && !hasValidMoves) {
            gameState = isWhiteTurn ? GameState.WHITE_WINS : GameState.BLACK_WINS;
        } else if (!isInCheck && !hasValidMoves) {
            gameState = GameState.STALEMATE;
        } else if (isInCheck) {
            gameState = GameState.CHECK;
        } else {
            gameState = GameState.IN_PROGRESS;
        }
    }

    public void restart() {
        this.board = new Board();
        this.isWhiteTurn = true;
        this.gameState = GameState.IN_PROGRESS;
        this.moveHistory.clear();
        this.whitePlayer.clearCapturedPieces();
        this.blackPlayer.clearCapturedPieces();
        this.lastPawnDoubleMovePosition = null;
    }

    public enum GameState {
        IN_PROGRESS,
        CHECK,
        CHECKMATE,
        STALEMATE,
        WHITE_WINS,
        BLACK_WINS,
        DRAW
    }
}