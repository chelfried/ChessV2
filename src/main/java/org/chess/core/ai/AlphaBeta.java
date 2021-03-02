package org.chess.core.ai;

import static org.chess.core.GameMechanics.getPlayerAI;
import static org.chess.core.GameMechanics.isGameRunning;
import static org.chess.core.move.MoveMaker.*;
import static org.chess.core.ai.Rating.calcRating;
import static org.chess.core.move.MoveSorting.getLegalSortedMoves;
import static org.chess.core.pieces._Piece.*;

public class AlphaBeta extends _CommAI {

    public static int alphaBetaMax(boolean white, long[] board, int alpha, int beta, int currentDepth) {

        if (currentDepth == searchToDepth) {
            leafNodesEvaluated++;
            return calcRating(board) * (getPlayerAI() * 2 - 1);
        }

        String moves;

        if (currentDepth < searchToDepth - 2) {
            moves = getLegalSortedMoves(white, board);
        } else {
            moves = getPseudoMoves(white, board);
        }

        if (moves.length() == 0) {
            if ((isCheck(white, board))) {
                return Integer.MIN_VALUE / (currentDepth + 1);
            } else {
                return 0;
            }
        }

        for (int i = 0; i < moves.length(); i += 5) {

            String move = moves.substring(i, i + 5);
            long[] updatedBoard = makeMove(board, move);

            if (! isCheck(white, updatedBoard)) {
                if (isGameRunning()) {
                    int score = alphaBetaMin(! white, updatedBoard, alpha, beta, currentDepth + 1);
                    if (score >= beta) {
                        return beta;
                    }
                    if (score > alpha) {
                        alpha = score;
                        if (currentDepth == 0) {
                            bestMove = move;
                            System.out.printf(
                                    "\n%c%d%c%d %18.2f",
                                    move.charAt(1) + 49, Math.abs((move.charAt(0) - 48) - 8),
                                    move.charAt(3) + 49, Math.abs((move.charAt(2) - 48) - 8),
                                    (float) score / 100
                            );
                        }
                    }
                }
            }

        }
        return alpha;
    }

    public static int alphaBetaMin(boolean white, long[] board, int alpha, int beta, int currentDepth) {

        if (currentDepth == searchToDepth) {
            leafNodesEvaluated++;
            return calcRating(board) * (getPlayerAI() * 2 - 1);
        }

        String moves;

        if (currentDepth < searchToDepth - 2) {
            moves = getLegalSortedMoves(white, board);
        } else {
            moves = getPseudoMoves(white, board);
        }

        if (moves.length() == 0) {
            if ((isCheck(white, board))) {
                return Integer.MAX_VALUE / (currentDepth + 1);
            } else {
                return 0;
            }
        }

        for (int i = 0; i < moves.length(); i += 5) {

            String move = moves.substring(i, i + 5);
            long[] updatedBoard = makeMove(board, move);

            if (! isCheck(white, updatedBoard)) {
                int score = alphaBetaMax(! white, updatedBoard, alpha, beta, currentDepth + 1);
                if (score <= alpha) {
                    return alpha;
                }
                if (score < beta) {
                    beta = score;
                }
            }

        }
        return beta;
    }

}
