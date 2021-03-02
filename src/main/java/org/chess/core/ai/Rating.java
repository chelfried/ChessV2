package org.chess.core.ai;

import static java.lang.Long.numberOfTrailingZeros;

public class Rating {

    private final static long[] isolatedPawnsMask = {0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x30203L,
            0x70507L, 0xe0a0eL, 0x1c141cL, 0x382830L, 0x705070L, 0xe0a0e0L, 0xc040c0L, 0x3020300L, 0x7050700L,
            0xe0a0e00L, 0x1c141c00L, 0x38283800L, 0x70507000L, 0xe0a0e000L, 0xc040c000L, 0x302030000L, 0x705070000L,
            0xe0a0e0000L, 0x1c141c0000L, 0x3828380000L, 0x7050700000L, 0xe0a0e00000L, 0xc040c00000L, 0x30203000000L,
            0x70507000000L, 0xe0a0e000000L, 0x1c141c000000L, 0x382838000000L, 0x705070000000L, 0xe0a0e0000000L,
            0xc040c0000000L, 0x3020300000000L, 0x7050700000000L, 0xe0a0e00000000L, 0x1c141c00000000L,
            0x38283800000000L, 0x70507000000000L, 0xe0a0e000000000L, 0xc040c000000000L, 0x302030000000000L,
            0x705070000000000L, 0xe0a0e0000000000L, 0x1c141c0000000000L, 0x3828380000000000L, 0x7050700000000000L,
            0xe0a0e00000000000L, 0xc040c00000000000L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L
    };

    private final static long[] doubledPawnsMask = {0x101010101010101L, 0x202020202020202L, 0x404040404040404L,
            0x808080808080808L, 0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
    };

