package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class Rook extends _Piece {

    public static String possibleR(long rook) {
        StringBuilder pseudoMoves = new StringBuilder();
        long i = rook & - rook;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            attackUtil(pseudoMoves, orthogonalMoves(idx) & antiPlayerPieces, idx,'d');
            rook &= ~ i;
            i = rook & - rook;
        }
        return pseudoMoves.toString();
    }

}
