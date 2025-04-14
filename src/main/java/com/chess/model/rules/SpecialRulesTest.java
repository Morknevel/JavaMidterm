//package main.java.com.chess.model.rules;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import main.java.com.chess.model.board.Board;
//import main.java.com.chess.model.board.Position;
//import main.java.com.chess.model.game.Game;
//import main.java.com.chess.model.game.Move;
//import main.java.com.chess.model.piece.Bishop;
//import main.java.com.chess.model.piece.King;
//import main.java.com.chess.model.piece.Knight;
//import main.java.com.chess.model.piece.Pawn;
//import main.java.com.chess.model.piece.Piece;
//import main.java.com.chess.model.piece.Queen;
//import main.java.com.chess.model.piece.Rook;
//import main.java.com.chess.model.rules.MoveValidator;
//import main.java.com.chess.model.GameControllerImpl;
//
//
//public class SpecialRulesTest {
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
//    // CASTLING TESTS
//
//    @Test
//    public void testKingsideCastling() {
//        // Setup a board with only king and kingside rook
//        Board testBoard = new Board(true);
//
//        // Add king and rook
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        Rook whiteRook = new Rook(true);
//        testBoard.placePiece(whiteRook, new Position(7, 0)); // h1
//
//        // Test castling move validation
//        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
//        assertTrue(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
//    }
//
//    @Test
//    public void testQueensideCastling() {
//        // Setup a board with only king and queenside rook
//        Board testBoard = new Board(true);
//
//        // Add king and rook
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        Rook whiteRook = new Rook(true);
//        testBoard.placePiece(whiteRook, new Position(0, 0)); // a1
//
//        // Test castling move validation
//        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(2, 0)));
//        assertTrue(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(2, 0)));
//    }
//
//    @Test
//    public void testCastlingWithBlockedPath() {
//        // Setup a board with king, rook, and blocking piece
//        Board testBoard = new Board(true);
//
//        // Add king and rook
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        Rook whiteRook = new Rook(true);
//        testBoard.placePiece(whiteRook, new Position(7, 0)); // h1
//
//        // Add blocking piece
//        Knight whiteKnight = new Knight(true);
//        testBoard.placePiece(whiteKnight, new Position(5, 0)); // f1
//
//        // Test castling move validation
//        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
//        assertFalse(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
//    }
//
//    @Test
//    public void testCastlingAfterKingMoved() {
//        // Setup a board with king and rook, but king has moved
//        Board testBoard = new Board(true);
//
//        // Add king and rook
//        King whiteKing = new King(true);
//        whiteKing.setMoved(true); // King has moved
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        Rook whiteRook = new Rook(true);
//        testBoard.placePiece(whiteRook, new Position(7, 0)); // h1
//
//        // Test castling move validation
//        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
//        assertFalse(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
//    }
//
//    @Test
//    public void testCastlingAfterRookMoved() {
//        // Setup a board with king and rook, but rook has moved
//        Board testBoard = new Board(true);
//
//        // Add king and rook
//        King whiteKing = new King(true);
//        testBoard.placePiece(whiteKing, new Position(4, 0)); // e1
//
//        Rook whiteRook = new Rook(true);
//        whiteRook.setMoved(true); // Rook has moved
//        testBoard.placePiece(whiteRook, new Position(7, 0)); // h1
//
//        // Test castling move validation
//        assertTrue(moveValidator.isCastlingMove(testBoard, new Position(4, 0), new Position(6, 0)));
//        assertFalse(moveValidator.isCastlingValid(testBoard, new Position(4, 0), new Position(6, 0)));
//    }
//
//    // EN PASSANT TESTS
//
//    @Test
//    public void testEnPassantCapture() {
//        // Setup a board with pawns for en passant
//        Board testBoard = new Board(true);
//
//        // Add the pawns
//        Pawn whitePawn = new Pawn(true);
//        testBoard.placePiece(whitePawn, new Position(1, 4)); // b5
//
//        Pawn blackPawn = new Pawn(false);
//        blackPawn.setMoved(false); // First move
//        testBoard.placePiece(blackPawn, new Position(0, 6)); // a7
//
//        // Simulate black pawn moving two squares forward
//        Position blackPawnStart = new Position(0, 6); // a7
//        Position blackPawnEnd = new Position(0, 4); // a5
//        testBoard.movePiece(blackPawnStart, blackPawnEnd);
//
//        // Test en passant validation
//        assertTrue(moveValidator.isEnPassantMove(testBoard, new Position(1, 4), new Position(0, 5), blackPawnEnd));
//        assertTrue(moveValidator.isEnPassantValid(testBoard, new Position(1, 4), new Position(0, 5), blackPawnEnd));
//    }
//
//    @Test
//    public void testEnPassantTiming() {
//        // Setup game for en passant test
//        Board testBoard = new Board(true);
//
//        // Place pawns
//        Pawn whitePawn = new Pawn(true);
//        whitePawn.setMoved(true);
//        testBoard.placePiece(whitePawn, new Position(1, 4)); // b5
//
//        Pawn blackPawn = new Pawn(false);
//        blackPawn.setMoved(false);
//        testBoard.placePiece(blackPawn, new Position(0, 6)); // a7
//
//        // Move black pawn two squares (creates en passant opportunity)
//        Position blackPawnStart = new Position(0, 6); // a7
//        Position blackPawnEnd = new Position(0, 4); // a5
//        testBoard.movePiece(blackPawnStart, blackPawnEnd);
//
//        // En passant should be valid immediately after the pawn double move
//        assertTrue(moveValidator.isEnPassantValid(testBoard, new Position(1, 4), new Position(0, 5), blackPawnEnd));
//
//        // But not valid for another unrelated pawn
//        assertFalse(moveValidator.isEnPassantValid(testBoard, new Position(1, 4), new Position(2, 5), blackPawnEnd));
//    }
//
//    // PAWN PROMOTION TESTS
//
//    @Test
//    public void testPawnPromotion() {
//        // Setup a board with a pawn about to promote
//        Board testBoard = new Board(true);
//
//        // Add a pawn one step away from promotion
//        Pawn whitePawn = new Pawn(true);
//        Position startPos = new Position(3, 6); // d7
//        testBoard.placePiece(whitePawn, startPos);
//
//        // Test if the move is a valid promotion move
//        Position endPos = new Position(3, 7); // d8
//
//        // Create a GameController to test promotion
//        GameControllerImpl testController = new GameControllerImpl();
//
//        // Get the game from the controller and replace its board
//        Game testGame = testController.getGame();
//        testGame.restart();
//
//        // Setup the test board
//        Board gameBoard = testGame.getBoard();
//        gameBoard.placePiece(new Pawn(true), startPos);
//
//        // Execute the move
//        assertTrue(testController.makeMove(startPos, endPos));
//
//        // Test promotion to queen
//        assertTrue(testController.promotePawn(endPos, Piece.PieceType.QUEEN));
//        Piece promotedPiece = gameBoard.getPieceAt(endPos);
//        assertNotNull(promotedPiece);
//        assertEquals(Piece.PieceType.QUEEN, promotedPiece.getType());
//        assertTrue(promotedPiece.isWhite());
//    }
//
//    @Test
//    public void testPromotionToAllPieceTypes() {
//        Board testBoard = new Board(true);
//
//        // Create a GameController to test promotion
//        GameControllerImpl testController = new GameControllerImpl();
//        Game testGame = testController.getGame();
//        testGame.restart();
//
//        // Test promoting to each piece type
//        for (Piece.PieceType pieceType : new Piece.PieceType[]{
//                Piece.PieceType.QUEEN,
//                Piece.PieceType.ROOK,
//                Piece.PieceType.BISHOP,
//                Piece.PieceType.KNIGHT
//        }) {
//            // Setup a new pawn about to promote
//            Position startPos = new Position(3, 6); // d7
//            Position endPos = new Position(3, 7); // d8
//
//            Board gameBoard = testGame.getBoard();
//            gameBoard.placePiece(null, endPos); // Clear any existing pieces
//            gameBoard.placePiece(new Pawn(true), startPos);
//
//            // Move pawn to promotion square
//            testController.makeMove(startPos, endPos);
//
//            // Test promotion
//            assertTrue(testController.promotePawn(endPos, pieceType));
//            Piece promotedPiece = gameBoard.getPieceAt(endPos);
//
//            assertNotNull(promotedPiece);
//            assertEquals(pieceType, promotedPiece.getType());
//            assertTrue(promotedPiece.isWhite());
//        }
//    }
//
//    @Test
//    public void testCannotPromoteNonPawn() {
//        // Setup a board with a knight (not a pawn)
//        Board testBoard = new Board(true);
//
//        // Add a knight on the last rank
//        Knight whiteKnight = new Knight(true);
//        Position pos = new Position(3, 7); // d8
//        testBoard.placePiece(whiteKnight, pos);
//
//        // Create a GameController to test promotion
//        GameControllerImpl testController = new GameControllerImpl();
//        Game testGame = testController.getGame();
//        testGame.restart();
//
//        // Setup the test board
//        Board gameBoard = testGame.getBoard();
//        gameBoard.placePiece(new Knight(true), pos);
//
//        // Try to promote the knight (should fail)
//        assertFalse(testController.promotePawn(pos, Piece.PieceType.QUEEN));
//
//        // Verify the piece is still a knight
//        Piece piece = gameBoard.getPieceAt(pos);
//        assertEquals(Piece.PieceType.KNIGHT, piece.getType());
//    }
//}