package main.java.com.chess.model;

import java.util.List;

import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.game.Game;
import main.java.com.chess.model.game.Move;
import main.java.com.chess.model.piece.Piece;

/**
 * Interface for UI to control the chess game.
 */
public interface GameController {

    /**
     * Makes a move from one position to another.
     *
     * @param from the source position
     * @param to the destination position
     * @return true if the move was successful
     */
    boolean makeMove(Position from, Position to);

    /**
     * Gets the valid moves for the piece at the specified position.
     *
     * @param position the position
     * @return list of valid move positions
     */
    List<Position> getValidMovesForPiece(Position position);

    /**
     * Gets the game.
     *
     * @return the game
     */
    Game getGame();

    /**
     * Gets the move history.
     *
     * @return the move history
     */
    List<Move> getMoveHistory();

    /**
     * Restarts the game.
     */
    void restart();

    /**
     * Promotes a pawn to the specified piece type.
     *
     * @param position the position of the pawn to promote
     * @param type the type of piece to promote to
     * @return true if the promotion was successful
     */
    boolean promotePawn(Position position, Piece.PieceType type);

    /**
     * Adds a listener to be notified of game events.
     *
     * @param listener the listener to add
     */
    void addGameListener(GameListener listener);

    /**
     * Removes a game listener.
     *
     * @param listener the listener to remove
     */
    void removeGameListener(GameListener listener);

    /**
     * Interface for objects that want to be notified of game events.
     */
    interface GameListener {
        /**
         * Called when a move is made.
         *
         * @param move the move that was made
         */
        void onMoveMade(Move move);

        /**
         * Called when the game state changes.
         *
         * @param newState the new game state
         */
        void onGameStateChanged(Game.GameState newState);

        /**
         * Called when a piece is captured.
         *
         * @param move the move that resulted in a capture
         */
        void onPieceCaptured(Move move);

        /**
         * Called when a pawn is promoted.
         *
         * @param move the promotion move
         */
        void onPawnPromoted(Move move);
    }
}