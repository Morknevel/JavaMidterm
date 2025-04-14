//package main.java.com.chess.model.piece;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import main.java.com.chess.model.board.Board;
//import main.java.com.chess.model.board.Position;
//import main.java.com.chess.model.piece.Pawn;
//import main.java.com.chess.model.piece.Rook;
//import main.java.com.chess.model.piece.Knight;
//import main.java.com.chess.model.piece.Bishop;
//import main.java.com.chess.model.piece.Queen;
//import main.java.com.chess.model.piece.King;
//import main.java.com.chess.model.piece.Piece;
//
//
//public class PieceMovementTest {
//    private Board emptyBoard;
//
//    @BeforeEach
//    public void setUp() {
//        emptyBoard = new Board(true);
//    }
//
//    // PAWN TESTS
//
//    @Test
//    public void testPawnInitialMoves() {
//        // White pawn on starting position
//        Pawn whitePawn = new Pawn(true);
//        Position startPos = new Position(3, 1);  // d2
//        emptyBoard.placePiece(whitePawn, startPos);
//
//        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);
//
//        // Should have two possible moves: one square and two squares forward
//        assertEquals(2, validMoves.size());
//        assertTrue(validMoves.contains(new Position(3, 2))); // d3
//        assertTrue(validMoves.contains(new Position(3, 3))); // d4
//
//        // Black pawn on starting position
//        Pawn blackPawn = new Pawn(false);
//        Position blackStartPos = new Position(3, 6);  // d7
//        emptyBoard.placePiece(blackPawn, blackStartPos);
//
//        List<Position> blackValidMoves = blackPawn.getValidMoves(emptyBoard);
//
//        // Should have two possible moves: one square and two squares forward
//        assertEquals(2, blackValidMoves.size());
//        assertTrue(blackValidMoves.contains(new Position(3, 5))); // d6
//        assertTrue(blackValidMoves.contains(new Position(3, 4))); // d5
//    }
//
//    @Test
//    public void testPawnNormalMoves() {
//        // White pawn that has already moved
//        Pawn whitePawn = new Pawn(true);
//        Position midPos = new Position(3, 3);  // d4
//        whitePawn.setMoved(true);
//        emptyBoard.placePiece(whitePawn, midPos);
//
//        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);
//
//        // Should have only one possible move: one square forward
//        assertEquals(1, validMoves.size());
//        assertTrue(validMoves.contains(new Position(3, 4))); // d5
//    }
//
//    @Test
//    public void testPawnCaptures() {
//        // White pawn with enemy pieces to capture
//        Pawn whitePawn = new Pawn(true);
//        Position midPos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(whitePawn, midPos);
//
//        // Place enemy pieces to capture
//        emptyBoard.placePiece(new Pawn(false), new Position(2, 4)); // c5
//        emptyBoard.placePiece(new Pawn(false), new Position(4, 4)); // e5
//
//        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);
//
//        // Should have three possible moves: forward and two captures
//        assertEquals(3, validMoves.size());
//        assertTrue(validMoves.contains(new Position(3, 4))); // d5
//        assertTrue(validMoves.contains(new Position(2, 4))); // c5
//        assertTrue(validMoves.contains(new Position(4, 4))); // e5
//    }
//
//    @Test
//    public void testPawnBlockedMoves() {
//        // White pawn blocked by another piece
//        Pawn whitePawn = new Pawn(true);
//        Position startPos = new Position(3, 1);  // d2
//        emptyBoard.placePiece(whitePawn, startPos);
//
//        // Place a blocking piece
//        emptyBoard.placePiece(new Pawn(true), new Position(3, 2)); // d3
//
//        List<Position> validMoves = whitePawn.getValidMoves(emptyBoard);
//
//        // Should have no valid moves
//        assertEquals(0, validMoves.size());
//    }
//
//    // ROOK TESTS
//
//    @Test
//    public void testRookMoves() {
//        Rook rook = new Rook(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(rook, pos);
//
//        List<Position> validMoves = rook.getValidMoves(emptyBoard);
//
//        // Should have 14 possible moves: 7 horizontal and 7 vertical
//        assertEquals(14, validMoves.size());
//
//        // Check some key positions
//        assertTrue(validMoves.contains(new Position(3, 7))); // d8
//        assertTrue(validMoves.contains(new Position(3, 0))); // d1
//        assertTrue(validMoves.contains(new Position(0, 3))); // a4
//        assertTrue(validMoves.contains(new Position(7, 3))); // h4
//    }
//
//    @Test
//    public void testRookCaptures() {
//        Rook rook = new Rook(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(rook, pos);
//
//        // Place an ally piece to block
//        emptyBoard.placePiece(new Pawn(true), new Position(3, 5)); // d6
//
//        // Place an enemy piece to capture
//        emptyBoard.placePiece(new Pawn(false), new Position(6, 3)); // g4
//
//        List<Position> validMoves = rook.getValidMoves(emptyBoard);
//
//        // Check specific blocking behavior
//        assertFalse(validMoves.contains(new Position(3, 6))); // d7 (blocked by ally)
//        assertFalse(validMoves.contains(new Position(3, 7))); // d8 (blocked by ally)
//        assertTrue(validMoves.contains(new Position(3, 4))); // d5
//        assertFalse(validMoves.contains(new Position(3, 5))); // d6 (ally piece)
//
//        assertTrue(validMoves.contains(new Position(6, 3))); // g4 (enemy piece)
//        assertFalse(validMoves.contains(new Position(7, 3))); // h4 (beyond enemy)
//    }
//
//    // KNIGHT TESTS
//
//    @Test
//    public void testKnightMoves() {
//        Knight knight = new Knight(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(knight, pos);
//
//        List<Position> validMoves = knight.getValidMoves(emptyBoard);
//
//        // Knight should have 8 possible moves from the center
//        assertEquals(8, validMoves.size());
//
//        // Check all expected positions
//        assertTrue(validMoves.contains(new Position(1, 2))); // b3
//        assertTrue(validMoves.contains(new Position(1, 4))); // b5
//        assertTrue(validMoves.contains(new Position(2, 1))); // c2
//        assertTrue(validMoves.contains(new Position(2, 5))); // c6
//        assertTrue(validMoves.contains(new Position(4, 1))); // e2
//        assertTrue(validMoves.contains(new Position(4, 5))); // e6
//        assertTrue(validMoves.contains(new Position(5, 2))); // f3
//        assertTrue(validMoves.contains(new Position(5, 4))); // f5
//    }
//
//    @Test
//    public void testKnightEdgeMoves() {
//        Knight knight = new Knight(true);
//        Position pos = new Position(0, 0);  // a1
//        emptyBoard.placePiece(knight, pos);
//
//        List<Position> validMoves = knight.getValidMoves(emptyBoard);
//
//        // Knight on edge should have fewer moves
//        assertEquals(2, validMoves.size());
//        assertTrue(validMoves.contains(new Position(1, 2))); // b3
//        assertTrue(validMoves.contains(new Position(2, 1))); // c2
//    }
//
//    // BISHOP TESTS
//
//    @Test
//    public void testBishopMoves() {
//        Bishop bishop = new Bishop(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(bishop, pos);
//
//        List<Position> validMoves = bishop.getValidMoves(emptyBoard);
//
//        // Bishop should have 13 possible diagonal moves from d4
//        assertEquals(13, validMoves.size());
//
//        // Check some key positions
//        assertTrue(validMoves.contains(new Position(0, 0))); // a1
//        assertTrue(validMoves.contains(new Position(6, 0))); // g1
//        assertTrue(validMoves.contains(new Position(0, 6))); // a7
//        assertTrue(validMoves.contains(new Position(6, 6))); // g7
//    }
//
//    @Test
//    public void testBishopCaptures() {
//        Bishop bishop = new Bishop(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(bishop, pos);
//
//        // Place ally and enemy pieces
//        emptyBoard.placePiece(new Pawn(true), new Position(5, 5)); // f6
//        emptyBoard.placePiece(new Pawn(false), new Position(1, 5)); // b6
//
//        List<Position> validMoves = bishop.getValidMoves(emptyBoard);
//
//        // Check blocking
//        assertFalse(validMoves.contains(new Position(5, 5))); // f6 (ally piece)
//        assertFalse(validMoves.contains(new Position(6, 6))); // g7 (beyond ally)
//        assertTrue(validMoves.contains(new Position(1, 5))); // b6 (enemy piece)
//        assertFalse(validMoves.contains(new Position(0, 6))); // a7 (beyond enemy)
//    }
//
//    // QUEEN TESTS
//
//    @Test
//    public void testQueenMoves() {
//        Queen queen = new Queen(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(queen, pos);
//
//        List<Position> validMoves = queen.getValidMoves(emptyBoard);
//
//        // Queen should have 27 possible moves: 13 diagonal + 14 straight
//        assertEquals(27, validMoves.size());
//
//        // Check some key positions (diagonal)
//        assertTrue(validMoves.contains(new Position(0, 0))); // a1
//        assertTrue(validMoves.contains(new Position(6, 6))); // g7
//
//        // Check some key positions (straight)
//        assertTrue(validMoves.contains(new Position(3, 7))); // d8
//        assertTrue(validMoves.contains(new Position(7, 3))); // h4
//    }
//
//    // KING TESTS
//
//    @Test
//    public void testKingMoves() {
//        King king = new King(true);
//        Position pos = new Position(3, 3);  // d4
//        emptyBoard.placePiece(king, pos);
//
//        List<Position> validMoves = king.getValidMoves(emptyBoard);
//
//        // King should have 8 possible moves in all directions
//        assertEquals(8, validMoves.size());
//
//        // Check all surrounding squares
//        assertTrue(validMoves.contains(new Position(2, 2))); // c3
//        assertTrue(validMoves.contains(new Position(2, 3))); // c4
//        assertTrue(validMoves.contains(new Position(2, 4))); // c5
//        assertTrue(validMoves.contains(new Position(3, 2))); // d3
//        assertTrue(validMoves.contains(new Position(3, 4))); // d5
//        assertTrue(validMoves.contains(new Position(4, 2))); // e3
//        assertTrue(validMoves.contains(new Position(4, 3))); // e4
//        assertTrue(validMoves.contains(new Position(4, 4))); // e5
//    }
//
//    @Test
//    public void testKingCastlingPossible() {
//        King king = new King(true);
//        Position kingPos = new Position(4, 0);  // e1
//        emptyBoard.placePiece(king, kingPos);
//
//        // Add the rooks in starting positions
//        Rook kingsideRook = new Rook(true);
//        emptyBoard.placePiece(kingsideRook, new Position(7, 0)); // h1
//
//        Rook queensideRook = new Rook(true);
//        emptyBoard.placePiece(queensideRook, new Position(0, 0)); // a1
//
//        List<Position> validMoves = king.getValidMoves(emptyBoard);
//
//        // King should have its normal moves plus castling
//        assertTrue(validMoves.contains(new Position(6, 0))); // g1 (kingside castling)
//        assertTrue(validMoves.contains(new Position(2, 0))); // c1 (queenside castling)
//    }
//
//    @Test
//    public void testKingCastlingBlocked() {
//        King king = new King(true);
//        Position kingPos = new Position(4, 0);  // e1
//        emptyBoard.placePiece(king, kingPos);
//
//        // Add the rooks in starting positions
//        Rook kingsideRook = new Rook(true);
//        emptyBoard.placePiece(kingsideRook, new Position(7, 0)); // h1
//
//        Rook queensideRook = new Rook(true);
//        emptyBoard.placePiece(queensideRook, new Position(0, 0)); // a1
//
//        // Block kingside castling
//        emptyBoard.placePiece(new Knight(true), new Position(5, 0)); // f1
//
//        // Block queenside castling
//        emptyBoard.placePiece(new Bishop(true), new Position(1, 0)); // b1
//
//        List<Position> validMoves = king.getValidMoves(emptyBoard);
//
//        // Castling should not be possible
//        assertFalse(validMoves.contains(new Position(6, 0))); // g1 (kingside castling)
//        assertFalse(validMoves.contains(new Position(2, 0))); // c1 (queenside castling)
//    }
//
//    @Test
//    public void testKingCastlingAfterMoving() {
//        King king = new King(true);
//        Position kingPos = new Position(4, 0);  // e1
//        king.setMoved(true); // King has moved
//        emptyBoard.placePiece(king, kingPos);
//
//        // Add the rooks in starting positions
//        Rook kingsideRook = new Rook(true);
//        emptyBoard.placePiece(kingsideRook, new Position(7, 0)); // h1
//
//        Rook queensideRook = new Rook(true);
//        emptyBoard.placePiece(queensideRook, new Position(0, 0)); // a1
//
//        List<Position> validMoves = king.getValidMoves(emptyBoard);
//
//        // Castling should not be possible after king has moved
//        assertFalse(validMoves.contains(new Position(6, 0))); // g1 (kingside castling)
//        assertFalse(validMoves.contains(new Position(2, 0))); // c1 (queenside castling)
//    }
//}