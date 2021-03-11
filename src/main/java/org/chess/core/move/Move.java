package org.chess.core.move;

public class Move {

    private final int from;
    private final int dest;
    private final int type;

    public Move(int from, int dest, int type) {
        this.from = from;
        this.dest = dest;
        this.type = type;
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
