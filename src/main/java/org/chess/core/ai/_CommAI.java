package org.chess.core.ai;

import org.chess.core.move.Move;

import static org.chess.core.GameMechanics.getActiveBitboard;
import static org.chess.core.GameMechanics.getPlayerAI;

public class _CommAI {

    static int searchToDepth;
    static Move bestMove;
    static int searchTime;

    static int leafNodesEvaluated;

    public static Move moveSearchAI() {

        bestMove = null;
        leafNodesEvaluated = 0;
        searchToDepth = 7;
        searchTime = 0;

        System.out.print("\n***********************");
        startStopWatch();
        AlphaBeta.alphaBetaMax(getPlayerAI() == 1, getActiveBitboard(), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        searchTime = endStopWatch();
        System.out.print("\n***********************\n");
        System.out.printf("termin nodes %10d\n", leafNodesEvaluated);
        System.out.printf("time (milli) %10d\n", searchTime);
        System.out.printf("search depth %10d\n", searchToDepth);
        System.out.printf("nodes/second %10d\n", (leafNodesEvaluated / (searchTime + 1)) * 1000);
        System.out.print("***********************\n\n");

        return bestMove;
    }

    static long createdMillis = System.currentTimeMillis();

    public static void startStopWatch() {
        createdMillis = System.currentTimeMillis();
    }

    public static int endStopWatch() {
        long currentTimeInMillis = System.currentTimeMillis();
        return (int) ((currentTimeInMillis - createdMillis));
    }

}
