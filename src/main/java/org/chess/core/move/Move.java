package org.chess.core.move;

public class Move {

    private final int from;
    private final int dest;
    private final int type;
    private int rate;

    /**
     * @param from starting field of piece to be moved
     * @param dest destination field of piece to be moved
     * @param type set the type of move:
     * 0:     regular move
     * 1:     double push, marker for en passant
     * 2:     en passant
     * 3:     castling
     * 11-14: promotion, white pieces (knight, bishop, rook, queen)
     * 21-24: promotion, black pieces (knight, bishop, rook, queen)
     * @param rate will be set to >0 if capturing move, used move sorting
     */

    public Move(int from, int dest, int type, int rate) {
        this.from = from;
        this.dest = dest;
        this.type = type;
        this.rate = rate;
    }

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

    public int getRate() {
        return rate;
    }

    public void setRate(int capt) {
        this.rate = capt;
    }
}
