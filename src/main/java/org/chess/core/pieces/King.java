package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;

public class King extends _Piece {

    private final static long[] kingMask = {
            0x0000000000000302L, 0x0000000000000705L, 0x0000000000000e0aL, 0x0000000000001c14L,
            0x0000000000003828L, 0x0000000000007050L, 0x000000000000e0a0L, 0x000000000000c040L,
            0x0000000000030203L, 0x0000000000070507L, 0x00000000000e0a0eL, 0x00000000001c141cL,
            0x0000000000382830L, 0x0000000000705070L, 0x0000000000e0a0e0L, 0x0000000000c040c0L,
            0x0000000003020300L, 0x0000000007050700L, 0x000000000e0a0e00L, 0x000000001c141c00L,
            0x0000000038283800L, 0x0000000070507000L, 0x00000000e0a0e000L, 0x00000000c040c000L,
            0x0000000302030000L, 0x0000000705070000L, 0x0000000e0a0e0000L, 0x0000001c141c0000L,
            0x0000003828380000L, 0x0000007050700000L, 0x000000e0a0e00000L, 0x000000c040c00000L,
            0x0000030203000000L, 0x0000070507000000L, 0x00000e0a0e000000L, 0x00001c141c000000L,
            0x0000382838000000L, 0x0000705070000000L, 0x0000e0a0e0000000L, 0x0000c040c0000000L,
            0x0003020300000000L, 0x0007050700000000L, 0x000e0a0e00000000L, 0x001c141c00000000L,
            0x0038283800000000L, 0x0070507000000000L, 0x00e0a0e000000000L, 0x00c040c000000000L,
            0x0302030000000000L, 0x0705070000000000L, 0x0e0a0e0000000000L, 0x1c141c0000000000L,
            0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L, 0xc040c00000000000L,
            0x0203000000000000L, 0x0507000000000000L, 0x0a0e000000000000L, 0x141c000000000000L,
            0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L,
    };

    public static String possibleK(long K) {
        StringBuilder pseudoMoves = new StringBuilder();
        int idx = numberOfTrailingZeros(K);
        long possibility = kingMask[idx];
        possibility &= antiPlayerPieces;
        attackUtil(pseudoMoves, possibility, idx,'d');
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