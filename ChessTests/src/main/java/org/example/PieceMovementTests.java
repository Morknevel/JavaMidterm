package org.example;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.chess.model.board.Board;
import main.java.com.chess.model.board.Position;
import main.java.com.chess.model.piece.Pawn;
import main.java.com.chess.model.piece.Rook;
import main.java.com.chess.model.piece.Knight;
import main.java.com.chess.model.piece.Bishop;
import main.java.com.chess.model.piece.Queen;
import main.java.com.chess.model.piece.King;

public class PieceMovementTests {
    private Board emptyBoard;

    @BeforeEach
    public void setUp() {
        emptyBoard = new Board(true);
    }

    @Test
    public void testPawnInitialMoves() {
        Pawn whitePawn = new Pawn(true);
        Position startPos = new Position(3, 1);
        emptyBoard.placePiece(whitePawn, startPos);

        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);

        assertEquals(2, validMoves.size());
        assertTrue(validMoves.contains(new Position(3, 2)));
        assertTrue(validMoves.contains(new Position(3, 3)));

        Pawn blackPawn = new Pawn(false);
        Position blackStartPos = new Position(3, 6);
        emptyBoard.placePiece(blackPawn, blackStartPos);

        List<Position> blackValidMoves = blackPawn.getValidMoves(emptyBoard);

