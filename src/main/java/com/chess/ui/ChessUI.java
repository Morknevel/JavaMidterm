package main.java.com.chess.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.com.chess.model.GameController;
import main.java.com.chess.model.GameControllerImpl;
import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.game.Game;
import main.java.com.chess.model.game.Move;
import main.java.com.chess.model.piece.Piece;

/**
 * Sample implementation of a chess UI that integrates with the model.
 * This is not a complete implementation, but shows how to use the model.
 */
public class ChessUI extends JPanel implements GameController.GameListener {
    private static final int SQUARE_SIZE = 60;

    private GameController gameController;
    private Position selectedPosition;
    private List<Position> validMoves;

    public ChessUI() {
        // Initialize the game controller
        gameController = new GameControllerImpl();
        gameController.addGameListener(this);

        // Initialize UI state
        selectedPosition = null;
        validMoves = new ArrayList<>();

        // Set up the UI
        setPreferredSize(new java.awt.Dimension(8 * SQUARE_SIZE, 8 * SQUARE_SIZE));

        // Add mouse listener for handling clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void handleMouseClick(MouseEvent e) {
        // Convert pixel coordinates to board position
        Position clickedPosition = convertToPosition(e.getX(), e.getY());

        if (clickedPosition == null) {
            return;
        }

        if (selectedPosition == null) {
            // First click - select piece
            Piece piece = gameController.getGame().getBoard().getPieceAt(clickedPosition);

            if (piece != null && piece.isWhite() == gameController.getGame().isWhiteTurn()) {
                // Select the piece
                selectedPosition = clickedPosition;

                // Get valid moves for this piece
                validMoves = gameController.getValidMovesForPiece(selectedPosition);

                repaint();
            }
        } else if (clickedPosition.equals(selectedPosition)) {
            // Clicked the same square again - deselect
            selectedPosition = null;
            validMoves.clear();
            repaint();
        } else {
            // Second click - attempt to move
            if (validMoves.contains(clickedPosition)) {
                // Check if this is a pawn promotion move
                Piece piece = gameController.getGame().getBoard().getPieceAt(selectedPosition);
                boolean isPromotion = isPawnPromotion(piece, clickedPosition);

                // Make the move
                if (gameController.makeMove(selectedPosition, clickedPosition)) {
                    // If this is a promotion, show promotion dialog
                    if (isPromotion) {
                        showPromotionDialog(clickedPosition);
                    }
                }

                // Clear selection after move
                selectedPosition = null;
                validMoves.clear();
                repaint();
            } else {
                // Invalid move destination, try selecting a new piece
                Piece piece = gameController.getGame().getBoard().getPieceAt(clickedPosition);

                if (piece != null && piece.isWhite() == gameController.getGame().isWhiteTurn()) {
                    // Select the new piece
                    selectedPosition = clickedPosition;
                    validMoves = gameController.getValidMovesForPiece(selectedPosition);
                } else {
                    // Deselect if clicked an invalid square
                    selectedPosition = null;
                    validMoves.clear();
                }

                repaint();
            }
        }
    }

    private boolean isPawnPromotion(Piece piece, Position to) {
        if (piece == null || piece.getType() != Piece.PieceType.PAWN) {
            return false;
        }

        int promotionRank = piece.isWhite() ? 7 : 0;
        return to.getRank() == promotionRank;
    }

