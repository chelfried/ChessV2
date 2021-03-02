package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class Knight extends _Piece {

    public static String possibleN(long knight) {
        StringBuilder PseudoMoves = new StringBuilder();
        long i = knight & - knight;
        long movement;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            if (idx > 18) {
                movement = 0xa1100110aL << (idx - 18);
            } else {
                movement = 0xa1100110aL >> (18 - idx);
            }
            if (idx % 8 < 4) {
                movement &= 0x3f3f3f3f3f3f3f3fL & antiPlayerPieces;
            } else {
                movement &= 0xfcfcfcfcfcfcfcfcL & antiPlayerPieces;
            }
            attackUtil(PseudoMoves, movement, idx,'d');
            knight &= ~ i;
            i = knight & - knight;
        }
        return PseudoMoves.toString();
    }

}
