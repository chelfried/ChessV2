package org.chess.core.ai;

import org.chess.core.move.Move;

import java.util.Comparator;
import java.util.List;

import static org.chess.core.GameMechanics.getPlayerAI;
import static org.chess.core.GameMechanics.isGameRunning;
import static org.chess.core.ai.Rating.calcRating;
import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.getPseudoMoves;
import static org.chess.core.pieces._Piece.isCheck;

public class Quiescence extends _CommAI {

    public static int quiescenceMax(boolean white, long[] board, int alpha, int beta) {

        int score = calcRating(board) * (getPlayerAI() * 2 - 1);

        if (score >= beta) {
            return beta;
        }

        if (score > alpha) {
            alpha = score;
        }

        List<Move> moves = getPseudoMoves(white, board, true);
        moves.sort(Comparator.comparing(Move::getRate).reversed());

        if (moves.size() == 0) {
            return calcRating(board);
        }

        for (Move move : moves) {
            long[] updatedBoard = makeMove(board, move);
            if (! isCheck(white, updatedBoard)) {
                if (isGameRunning()) {
                    score = quiescenceMin(! white, updatedBoard, alpha, beta);
                    if (score >= beta) {
                        return beta;
                    }
                    if (score > alpha) {
                        alpha = score;
                    }
                }
            }
        }
        return alpha;
    }

    public static int quiescenceMin(boolean white, long[] board, int alpha, int beta) {

        int score = calcRating(board) * (getPlayerAI() * 2 - 1);

        if (score <= alpha) {
            return alpha;
        }

        if (score < beta) {
            beta = score;
        }

        List<Move> moves = getPseudoMoves(white, board, true);
        moves.sort(Comparator.comparing(Move::getRate).reversed());

        if (moves.size() == 0) {
            return calcRating(board);
        }

        for (Move move : moves) {
            long[] updatedBoard = makeMove(board, move);
            if (! isCheck(white, updatedBoard)) {
                score = quiescenceMax(! white, updatedBoard, alpha, beta);
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
