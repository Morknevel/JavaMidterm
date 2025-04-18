package org.example;

import static org.junit.jupiter.api.Assertions.*;

import main.java.com.chess.model.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.piece.King;
import main.java.com.chess.model.piece.Knight;
import main.java.com.chess.model.piece.Pawn;
import main.java.com.chess.model.piece.Queen;
import main.java.com.chess.model.piece.Rook;
import main.java.com.chess.model.rules.MoveValidator;
import main.java.com.chess.model.GameControllerImpl;

public class GameStateTest {
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
    public void testCheckDetection() {
        Board testBoard = new Board(true);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        King whiteKing = new King(true);
        Position kingPos = new Position(4, 0);
        testBoard.placePiece(whiteKing, kingPos);
        Queen blackQueen = new Queen(false);
        Position queenPos = new Position(4, 7);
        testBoard.placePiece(blackQueen, queenPos);
        assertTrue(moveValidator.isInCheck(testBoard, true));
        testBoard.placePiece(null, queenPos);
        testBoard.placePiece(blackQueen, new Position(0, 0));
        assertFalse(moveValidator.isInCheck(testBoard, true));
    }

    @Test
    public void testKingCannotMoveIntoCheck() {
        Board testBoard = new Board(true);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        King whiteKing = new King(true);
        Position kingPos = new Position(4, 0);
        testBoard.placePiece(whiteKing, kingPos);
        Rook blackRook = new Rook(false);
        Position rookPos = new Position(5, 1);
        testBoard.placePiece(blackRook, rookPos);
        assertFalse(moveValidator.isValidMove(testBoard, kingPos, new Position(5, 0)));
        assertTrue(moveValidator.isValidMove(testBoard, kingPos, new Position(3, 0)));
    }

    @Test
    public void testMustMoveOutOfCheck() {
        Board testBoard = new Board(true);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        King whiteKing = new King(true);
        Position kingPos = new Position(4, 0);
        testBoard.placePiece(whiteKing, kingPos);
        Pawn whitePawn = new Pawn(true);
        Position pawnPos = new Position(3, 1);
        testBoard.placePiece(whitePawn, pawnPos);
        Queen blackQueen = new Queen(false);
        Position queenPos = new Position(4, 7);
        testBoard.placePiece(blackQueen, queenPos);
        assertTrue(moveValidator.isInCheck(testBoard, true));
        Position validPawnMove = new Position(4, 2);
        assertTrue(moveValidator.isValidMove(testBoard, pawnPos, validPawnMove));
        Position invalidPawnMove = new Position(2, 2);
        Board tempBoard = new Board(testBoard);
        tempBoard.movePiece(pawnPos, invalidPawnMove);
        assertTrue(moveValidator.isInCheck(tempBoard, true));
    }

    @Test
    public void testCheckmateDetection() {
        Board testBoard = new Board(true);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));
        King blackKing = new King(false);
        testBoard.placePiece(blackKing, new Position(4, 7));
        testBoard.placePiece(new Pawn(true), new Position(5, 2));
        testBoard.placePiece(new Pawn(true), new Position(6, 3));
        testBoard.placePiece(new Queen(false), new Position(7, 3));
        assertTrue(moveValidator.isInCheck(testBoard, true));
        assertFalse(moveValidator.hasValidMoves(testBoard, true, null));
    }

    @Test
    public void testNearCheckmateButCanBlock() {
        Board testBoard = new Board(true);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(4, 0));
        King blackKing = new King(false);
        testBoard.placePiece(blackKing, new Position(4, 7));
        testBoard.placePiece(new Queen(false), new Position(4, 3));
        testBoard.placePiece(new Knight(true), new Position(3, 1));
        assertTrue(moveValidator.isInCheck(testBoard, true));
        assertTrue(moveValidator.hasValidMoves(testBoard, true, null));
    }

    @Test
    public void testStalemateDetection() {
        Board testBoard = new Board(true);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        King whiteKing = new King(true);
        testBoard.placePiece(whiteKing, new Position(0, 0));
        King blackKing = new King(false);
        testBoard.placePiece(blackKing, new Position(2, 1));
        testBoard.placePiece(new Queen(false), new Position(1, 2));
        assertFalse(moveValidator.isInCheck(testBoard, true));
        assertFalse(moveValidator.hasValidMoves(testBoard, true, null));
    }

    @Test
    public void testGameStateProgression() {
        GameControllerImpl testController = new GameControllerImpl();
        Game testGame = testController.getGame();
        testGame.restart();
        assertEquals(Game.GameState.IN_PROGRESS, testGame.getGameState());
        Board testBoard = testGame.getBoard();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                testBoard.placePiece(null, new Position(file, rank));
            }
        }
        testBoard.placePiece(new King(true), new Position(4, 0));
        testBoard.placePiece(new King(false), new Position(4, 7));
        testBoard.placePiece(new Queen(false), new Position(4, 1));
        assertTrue(moveValidator.isInCheck(testBoard, true));
    }
}