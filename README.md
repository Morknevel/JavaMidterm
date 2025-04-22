## Project Overview

This project represents a refactoring of the original chess implementation from [jlundstedt/chess-java](https://github.com/jlundstedt/chess-java). The goal was to improve code organization, maintainability, and testability by properly encapsulating chess logic into a well-structured model separate from the UI components.

## Refactoring Approach

The refactoring followed these guiding principles:

1. **Separation of Concerns**: Clear distinction between model (game logic), view (UI), and controller (interaction)
2. **Encapsulation**: Proper packaging of related functionality
3. **Immutability**: Where appropriate, to prevent unintended state modifications
4. **Testability**: Comprehensive test coverage for game rules and piece movement
5. **Interface-based Design**: Using interfaces to define contracts between components

## Package Structure

The refactored code is organized into the following package structure:

```
main.java.com.chess/
├── ChessApplication.java (main entry point)
├── model/
│   ├── BoardRenderer.java (interface for UI to render the board)
│   ├── GameController.java (interface for UI to control the game)
│   ├── GameControllerImpl.java (implementation of game controller)
│   ├── board/
│   │   ├── Board.java (chess board representation)
│   │   ├── Position.java (immutable position on the board)
│   │   └── Square.java (individual square on the board)
│   ├── game/
│   │   ├── Game.java (main game logic)
│   │   ├── Move.java (representation of a chess move)
│   │   └── Player.java (player representation)
│   ├── piece/
│   │   ├── Piece.java (abstract base class for all pieces)
│   │   ├── Bishop.java
│   │   ├── King.java
│   │   ├── Knight.java
│   │   ├── Pawn.java
│   │   ├── Queen.java
│   │   └── Rook.java
│   └── rules/
│       ├── GameRules.java (game state detection)
│       └── MoveValidator.java (validates chess moves)
└── ui/
    └── ChessUI.java (Swing-based UI implementation)
```

## Key Components

### Model

#### Board and Positions

- `Position`: An immutable class representing a position on the board (file and rank)
- `Square`: Represents a board square that may contain a piece
- `Board`: The chess board with 8x8 grid of squares and methods for piece placement and movement

#### Pieces

- `Piece`: Abstract base class for all chess pieces with shared functionality
- Individual piece classes (King, Queen, Rook, etc.) that implement piece-specific move logic

#### Game Logic

- `Game`: Manages the game state, turn management, move history, and special moves
- `Move`: Represents a chess move with metadata (capture, promotion, etc.)
- `Player`: Represents a player with their captured pieces

#### Rules

- `MoveValidator`: Validates chess moves according to the rules, including special cases
- `GameRules`: Detects game states like checkmate and stalemate

### Controller

- `GameController`: Interface defining how the UI interacts with the game model
- `GameControllerImpl`: Implementation of this interface that delegates to the model

### UI

- `BoardRenderer`: Interface for the model to update the UI
- `ChessUI`: Swing-based UI implementation that renders the board and handles user input

## Special Chess Rules Implementation

The refactored code correctly implements all standard chess rules:

### Castling

Castling is implemented through validation in `MoveValidator.isCastlingValid()` which checks:
- The king and rook haven't moved
- No pieces between king and rook
- King is not in check and doesn't pass through check
- King moves two squares horizontally

### En Passant

En Passant capture is implemented through:
- Tracking the last pawn that made a double move in `Game`
- Validating en passant moves in `MoveValidator.isEnPassantValid()`
- Special handling in `Game.executeEnPassant()` for the capture

### Pawn Promotion

Pawn promotion is handled by:
- Detecting promotion opportunities in `Game.makeMove()`
- Implementing promotion logic in `GameControllerImpl.promotePawn()`
- UI dialog for promotion piece selection in `ChessUI.showPromotionDialog()`

### Check and Checkmate

- `MoveValidator.isInCheck()` detects if a king is under attack
- `MoveValidator.hasValidMoves()` checks if a player has legal moves
- These are combined to detect checkmate and stalemate

## Testing

The refactored code includes comprehensive unit tests:

- `PieceMovementTest`: Tests the movement patterns of all piece types
- `GameStateTest`: Tests game state conditions (check, checkmate, stalemate)
- `SpecialRulesTest`: Tests special chess rules (castling, en passant, promotion)

## Running the Application

To run the chess application:

1. Ensure you have Java 8 or higher installed
2. Compile the source files:
   ```
   javac main/java/com/chess/ChessApplication.java
   ```
3. Run the application:
   ```
   java main.java.com.chess.ChessApplication
   ```

## Playing the Game

1. Click on a piece to select it
2. Valid moves will be highlighted in green
3. Click on a highlighted square to move the selected piece
4. For pawn promotion, a dialog will appear to choose the promotion piece
5. The game automatically detects check, checkmate, and stalemate

## Refactoring Benefits

The refactored code offers several improvements over the original:

1. **Better Encapsulation**: Game logic is now properly encapsulated in model classes
2. **Improved Testability**: Clear separations make unit testing easier and more focused
3. **Enhanced Maintainability**: Logical package structure makes code navigation simpler
4. **Clearer Responsibilities**: Each class has well-defined, focused responsibilities
5. **More Robust**: Comprehensive testing improves reliability

## Future Improvements

While the refactoring addressed many issues, some further improvements could include:

1. Adding a proper graphical representation of chess pieces (instead of letters)
2. Implementing chess clocks for timed games
3. Adding save/load game functionality
4. Network play capabilities
5. Chess notation support (PGN)
6. Undo/redo functionality

## Conclusion

This refactoring project successfully transformed the original chess implementation into a well-structured, maintainable application that follows good software engineering principles. The clear separation between model and UI components makes the code easier to understand, test, and extend in the future.
