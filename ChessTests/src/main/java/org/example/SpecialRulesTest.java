package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.game.Game;
import main.java.com.chess.model.piece.King;
import main.java.com.chess.model.piece.Knight;
import main.java.com.chess.model.piece.Pawn;
import main.java.com.chess.model.piece.Piece;
import main.java.com.chess.model.piece.Rook;
import main.java.com.chess.model.rules.MoveValidator;
import main.java.com.chess.model.GameControllerImpl;

public class SpecialRulesTest {
    private Game game;
    private Board board;
    private MoveValidator moveValidator;
    private GameControllerImpl gameController;

    @BeforeEach
    public void setUp() {
        game = new Game();
        board = game.getBoard();
        moveValidator = new MoveValidator();
        gameController = new GameControllerImpl();
    }

    @Test
    public void testKingsideCastling() {
        Board testBoard = new Board(true);

        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));

        Rook whiteRook = new Rook(true);
        testBoard.placePiece(whiteRook, new Position(7, 0));

        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
        assertTrue(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
    }

    @Test
    public void testQueensideCastling() {
        Board testBoard = new Board(true);

        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));

        Rook whiteRook = new Rook(true);
        testBoard.placePiece(whiteRook, new Position(0, 0));

        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(2, 0)));
        assertTrue(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(2, 0)));
    }

    @Test
    public void testCastlingWithBlockedPath() {
        Board testBoard = new Board(true);

        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));

        Rook whiteRook = new Rook(true);
        testBoard.placePiece(whiteRook, new Position(7, 0));

        Knight whiteKnight = new Knight(true);
        testBoard.placePiece(whiteKnight, new Position(5, 0));

        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
        assertFalse(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
    }

    @Test
    public void testCastlingAfterKingMoved() {
        Board testBoard = new Board(true);

        King whiteKing = new King(true);
        whiteKing.setMoved(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));

        Rook whiteRook = new Rook(true);
        testBoard.placePiece(whiteRook, new Position(7, 0));

        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
        assertFalse(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
    }

    @Test
    public void testCastlingAfterRookMoved() {
        Board testBoard = new Board(true);

        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));

        Rook whiteRook = new Rook(true);
        whiteRook.setMoved(true);
        testBoard.placePiece(whiteRook, new Position(7, 0));

        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
        assertFalse(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
    }

    @Test
    public void testEnPassantCapture() {
        Board testBoard = new Board(true);

        Pawn whitePawn = new Pawn(true);
        testBoard.placePiece(whitePawn, new Position(1, 4));

        Pawn blackPawn = new Pawn(false);
        blackPawn.setMoved(false);
        testBoard.placePiece(blackPawn, new Position(0, 6));

        Position blackPawnStart = new Position(0, 6);
        Position blackPawnEnd = new Position(0, 4);
        testBoard.movePiece(blackPawnStart, blackPawnEnd);

        assertTrue(moveValidator.isEnPassantMove(testBoard, new Position(1, 4), new Position(0, 5), blackPawnEnd));
        assertTrue(moveValidator.isEnPassantValid(testBoard, new Position(1, 4), new Position(0, 5), blackPawnEnd));
    }

    @Test
    public void testEnPassantTiming() {
        Board testBoard = new Board(true);

        Pawn whitePawn = new Pawn(true);
        whitePawn.setMoved(true);
        testBoard.placePiece(whitePawn, new Position(1, 4));

        Pawn blackPawn = new Pawn(false);
        blackPawn.setMoved(false);
        testBoard.placePiece(blackPawn, new Position(0, 6));

        Position blackPawnStart = new Position(0, 6);
        Position blackPawnEnd = new Position(0, 4);
        testBoard.movePiece(blackPawnStart, blackPawnEnd);

        assertTrue(moveValidator.isEnPassantValid(testBoard, new Position(1, 4), new Position(0, 5), blackPawnEnd));
        assertFalse(moveValidator.isEnPassantValid(testBoard, new Position(1, 4), new Position(2, 5), blackPawnEnd));
    }

    @Test
    public void testCannotPromoteNonPawn() {
        Board testBoard = new Board(true);

        Knight whiteKnight = new Knight(true);
        Position pos = new Position(3, 7);
        testBoard.placePiece(whiteKnight, pos);

        GameControllerImpl testController = new GameControllerImpl();
        Game testGame = testController.getGame();
        testGame.restart();

        Board gameBoard = testGame.getBoard();
        gameBoard.placePiece(new Knight(true), pos);

        assertFalse(testController.promotePawn(pos, Piece.PieceType.QUEEN));

        Piece piece = gameBoard.getPieceAt(pos);
        assertEquals(Piece.PieceType.KNIGHT, piece.getType());
    }
}