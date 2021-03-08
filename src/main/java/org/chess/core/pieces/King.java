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

        if (board[5] == 0x1000000000000000L) { // king present at e1

            if ((0x8L & board[13]) != 0 && // king side castling rights
                    (board[3] & 0x8000000000000000L) != 0 && // rook present at h1
                    (0x6000000000000000L & piecesMask) == 0 && // f1, g1 not occupied
                    (0x7000000000000000L & attacked(true, board)) == 0) { // e1, f1, g1 not under attack
                pseudoMoves.append("7476C");
            }

            if ((0x4L & board[13]) != 0 && // queen side castling rights
                    (board[3] & 0x100000000000000L) != 0 && // rook present at a1
                    (0xe00000000000000L & piecesMask) == 0 && // b1, c1, d1 not occupied
                    (0x1c00000000000000L & attacked(true, board)) == 0) { // c1, d1, e1 not under attack
                pseudoMoves.append("7472C");
            }

        }

        return pseudoMoves.toString();
    }

    public static String possibleCB(long[] board) {
        StringBuilder pseudoMoves = new StringBuilder();

        if (board[11] == 0x10L) { // king present at e8

            if ((0x2L & board[13]) != 0 && // king side castling rights
                    (board[9] & 0x80L) != 0 && // rook present at h8
                    (0x60L & piecesMask) == 0 && // e8, f8, g8 not occupied
                    (0x70L & attacked(false, board)) == 0) { // f8, g8 not under attack
                pseudoMoves.append("0406C");
            }

            if ((0x1L & board[13]) != 0 && // queen side castling rights
                    (board[9] & 0x1L) != 0 && // rook present at a8
                    (0xeL & piecesMask) == 0 && // b8, c8, d8 not occupied
                    (0x1cL & attacked(false, board)) == 0) { // c8, d8, e8 not under attack
                pseudoMoves.append("0402C");
            }

        }

        return pseudoMoves.toString();
    }

}