package main.java.com.chess.model.board;

public class Position {
    private final int file;
    private final int rank;

    public Position(int file, int rank) {
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            throw new IllegalArgumentException("Position must be within bounds (0-7)");
        }
        this.file = file;
        this.rank = rank;
    }

    public static Position fromAlgebraic(String algebraic) {
        if (algebraic == null || algebraic.length() != 2) {
            throw new IllegalArgumentException("Invalid algebraic notation: " + algebraic);
        }
        char fileChar = algebraic.charAt(0);
        char rankChar = algebraic.charAt(1);
        if (fileChar < 'a' || fileChar > 'h' || rankChar < '1' || rankChar > '8') {
            throw new IllegalArgumentException("Invalid algebraic notation: " + algebraic);
        }
        int file = fileChar - 'a';
        int rank = rankChar - '1';
        return new Position(file, rank);
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public String toAlgebraic() {
        char fileChar = (char) ('a' + file);
        char rankChar = (char) ('1' + rank);
        return "" + fileChar + rankChar;
    }

    public Position offset(int fileDelta, int rankDelta) {
        int newFile = file + fileDelta;
        int newRank = rank + rankDelta;
        if (newFile < 0 || newFile > 7 || newRank < 0 || newRank > 7) {
            return null;
        }
        return new Position(newFile, newRank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return file == position.file && rank == position.rank;
    }

    @Override
    public int hashCode() {
        return 8 * rank + file;
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }
}