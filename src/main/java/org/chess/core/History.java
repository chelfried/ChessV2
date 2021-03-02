package org.chess.core;

public class History {

    private static String history = "#";

    public static void addMoveToHistory(String move){
        history = history.concat(move.substring(0, 4));
    }

    public static void resetHistory() {
        history = "#";
    }

    public static String getHistory() {
        return history;
    }

}
