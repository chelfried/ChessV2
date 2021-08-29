package org.chess.core.move;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.chess.core.ai.Rating.calcRating;
import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.getPseudoMoves;
import static org.chess.core.pieces._Piece.isCheck;

public class MoveSorting {

    public static List<Move> getSortedLegalMoves(boolean white, long[] board) {

        List<Move> pseudoMoves = getPseudoMoves(white, board, false);
        List<Move> legalMoves = new ArrayList<>();

        for (Move move : pseudoMoves) {
            long[] tempBoard = makeMove(board, move);
            if (! isCheck(white, tempBoard)) {
                if (move.getRate() == 0) {
                    if (white) {
                        move.setRate(calcRating(tempBoard));
                    } else {
                        move.setRate(calcRating(tempBoard) * - 1);
                    }
                }
                legalMoves.add(move);
            }
        }

        legalMoves.sort(Comparator.comparing(Move::getRate).reversed());

        return legalMoves;
    }

}
