package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class Bishop extends _Piece {

    public static String possibleB(long bishop) {
        StringBuilder pseudoMoves = new StringBuilder();
        long i = bishop & - bishop;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            moveUtil(pseudoMoves, diagonalMoves(idx) & antiPlayerPieces, idx, 'd');
            bishop &= ~ i;
            i = bishop & - bishop;
        }
        return pseudoMoves.toString();
    }

}
