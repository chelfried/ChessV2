package org.chess.core.ai;

import org.chess.core.move.Move;

import java.util.List;

import static org.chess.core.GameMechanics.getPlayerAI;
import static org.chess.core.GameMechanics.isGameRunning;
import static org.chess.core.move.MoveMaker.*;
import static org.chess.core.ai.Rating.calcRating;
import static org.chess.core.move.MoveSorting.getLegalSortedMoves;

public class AlphaBeta extends _CommAI {

    public static int alphaBetaMax(boolean white, long[] board, int alpha, int beta, int currentDepth) {

        if (currentDepth == searchToDepth) {
            leafNodesEvaluated++;
            return calcRating(board) * (getPlayerAI() * 2 - 1);
        }

        List<Move> moves;

        if (currentDepth < searchToDepth - 2) {
            moves = getLegalSortedMoves(white, board);
        } else {
            moves = getPseudoMoves(white, board);
        }

        if (moves.size() == 0) {
            if ((isCheck(white, board))) {
                return Integer.MIN_VALUE / (currentDepth + 1);
            } else {
                return 0;
            }
        }

        for (Move move : moves) {

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
                                    (char) (move.getFrom() % 8 + 97),
                                    Math.abs(move.getFrom() / 8 - 8),
                                    (char) (move.getDest() % 8 + 97),
                                    Math.abs(move.getDest() / 8 - 8),
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

        List<Move> moves;

        if (currentDepth < searchToDepth - 2) {
            moves = getLegalSortedMoves(white, board);
        } else {
            moves = getPseudoMoves(white, board);
        }

        if (moves.size() == 0) {
            if (isCheck(white, board)) {
                return Integer.MAX_VALUE / (currentDepth + 1);
            } else {
                return 0;
            }
        }

        for (Move move : moves) {

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