        assertEquals(2, blackValidMoves.size());
        assertTrue(blackValidMoves.contains(new Position(3, 5)));
        assertTrue(blackValidMoves.contains(new Position(3, 4)));
    }

    @Test
    public void testPawnNormalMoves() {
        Pawn whitePawn = new Pawn(true);
        Position midPos = new Position(3, 3);
        whitePawn.setMoved(true);
        emptyBoard.placePiece(whitePawn, midPos);

        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);

        assertEquals(1, validMoves.size());
        assertTrue(validMoves.contains(new Position(3, 4)));
    }

    @Test
    public void testPawnBlockedMoves() {
        Pawn whitePawn = new Pawn(true);
        Position startPos = new Position(3, 1);
        emptyBoard.placePiece(whitePawn, startPos);

        emptyBoard.placePiece(new Pawn(true), new Position(3, 2));

        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);

        assertEquals(0, validMoves.size());
    }

    @Test
    public void testRookMoves() {
        Rook rook = new Rook(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(rook, pos);

        List<Position> validMoves = rook.getValidMoves(emptyBoard);

        assertEquals(14, validMoves.size());
        assertTrue(validMoves.contains(new Position(3, 7)));
        assertTrue(validMoves.contains(new Position(3, 0)));
        assertTrue(validMoves.contains(new Position(0, 3)));
        assertTrue(validMoves.contains(new Position(7, 3)));
    }

    @Test
    public void testRookCaptures() {
        Rook rook = new Rook(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(rook, pos);

        emptyBoard.placePiece(new Pawn(true), new Position(3, 5));
        emptyBoard.placePiece(new Pawn(false), new Position(6, 3));

        List<Position> validMoves = rook.getValidMoves(emptyBoard);

        assertFalse(validMoves.contains(new Position(3, 6)));
        assertFalse(validMoves.contains(new Position(3, 7)));
        assertTrue(validMoves.contains(new Position(3, 4)));
        assertFalse(validMoves.contains(new Position(3, 5)));
        assertTrue(validMoves.contains(new Position(6, 3)));
        assertFalse(validMoves.contains(new Position(7, 3)));
    }

    @Test
    public void testKnightMoves() {
        Knight knight = new Knight(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(knight, pos);

        List<Position> validMoves = knight.getValidMoves(emptyBoard);

        assertEquals(8, validMoves.size());
        assertTrue(validMoves.contains(new Position(1, 2)));
        assertTrue(validMoves.contains(new Position(1, 4)));
        assertTrue(validMoves.contains(new Position(2, 1)));
        assertTrue(validMoves.contains(new Position(2, 5)));
        assertTrue(validMoves.contains(new Position(4, 1)));
        assertTrue(validMoves.contains(new Position(4, 5)));
        assertTrue(validMoves.contains(new Position(5, 2)));
        assertTrue(validMoves.contains(new Position(5, 4)));
    }

    @Test
    public void testKnightEdgeMoves() {
        Knight knight = new Knight(true);
        Position pos = new Position(0, 0);
        emptyBoard.placePiece(knight, pos);

        List<Position> validMoves = knight.getValidMoves(emptyBoard);

        assertEquals(2, validMoves.size());
        assertTrue(validMoves.contains(new Position(1, 2)));
        assertTrue(validMoves.contains(new Position(2, 1)));
    }

    @Test
    public void testBishopMoves() {
        Bishop bishop = new Bishop(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(bishop, pos);

        List<Position> validMoves = bishop.getValidMoves(emptyBoard);

        assertEquals(13, validMoves.size());
        assertTrue(validMoves.contains(new Position(0, 0)));
        assertTrue(validMoves.contains(new Position(6, 0)));
        assertTrue(validMoves.contains(new Position(0, 6)));
        assertTrue(validMoves.contains(new Position(6, 6)));
    }

    @Test
    public void testBishopCaptures() {
        Bishop bishop = new Bishop(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(bishop, pos);

        emptyBoard.placePiece(new Pawn(true), new Position(5, 5));
        emptyBoard.placePiece(new Pawn(false), new Position(1, 5));

        List<Position> validMoves = bishop.getValidMoves(emptyBoard);

        assertFalse(validMoves.contains(new Position(5, 5)));
        assertFalse(validMoves.contains(new Position(6, 6)));
        assertTrue(validMoves.contains(new Position(1, 5)));
        assertFalse(validMoves.contains(new Position(0, 6)));
    }

    @Test
    public void testQueenMoves() {
        Queen queen = new Queen(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(queen, pos);

        List<Position> validMoves = queen.getValidMoves(emptyBoard);

        assertEquals(27, validMoves.size());
        assertTrue(validMoves.contains(new Position(0, 0)));
        assertTrue(validMoves.contains(new Position(6, 6)));
        assertTrue(validMoves.contains(new Position(3, 7)));
        assertTrue(validMoves.contains(new Position(7, 3)));
    }

    @Test
    public void testKingMoves() {
        King king = new King(true);
        Position pos = new Position(3, 3);
        emptyBoard.placePiece(king, pos);

        List<Position> validMoves = king.getValidMoves(emptyBoard);

        assertEquals(8, validMoves.size());
        assertTrue(validMoves.contains(new Position(2, 2)));
        assertTrue(validMoves.contains(new Position(2, 3)));
        assertTrue(validMoves.contains(new Position(2, 4)));
        assertTrue(validMoves.contains(new Position(3, 2)));
        assertTrue(validMoves.contains(new Position(3, 4)));
        assertTrue(validMoves.contains(new Position(4, 2)));
        assertTrue(validMoves.contains(new Position(4, 3)));
        assertTrue(validMoves.contains(new Position(4, 4)));
    }

    @Test
    public void testKingCastlingPossible() {
        King king = new King(true);
        Position kingPos = new Position(4, 0);
        emptyBoard.placePiece(king, kingPos);

        Rook kingsideRook = new Rook(true);
        emptyBoard.placePiece(kingsideRook, new Position(7, 0));

        Rook queensideRook = new Rook(true);
        emptyBoard.placePiece(queensideRook, new Position(0, 0));

        List<Position> validMoves = king.getValidMoves(emptyBoard);

        assertTrue(validMoves.contains(new Position(6, 0)));
        assertTrue(validMoves.contains(new Position(2, 0)));
    }

    @Test
    public void testKingCastlingBlocked() {
        King king = new King(true);
        Position kingPos = new Position(4, 0);
        emptyBoard.placePiece(king, kingPos);

        Rook kingsideRook = new Rook(true);
        emptyBoard.placePiece(kingsideRook, new Position(7, 0));

        Rook queensideRook = new Rook(true);
        emptyBoard.placePiece(queensideRook, new Position(0, 0));

        emptyBoard.placePiece(new Knight(true), new Position(5, 0));
        emptyBoard.placePiece(new Bishop(true), new Position(1, 0));

        List<Position> validMoves = king.getValidMoves(emptyBoard);

        assertFalse(validMoves.contains(new Position(6, 0)));
        assertFalse(validMoves.contains(new Position(2, 0)));
    }

    @Test
    public void testKingCastlingAfterMoving() {
        King king = new King(true);
        Position kingPos = new Position(4, 0);
        king.setMoved(true);
        emptyBoard.placePiece(king, kingPos);

        Rook kingsideRook = new Rook(true);
        emptyBoard.placePiece(kingsideRook, new Position(7, 0));

        Rook queensideRook = new Rook(true);
        emptyBoard.placePiece(queensideRook, new Position(0, 0));

        List<Position> validMoves = king.getValidMoves(emptyBoard);

        assertFalse(validMoves.contains(new Position(6, 0)));
        assertFalse(validMoves.contains(new Position(2, 0)));
    }
}