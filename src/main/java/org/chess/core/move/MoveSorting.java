package org.chess.core.move;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.chess.core.ai.Rating;

import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.getPseudoMoves;
import static org.chess.core.pieces._Piece.isCheck;

public class MoveSorting {

    private static final int[][] MVVLVA = { // Most Valuable Victim - Least Valuable Aggressor
            //p     n       b       r       q       k
            {60000, 120000, 180000, 240000, 300000, 0}, //p
            {50000, 110000, 170000, 230000, 290000, 0}, //n
            {40000, 100000, 160000, 220000, 280000, 0}, //b
            {30000, 90000, 150000, 210000, 270000, 0}, //r
            {20000, 80000, 140000, 200000, 260000, 0}, //q
            {10000, 70000, 130000, 190000, 250000, 0}  //k
    };

    public static List<Move> getLegalSortedMoves(boolean white, long[] board) {

        List<Move> movesPseudo = getPseudoMoves(white, board);

        List<Pair<Integer, Move>> sortedList = new ArrayList<>();

        for (Move move : movesPseudo) {

            long[] updatedBoard = makeMove(board, move);

            int wPiece = 5;
            int bPiece = 5;

            if (updatedBoard[0] != board[0]) {
                wPiece = 0;
            } else if (updatedBoard[1] != board[1]) {
                wPiece = 1;
            } else if (updatedBoard[2] != board[2]) {
                wPiece = 2;
            } else if (updatedBoard[3] != board[3]) {
                wPiece = 3;
            } else if (updatedBoard[4] != board[4]) {
                wPiece = 4;
            }

            if (updatedBoard[6] != board[6]) {
                bPiece = 0;
            } else if (updatedBoard[7] != board[7]) {
                bPiece = 1;
            } else if (updatedBoard[8] != board[8]) {
                bPiece = 2;
            } else if (updatedBoard[9] != board[9]) {
                bPiece = 3;
            } else if (updatedBoard[10] != board[10]) {
                bPiece = 4;
            }

            int aggressor;
            int victim;
            int rating;

            if (white){
                aggressor = wPiece;
                victim = bPiece;
                rating = Rating.calcRating(updatedBoard);
            } else {
                aggressor = bPiece;
                victim = wPiece;
                rating = Rating.calcRating(updatedBoard) * -1;
            }

            if (! isCheck(white, updatedBoard)) {
                sortedList.add(new Pair<>(MVVLVA[aggressor][victim] + rating, move));
            }

        }

        sortedList.sort(Comparator.comparingInt(Pair<Integer, Move>::getKey).reversed());

        List<Move> sortedMoves = new ArrayList<>();

        for (Pair<Integer, Move> move : sortedList) {
            sortedMoves.add(move.getValue());
        }

        return sortedMoves;
    }

}
