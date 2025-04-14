package main.java.com.chess.model;

import java.util.ArrayList;
import java.util.List;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.game.Game;
import main.java.com.chess.model.game.Move;
import main.java.com.chess.model.piece.*;
import main.java.com.chess.model.rules.MoveValidator;

/**
 * Implementation of the GameController interface.
 * Fixed version that correctly handles pawn promotion.
 */
public class GameControllerImpl implements GameController {
    private Game game;
    private MoveValidator moveValidator;
    private List<GameListener> listeners;

    /**
     * Creates a new GameController.
     */
    public GameControllerImpl() {
        this.game = new Game();
        this.moveValidator = new MoveValidator();
        this.listeners = new ArrayList<>();
    }

    @Override
    public boolean makeMove(Position from, Position to) {
        // Store state before move for later comparison
        Game.GameState stateBefore = game.getGameState();

        if (game.makeMove(from, to)) {
            // Get the move from history
            List<Move> history = game.getMoveHistory();
            Move lastMove = history.get(history.size() - 1);

            // Notify listeners
            notifyMoveMade(lastMove);

            if (game.getGameState() != stateBefore) {
                notifyGameStateChanged(game.getGameState());
            }

            if (lastMove.isCapture()) {
                notifyPieceCaptured(lastMove);
            }

            if (lastMove.isPromotion()) {
                notifyPawnPromoted(lastMove);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<Position> getValidMovesForPiece(Position position) {
        Piece piece = game.getBoard().getPieceAt(position);
        if (piece == null || piece.isWhite() != game.isWhiteTurn()) {
            return new ArrayList<>();
        }

        List<Position> validMoves = new ArrayList<>();
        List<Position> possibleMoves = piece.getValidMoves(game.getBoard());

        for (Position to : possibleMoves) {
            if (moveValidator.isValidMove(game.getBoard(), position, to, game.getLastPawnDoubleMovePosition())) {
                validMoves.add(to);
            }
        }

        // Check for castling
        if (piece instanceof King && !piece.hasMoved()) {
            Position kingsideCastling = new Position(position.getFile() + 2, position.getRank());
            Position queensideCastling = new Position(position.getFile() - 2, position.getRank());

            if (moveValidator.isCastlingValid(game.getBoard(), position, kingsideCastling)) {
                validMoves.add(kingsideCastling);
            }

            if (moveValidator.isCastlingValid(game.getBoard(), position, queensideCastling)) {
                validMoves.add(queensideCastling);
            }
        }

        // Check for en passant
        if (piece instanceof Pawn && game.getLastPawnDoubleMovePosition() != null) {
            int direction = piece.isWhite() ? 1 : -1;
            Position leftCapture = position.offset(-1, direction);
            Position rightCapture = position.offset(1, direction);

            if (leftCapture != null && moveValidator.isEnPassantValid(game.getBoard(), position, leftCapture, game.getLastPawnDoubleMovePosition())) {
                validMoves.add(leftCapture);
            }

            if (rightCapture != null && moveValidator.isEnPassantValid(game.getBoard(), position, rightCapture, game.getLastPawnDoubleMovePosition())) {
                validMoves.add(rightCapture);
            }
        }

        return validMoves;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public List<Move> getMoveHistory() {
        return game.getMoveHistory();
    }

    @Override
    public void restart() {
        game.restart();
        notifyGameStateChanged(game.getGameState());
    }

    @Override
    public boolean promotePawn(Position position, Piece.PieceType type) {
        Board board = game.getBoard();
        Piece piece = board.getPieceAt(position);

        // Check if there is a pawn to promote
        if (piece == null || piece.getType() != Piece.PieceType.PAWN) {
            return false;
        }

        // Check if the pawn is in the right position for promotion
        int promotionRank = piece.isWhite() ? 7 : 0;
        if (position.getRank() != promotionRank) {
            return false;
        }

        // Create the promoted piece
        Piece promotedPiece;
        boolean isWhite = piece.isWhite();

        switch (type) {
            case QUEEN:
                promotedPiece = new Queen(isWhite);
                break;
            case ROOK:
                promotedPiece = new Rook(isWhite);
                break;
            case BISHOP:
                promotedPiece = new Bishop(isWhite);
                break;
            case KNIGHT:
                promotedPiece = new Knight(isWhite);
                break;
            default:
                promotedPiece = new Queen(isWhite); // Default to queen
        }

        // Remove the pawn and place the promoted piece
        board.placePiece(promotedPiece, position);

        // Find the last move (should be the promotion move)
        if (!game.getMoveHistory().isEmpty()) {
            Move lastMove = game.getMoveHistory().get(game.getMoveHistory().size() - 1);
            if (lastMove.isPromotion() && lastMove.getTo().equals(position)) {
                lastMove.setPromotedToPiece(promotedPiece);
                notifyPawnPromoted(lastMove);
            }
        }

        return true;
    }

    @Override
    public void addGameListener(GameListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }

    private void notifyMoveMade(Move move) {
        for (GameListener listener : listeners) {
            listener.onMoveMade(move);
        }
    }

    private void notifyGameStateChanged(Game.GameState newState) {
        for (GameListener listener : listeners) {
            listener.onGameStateChanged(newState);
        }
    }

    private void notifyPieceCaptured(Move move) {
        for (GameListener listener : listeners) {
            listener.onPieceCaptured(move);
        }
    }

    private void notifyPawnPromoted(Move move) {
        for (GameListener listener : listeners) {
            listener.onPawnPromoted(move);
        }
    }
}