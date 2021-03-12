package org.chess.core.move;

public class Move {

    private final int from;
    private final int dest;
    private final int type;

    public Move(int from, int dest, int type) {
        this.from = from;
        this.dest = dest;
        this.type = type; // 0 - regular move
                          // 1 - double push, marker for en passant
                          // 2 - en passant
                          // 3 - castling
                          // 11-14 - promotion, white pieces
                          // 21-24 - promotion, black pieces
    }

    public int getFrom() {
        return from;
    }

    public int getDest() {
        return dest;
    }

    public int getType() {
        return type;
    }

}
