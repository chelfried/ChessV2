package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class King extends _Piece {

    public static String possibleK(long K) {
        StringBuilder pseudoMoves = new StringBuilder();
        int idx = numberOfTrailingZeros(K);
        moveUtil(pseudoMoves, kingMask[idx] & antiPlayerPieces, idx, 'd');
        return pseudoMoves.toString();
    }

    public static String possibleCW(long[] board) {
        StringBuilder pseudoMoves = new StringBuilder();
        long underAttack = opponentAttack(true, board);
        if ((underAttack & board[5]) == 0) {
            if ((0x8L & board[13]) != 0 && ((0x8000000000000000L & board[3]) != 0)) { // king side castling white
                if (((piecesMask | underAttack) & 0x6000000000000000L) == 0) {
                    pseudoMoves.append("7476C");
                }
            }
            if ((0x4L & board[13]) != 0 && ((0x100000000000000L & board[3]) != 0)) { // queen side castling white
                if (((piecesMask | underAttack) & 0xc00000000000000L) == 0) {
                    pseudoMoves.append("7472C");
                }
            }
        }
        return pseudoMoves.toString();
    }

    public static String possibleCB(long[] board) {
        StringBuilder pseudoMoves = new StringBuilder();
        long underAttack = opponentAttack(false, board);
        if ((underAttack & board[11]) == 0) {
            if ((0x2L & board[13]) != 0 && ((0x80L & board[9]) != 0)) { // king side castling black
                if (((piecesMask | underAttack) & 0x60L) == 0) {
                    pseudoMoves.append("0406C");
                }
            }
            if ((1L & board[13]) != 0 && ((1L & board[9]) != 0)) { // queen side castling black
                if (((piecesMask | underAttack) & 0xcL) == 0) {
                    pseudoMoves.append("0402C");
                }
            }
        }
        return pseudoMoves.toString();
    }

}