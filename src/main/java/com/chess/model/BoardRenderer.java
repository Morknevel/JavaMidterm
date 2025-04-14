package main.java.com.chess.model;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;

public interface BoardRenderer {
    void renderBoard(Board board);

    void highlightSquare(Position position, boolean isSelected);

    void highlightValidMoves(Iterable<Position> positions);

    void showCheckIndicator(Position kingPosition);

    void clearHighlights();
    void showMessage(String message);
}