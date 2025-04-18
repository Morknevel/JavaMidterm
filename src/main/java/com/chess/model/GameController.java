package main.java.com.chess.model;

import java.util.List;

import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.game.Game;
import main.java.com.chess.model.game.Move;
import main.java.com.chess.model.piece.Piece;

public interface GameController {

    boolean makeMove(Position from, Position to);

    List<Position> getValidMovesForPiece(Position position);

    Game getGame();

    List<Move> getMoveHistory();

    void restart();

    boolean promotePawn(Position position, Piece.PieceType type);

    void addGameListener(GameListener listener);

    void removeGameListener(GameListener listener);

    interface GameListener {
        void onMoveMade(Move move);
        void onGameStateChanged(Game.GameState newState);
        void onPieceCaptured(Move move);
        void onPawnPromoted(Move move);
    }
}