package org.chess.core;

import org.chess.core.move.Move;

public class History {

    private static String history = "#";

    public static void addMoveToHistory(Move move){

        String newMove = String.valueOf(move.getFrom() / 8) +
                move.getFrom() % 8 +
                move.getDest() / 8 +
                move.getDest() % 8;
        history = history.concat(newMove);
    }

    public static void resetHistory() {
        history = "#";
    }

    public static String getHistory() {
        return history;
    }

}
