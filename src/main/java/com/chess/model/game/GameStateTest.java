//package main.java.com.chess.model.game;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import main.java.com.chess.model.board.Board;
//import main.java.com.chess.model.board.Position;
//import main.java.com.chess.model.piece.King;
//import main.java.com.chess.model.piece.Knight;
//import main.java.com.chess.model.piece.Pawn;
//import main.java.com.chess.model.piece.Queen;
//import main.java.com.chess.model.piece.Rook;
//import main.java.com.chess.model.rules.MoveValidator;
//import main.java.com.chess.model.GameControllerImpl;
//
///**
// * Unit tests for game state conditions including check, checkmate, and stalemate.
// */
//public class GameStateTest {
//    private Game game;
//    private Board board;
//    private MoveValidator moveValidator;
//    private GameControllerImpl gameController;
//
//    @BeforeEach
//    public void setUp() {
//        game = new Game();
//        board = game.getBoard();
//        moveValidator = new MoveValidator();
//        gameController = new GameControllerImpl();
//    }
//
//    // CHECK TESTS
//
//    @Test
//    public void testCheckDetection() {
//        // Setup a board with a king in check
//        Board testBoard = new Board(true);
//
//        // Clear the board first
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // Add white king
//        King whiteKing = new King(true);
//        Position kingPos = new Position(4, 0); // e1
//        testBoard.placePiece(whiteKing, kingPos);
//
//        // Add black queen threatening the king
//        Queen blackQueen = new Queen(false);
//        Position queenPos = new Position(4, 7); // e8
//        testBoard.placePiece(blackQueen, queenPos);
//
//        // Test if white king is in check
//        assertTrue(moveValidator.isInCheck(testBoard, true), "King should be in check");
//
//        // Move the queen to a non-threatening position
//        testBoard.placePiece(null, queenPos); // Remove queen
//        testBoard.placePiece(blackQueen, new Position(0, 0)); // a1 (being captured)
//
//        // King should no longer be in check
//        assertFalse(moveValidator.isInCheck(testBoard, true), "King should not be in check after queen is moved");
//    }
//
//    @Test
//    public void testKingCannotMoveIntoCheck() {
//        // Setup a board with a king that might move into check
//        Board testBoard = new Board(true);
//
//        // Clear the board first
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // Add white king
//        King whiteKing = new King(true);
//        Position kingPos = new Position(4, 0); // e1
//        testBoard.placePiece(whiteKing, kingPos);
//
//        // Add black rook controlling a square
//        Rook blackRook = new Rook(false);
//        Position rookPos = new Position(5, 1); // f2
//        testBoard.placePiece(blackRook, rookPos);
//
//        // Test that the king cannot move to the controlled square
//        assertFalse(moveValidator.isValidMove(testBoard, kingPos, new Position(5, 0)), "King should not be able to move into check");
//
//        // But can move to a safe square
//        assertTrue(moveValidator.isValidMove(testBoard, kingPos, new Position(3, 0)), "King should be able to move to a safe square");
//    }
//
//    @Test
//    public void testMustMoveOutOfCheck() {
//        // Setup a custom game with a king in check
//        Board testBoard = new Board(true);
//
//        // Clear the board first
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // Add white king
//        King whiteKing = new King(true);
//        Position kingPos = new Position(4, 0); // e1
//        testBoard.placePiece(whiteKing, kingPos);
//
//        // Add white pawn that can block
//        Pawn whitePawn = new Pawn(true);
//        Position pawnPos = new Position(3, 1); // d2
//        testBoard.placePiece(whitePawn, pawnPos);
//
//        // Add black queen giving check
//        Queen blackQueen = new Queen(false);
//        Position queenPos = new Position(4, 7); // e8 - vertical check
//        testBoard.placePiece(blackQueen, queenPos);
//
//        // Verify king is in check
//        assertTrue(moveValidator.isInCheck(testBoard, true), "King should be in check");
//
//        // Valid move - blocking the check (pawn to e3)
//        Position validPawnMove = new Position(4, 2); // e3 - blocks the queen
//        assertTrue(moveValidator.isValidMove(testBoard, pawnPos, validPawnMove),
//                "Pawn should be able to block check");
//
//        // Invalid move - doesn't block check (pawn to c3)
//        Position invalidPawnMove = new Position(2, 2); // c3 - doesn't block check
//
//        // Create a copy of the board to test this move
//        Board tempBoard = new Board(testBoard);
//        tempBoard.movePiece(pawnPos, invalidPawnMove);
//
//        // King should still be in check after this move
//        assertTrue(moveValidator.isInCheck(tempBoard, true),
//                "King should still be in check after invalid move");
//    }
//
//    // CHECKMATE TESTS
//
//    @Test
//    public void testCheckmateDetection() {
//        // Setup a board with a checkmate position (fool's mate)
//        Board testBoard = new Board(true);
//
//        // Clear the board first
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // Setup the pieces for a fool's mate
//        // White King
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        // Black King
//        King blackKing = new King(false);
//        testBoard.placePiece(blackKing, new Position(4, 7)); // e8
//
//        // White pawns moved for fool's mate
//        testBoard.placePiece(new Pawn(true), new Position(5, 2)); // f3
//        testBoard.placePiece(new Pawn(true), new Position(6, 3)); // g4
//
//        // Black Queen delivering checkmate
//        testBoard.placePiece(new Queen(false), new Position(7, 3)); // h4
//
//        // Test whether white is in checkmate
//        assertTrue(moveValidator.isInCheck(testBoard, true), "White king should be in check");
//        assertFalse(moveValidator.hasValidMoves(testBoard, true, null),
//                "White should have no valid moves (checkmate)");
//    }
//
//    @Test
//    public void testNearCheckmateButCanBlock() {
//        // Setup a board with a check that can be blocked
//        Board testBoard = new Board(true);
//
//        // Clear the board first
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // White King
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        // Black King
//        King blackKing = new King(false);
//        testBoard.placePiece(blackKing, new Position(4, 7)); // e8
//
//        // Black Queen giving check
//        testBoard.placePiece(new Queen(false), new Position(4, 3)); // e4
//
//        // White Knight that can block
//        testBoard.placePiece(new Knight(true), new Position(3, 1)); // d2
//
//        // Test whether white is in check but not checkmate
//        assertTrue(moveValidator.isInCheck(testBoard, true), "White king should be in check");
//        assertTrue(moveValidator.hasValidMoves(testBoard, true, null),
//                "White should have valid moves (knight can block)");
//    }
//
//    // STALEMATE TESTS
//
//    @Test
//    public void testStalemateDetection() {
//        // Setup a classic stalemate position
//        Board testBoard = new Board(true);
//
//        // Clear the board first
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // White King in corner
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(0, 0)); // a1
//
//        // Black King
//        King blackKing = new King(false);
//        testBoard.placePiece(blackKing, new Position(2, 1)); // c2
//
//        // Black Queen creating stalemate
//        testBoard.placePiece(new Queen(false), new Position(1, 2)); // b3
//
//        // Test for stalemate: white is not in check but has no valid moves
//        assertFalse(moveValidator.isInCheck(testBoard, true),
//                "White king should not be in check in stalemate position");
//        assertFalse(moveValidator.hasValidMoves(testBoard, true, null),
//                "White should have no valid moves in stalemate position");
//    }
//
//    @Test
//    public void testGameStateProgression() {
//        // Create a custom game that we can manipulate
//        GameControllerImpl testController = new GameControllerImpl();
//        Game testGame = testController.getGame();
//        testGame.restart();
//
//        // Initially the game should be in progress
//        assertEquals(Game.GameState.IN_PROGRESS, testGame.getGameState(),
//                "Game should start in progress state");
//
//        // Setup a check position
//        Board testBoard = testGame.getBoard();
//
//        // Clear the board
//        for (int file = 0; file < 8; file++) {
//            for (int rank = 0; rank < 8; rank++) {
//                testBoard.placePiece(null, new Position(file, rank));
//            }
//        }
//
//        // Add kings
//        testBoard.placePiece(new King(true), new Position(4, 0)); // e1
//        testBoard.placePiece(new King(false), new Position(4, 7)); // e8
//
//        // Add a queen to give check
//        testBoard.placePiece(new Queen(false), new Position(4, 1)); // e2
//
//        // Verify king is in check
//        assertTrue(moveValidator.isInCheck(testBoard, true),
//                "White king should be in check");
//    }
//}