    private void showPromotionDialog(Position position) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose promotion piece:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice >= 0) {
            Piece.PieceType type = convertChoiceToPieceType(choice);
            gameController.promotePawn(position, type);
        }
    }

    private Piece.PieceType convertChoiceToPieceType(int choice) {
        switch (choice) {
            case 0: return Piece.PieceType.QUEEN;
            case 1: return Piece.PieceType.ROOK;
            case 2: return Piece.PieceType.BISHOP;
            case 3: return Piece.PieceType.KNIGHT;
            default: return Piece.PieceType.QUEEN;
        }
    }

    private Position convertToPosition(int x, int y) {
        int file = x / SQUARE_SIZE;
        int rank = 7 - (y / SQUARE_SIZE); // Invert because screen coordinates go down, but rank goes up

        if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
            return new Position(file, rank);
        }

        return null;
    }

    private Point convertToPixel(Position position) {
        int x = position.getFile() * SQUARE_SIZE;
        int y = (7 - position.getRank()) * SQUARE_SIZE; // Invert for screen coordinates

        return new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        drawBoard(g);

        // Draw the pieces
        drawPieces(g);

        // Highlight selected position
        if (selectedPosition != null) {
            highlightSquare(g, selectedPosition, Color.YELLOW);
        }

        // Highlight valid moves
        for (Position position : validMoves) {
            highlightSquare(g, position, Color.GREEN);
        }

        // Highlight king in check
        if (gameController.getGame().getGameState() == Game.GameState.CHECK) {
            boolean isWhiteInCheck = !gameController.getGame().isWhiteTurn();
            Position kingPosition = gameController.getGame().getBoard().findKing(isWhiteInCheck);

            if (kingPosition != null) {
                highlightSquare(g, kingPosition, Color.RED);
            }
        }
    }

    private void drawBoard(Graphics g) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                // Determine square color
                boolean isLightSquare = (rank + file) % 2 == 0;
                Color squareColor = isLightSquare ? Color.WHITE : Color.GRAY;

                // Draw the square
                g.setColor(squareColor);
                g.fillRect(file * SQUARE_SIZE, (7 - rank) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawPieces(Graphics g) {
        Board board = gameController.getGame().getBoard();

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position position = new Position(file, rank);
                Piece piece = board.getPieceAt(position);

                if (piece != null) {
                    drawPiece(g, piece, position);
                }
            }
        }
    }

    private void drawPiece(Graphics g, Piece piece, Position position) {
        Point p = convertToPixel(position);

        // Draw a simple representation of the piece
        g.setColor(piece.isWhite() ? Color.WHITE : Color.BLACK);
        g.drawRect(p.x + 5, p.y + 5, SQUARE_SIZE - 10, SQUARE_SIZE - 10);

        // Draw the piece type
        g.setColor(piece.isWhite() ? Color.BLACK : Color.RED);
        String pieceChar = "";

        switch (piece.getType()) {
            case KING: pieceChar = "K"; break;
            case QUEEN: pieceChar = "Q"; break;
            case ROOK: pieceChar = "R"; break;
            case BISHOP: pieceChar = "B"; break;
            case KNIGHT: pieceChar = "N"; break;
            case PAWN: pieceChar = "P"; break;
        }

        g.drawString(pieceChar, p.x + SQUARE_SIZE / 2 - 4, p.y + SQUARE_SIZE / 2 + 4);
    }

    private void highlightSquare(Graphics g, Position position, Color color) {
        Point p = convertToPixel(position);

        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g.fillRect(p.x, p.y, SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void onMoveMade(Move move) {
        // Repaint after a move
        repaint();
    }

    @Override
    public void onGameStateChanged(Game.GameState newState) {
        // Show game state
        String message = "";

        switch (newState) {
            case CHECK:
                message = (gameController.getGame().isWhiteTurn() ? "Black" : "White") + " is in check!";
                break;
            case WHITE_WINS:
                message = "Checkmate! White wins!";
                break;
            case BLACK_WINS:
                message = "Checkmate! Black wins!";
                break;
            case STALEMATE:
                message = "Stalemate! The game is a draw.";
                break;
            case DRAW:
                message = "The game is a draw.";
                break;
        }

        if (!message.isEmpty()) {
            JOptionPane.showMessageDialog(this, message);
        }

        repaint();
    }

    @Override
    public void onPieceCaptured(Move move) {
        // Update captured pieces display
        repaint();
    }

    @Override
    public void onPawnPromoted(Move move) {
        repaint();
    }
}