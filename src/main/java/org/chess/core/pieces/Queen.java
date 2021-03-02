package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class Queen extends _Piece {

    public static String possibleQ(long queen) {
        StringBuilder pseudoMoves = new StringBuilder();
        long i = queen & - queen;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            attackUtil(pseudoMoves, (orthogonalMoves(idx) | diagonalMoves(idx)) & antiPlayerPieces, idx,'d');
            queen &= ~ i;
            i = queen & - queen;
        }
        return pseudoMoves.toString();
    }

}
