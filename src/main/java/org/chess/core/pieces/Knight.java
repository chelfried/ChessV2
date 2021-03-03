package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class Knight extends _Piece {

    public static String possibleN(long knight) {
        StringBuilder PseudoMoves = new StringBuilder();
        long i = knight & - knight;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            moveUtil(PseudoMoves, knightAttack[idx] & antiPlayerPieces, idx, 'd');
            knight &= ~ i;
            i = knight & - knight;
        }
        return PseudoMoves.toString();
    }

}
