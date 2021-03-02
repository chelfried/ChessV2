package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class Pawn extends _Piece {

    public static String possibleWP(long wPawn, long eP) {
        StringBuilder pseudoMoves = new StringBuilder();

        long wPawnTemp = wPawn;
        long i = wPawnTemp & - wPawnTemp;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);

            // move forward
            long fM = wPAdvance[idx] & antiPiecesMask;
            moveUtil(pseudoMoves, fM, idx, 'd');

            // double push
            if ((wPAdvance[idx] & antiPiecesMask) != 0) {
                long dP = wPDouble[idx] & antiPiecesMask;
                moveUtil(pseudoMoves, dP, idx, 'e');
            }

            // captures
            long cD = wPAttack[idx] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, cD, idx, 'd');

            // promotion - move forward
            long fP = wPPromotionAdv[idx] & antiPiecesMask;
            moveUtil(pseudoMoves, fP, idx, 'N');
            moveUtil(pseudoMoves, fP, idx, 'B');
            moveUtil(pseudoMoves, fP, idx, 'R');
            moveUtil(pseudoMoves, fP, idx, 'Q');

            // promotion - captures
            long cP = wPPromotionAtt[idx] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, cP, idx, 'N');
            moveUtil(pseudoMoves, cP, idx, 'B');
            moveUtil(pseudoMoves, cP, idx, 'R');
            moveUtil(pseudoMoves, cP, idx, 'Q');

            // en passant
            long ePTemp = wPAttack[idx] & eP;
            moveUtil(pseudoMoves, ePTemp, idx, 'E');

            wPawnTemp &= ~ i;
            i = wPawnTemp & - wPawnTemp;
        }

        return pseudoMoves.toString();
    }

    public static String possibleBP(long bPawn, long eP) {
        StringBuilder pseudoMoves = new StringBuilder();

        long bPawnTemp = bPawn;
        long i = bPawnTemp & - bPawnTemp;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);

            // move forward
            long fM = bPAdvance[idx] & antiPiecesMask;
            moveUtil(pseudoMoves, fM, idx, 'd');

            // double push
            if ((bPAdvance[idx] & antiPiecesMask) != 0) {
                long dP = bPDouble[idx] & antiPiecesMask;
                moveUtil(pseudoMoves, dP, idx, 'e');
            }

            // captures
            long cD = bPAttack[idx] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, cD, idx, 'd');

            // promotion - move forward
            long fP = bPPromotionAdv[idx] & antiPiecesMask;
            moveUtil(pseudoMoves, fP, idx, 'n');
            moveUtil(pseudoMoves, fP, idx, 'b');
            moveUtil(pseudoMoves, fP, idx, 'r');
            moveUtil(pseudoMoves, fP, idx, 'q');

            // promotion - captures
            long cP = bPPromotionAtt[idx] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, cP, idx, 'n');
            moveUtil(pseudoMoves, cP, idx, 'b');
            moveUtil(pseudoMoves, cP, idx, 'r');
            moveUtil(pseudoMoves, cP, idx, 'q');

            // en passant
            long ePTemp = bPAttack[idx] & eP;
            moveUtil(pseudoMoves, ePTemp, idx, 'E');

            bPawnTemp &= ~ i;
            i = bPawnTemp & - bPawnTemp;
        }

        return pseudoMoves.toString();
    }
}