    private final static int[] tableBP = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, - 20, - 20, 10, 10, 5,
            5, - 5, - 10, 0, 0, - 10, - 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, 5, 10, 25, 25, 10, 5, 5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private final static int[] tableWP = {
            0, 0, 0, 0, 0, 0, 0, 0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5, 5, 10, 25, 25, 10, 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, - 5, - 10, 0, 0, - 10, - 5, 5,
            5, 10, 10, - 20, - 20, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private final static int[] tableBN = {
            - 50, - 40, - 30, - 30, - 30, - 30, - 40, - 50,
            - 40, - 20, 0, 5, 5, 0, - 20, - 40,
            - 30, 5, 10, 15, 15, 10, 5, - 30,
            - 30, 0, 15, 20, 20, 15, 0, - 30,
            - 30, 5, 15, 20, 20, 15, 5, - 30,
            - 30, 0, 10, 15, 15, 10, 0, - 30,
            - 40, - 20, 0, 0, 0, 0, - 20, - 40,
            - 50, - 40, - 30, - 30, - 30, - 30, - 40, - 50
    };

    private final static int[] tableWN = {
            - 50, - 40, - 30, - 30, - 30, - 30, - 40, - 50,
            - 40, - 20, 0, 0, 0, 0, - 20, - 40,
            - 30, 0, 10, 15, 15, 10, 0, - 30,
            - 30, 5, 15, 20, 20, 15, 5, - 30,
            - 30, 0, 15, 20, 20, 15, 0, - 30,
            - 30, 5, 10, 15, 15, 10, 5, - 30,
            - 40, - 20, 0, 5, 5, 0, - 20, - 40,
            - 50, - 40, - 30, - 30, - 30, - 30, - 40, - 50
    };

    private final static int[] tableBB = {
            - 20, - 10, - 10, - 10, - 10, - 10, - 10, - 20,
            - 10, 5, 0, 0, 0, 0, 5, - 10,
            - 10, 10, 10, 10, 10, 10, 10, - 10,
            - 10, 0, 10, 10, 10, 10, 0, - 10,
            - 10, 5, 5, 10, 10, 5, 5, - 10,
            - 10, 0, 5, 10, 10, 5, 0, - 10,
            - 10, 0, 0, 0, 0, 0, 0, - 10,
            - 20, - 10, - 10, - 10, - 10, - 10, - 10, - 20
    };

    private final static int[] tableWB = {
            - 20, - 10, - 10, - 10, - 10, - 10, - 10, - 20,
            - 10, 0, 0, 0, 0, 0, 0, - 10,
            - 10, 0, 5, 10, 10, 5, 0, - 10,
            - 10, 5, 5, 10, 10, 5, 5, - 10,
            - 10, 0, 10, 10, 10, 10, 0, - 10,
            - 10, 10, 10, 10, 10, 10, 10, - 10,
            - 10, 5, 0, 0, 0, 0, 5, - 10,
            - 20, - 10, - 10, - 10, - 10, - 10, - 10, - 20
    };

    private final static int[] tableBR = {
            0, 0, 0, 5, 5, 0, 0, 0,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            5, 10, 10, 10, 10, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private final static int[] tableWR = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, 10, 10, 10, 10, 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            - 5, 0, 0, 0, 0, 0, 0, - 5,
            0, 0, 0, 5, 5, 0, 0, 0
    };

    private final static int[] tableBQ = {
            - 20, - 10, - 10, - 5, - 5, - 10, - 10, - 20,
            - 10, 0, 0, 0, 0, 5, 0, - 10,
            - 10, 0, 5, 5, 5, 5, 5, - 10,
            - 5, 0, 5, 5, 5, 5, 0, 0,
            - 5, 0, 5, 5, 5, 5, 0, - 5,
            - 10, 0, 5, 5, 5, 5, 0, - 10,
            - 10, 0, 0, 0, 0, 0, 0, - 10,
            - 20, - 10, - 10, - 5, - 5, - 10, - 10, - 20
    };

    private final static int[] tableWQ = {
            - 20, - 10, - 10, - 5, - 5, - 10, - 10, - 20,
            - 10, 0, 0, 0, 0, 0, 0, - 10,
            - 10, 0, 5, 5, 5, 5, 0, - 10,
            - 5, 0, 5, 5, 5, 5, 0, - 5,
            0, 0, 5, 5, 5, 5, 0, - 5,
            - 10, 5, 5, 5, 5, 5, 0, - 10,
            - 10, 0, 5, 0, 0, 0, 0, - 10,
            - 20, - 10, - 10, - 5, - 5, - 10, - 10, - 20
    };

    private final static int[] tableBK = {
            30, 20, 10, 0, 0, 10, 20, 30,
            20, 20, 0, 0, 0, 0, 20, 20,
            - 10, - 20, - 20, - 20, - 20, - 20, - 20, - 10,
            - 20, - 30, - 30, - 40, - 40, - 30, - 30, - 20,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30
    };

    private final static int[] tableWK = {
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 30, - 40, - 40, - 50, - 50, - 40, - 40, - 30,
            - 20, - 30, - 30, - 40, - 40, - 30, - 30, - 20,
            - 10, - 20, - 20, - 20, - 20, - 20, - 20, - 10,
            20, 20, 0, 0, 0, 0, 20, 20,
            20, 30, 10, 0, 0, 10, 30, 20
    };

    public static int calcRating(long[] board) {

        long wPawn = board[0], wKnight = board[1], wBishop = board[2], wRook = board[3], wQueen = board[4], wKing = board[5];
        long bPawn = board[6], bKnight = board[7], bBishop = board[8], bRook = board[9], bQueen = board[10], bKing = board[11];

        int rating = 0;
        long i;

        long WPt = wPawn;
        i = wPawn & - wPawn;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            if ((isolatedPawnsMask[idx] & WPt) == 0) {
                rating -= 50; // isolated pawn
            }
            rating += tableWP[idx] + 100;
            wPawn &= ~ i;
            i = wPawn & - wPawn;
            if ((doubledPawnsMask[idx % 8] & wPawn) != 0) {
                rating -= 50; // doubled pawn
            }
        }

        long BPt = bPawn;
        i = bPawn & - bPawn;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            if ((isolatedPawnsMask[idx] & BPt) == 0) {
                rating += 50; // isolated pawn
            }
            rating -= tableBP[idx] + 100;
            bPawn &= ~ i;
            i = bPawn & - bPawn;
            if ((doubledPawnsMask[idx % 8] & bPawn) != 0) {
                rating += 50; // doubled pawn
            }
        }

        i = wKnight & - wKnight;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating += tableWN[idx] + 333;
            wKnight &= ~ i;
            i = wKnight & - wKnight;
        }

        i = bKnight & - bKnight;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating -= tableBN[idx] + 333;
            bKnight &= ~ i;
            i = bKnight & - bKnight;
        }

        int whiteBishops = 0;
        i = wBishop & - wBishop;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating += tableWB[idx] + 305;
            whiteBishops++;
            wBishop &= ~ i;
            i = wBishop & - wBishop;
        }
        if (whiteBishops == 2) {
            rating += 50;
        }

        int blackBishops = 0;
        i = bBishop & - bBishop;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating -= tableBB[idx] + 305;
            blackBishops++;
            bBishop &= ~ i;
            i = bBishop & - bBishop;
        }
        if (blackBishops == 2) {
            rating -= 50;
        }

        i = wRook & - wRook;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating += tableWR[idx] + 563;
            wRook &= ~ i;
            i = wRook & - wRook;
        }

        i = bRook & - bRook;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating -= tableBR[idx] + 563;
            bRook &= ~ i;
            i = bRook & - bRook;
        }

        i = wQueen & - wQueen;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating += tableWQ[idx] + 950;
            wQueen &= ~ i;
            i = wQueen & - wQueen;
        }

        i = bQueen & - bQueen;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating -= tableBQ[idx] + 950;
            bQueen &= ~ i;
            i = bQueen & - bQueen;
        }

        rating += tableWK[numberOfTrailingZeros(wKing)];

        rating -= tableBK[numberOfTrailingZeros(bKing)];

        return rating;
    }

}
