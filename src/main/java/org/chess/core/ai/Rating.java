package org.chess.core.ai;

import static java.lang.Long.numberOfTrailingZeros;

public class Rating {

    public static int calcRating(long[] board) {

        int rating = 0;

        int bishopValue = 305;
        int knightValue = 333;
        int rookValue = 563;
        int queenValue = 950;
        int kingValue = 20000;

        rating += ratingPawnStructure(board[0], tableWP);
        rating += ratingUtil(board[2], tableWB, bishopValue);  // white bishop
        rating += ratingUtil(board[1], tableWN, knightValue);  // white knight
        rating += ratingUtil(board[3], tableWR, rookValue);    // white rook
        rating += ratingUtil(board[4], tableWQ, queenValue);   // white queen
        rating += ratingUtil(board[5], tableWK, kingValue);    // white king
        rating += bishopPairs(board[2]);

        rating -= ratingPawnStructure(board[6], tableBP);
        rating -= ratingUtil(board[8], tableBB, bishopValue);  // black bishop
        rating -= ratingUtil(board[7], tableBN, knightValue);  // black knight
        rating -= ratingUtil(board[9], tableBR, rookValue);    // black rook
        rating -= ratingUtil(board[10], tableBQ, queenValue);  // black queen
        rating -= ratingUtil(board[11], tableBK, kingValue);   // black king
        rating -= bishopPairs(board[8]);

        return rating;
    }

    public static int ratingUtil(long board, int[] mask, int centiPawns) {
        int rating = 0;
        long i = board & - board;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            rating += mask[idx];
            rating += centiPawns;
            board &= ~ i;
            i = board & - board;
        }
        return rating;
    }

    public static int ratingPawnStructure(long board, int[] mask) {
        int rating = 0;
        long boardT = board;
        long i = board & - board;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            if ((isolatedPawnsMask[idx] & boardT) == 0) {
                rating -= 50; // isolated pawn
            }
            rating += mask[idx];
            rating += 100;
            board &= ~ i;
            i = board & - board;
            if ((doubledPawnsMask[idx % 8] & board) != 0) {
                rating -= 50; // doubled pawn
            }
        }
        return rating;
    }

    public static int bishopPairs(long board) {
        int noOfBishops = 0;
        long i = board & - board;
        while (i != 0) {
            noOfBishops++;
            board &= ~ i;
            i = board & - board;
        }
        if (noOfBishops > 1) {
            return 50;
        } else return 0;
    }

    private final static long[] isolatedPawnsMask = {
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000030203L, 0x0000000000070507L, 0x00000000000e0a0eL, 0x00000000001c141cL,
            0x0000000000382838L, 0x0000000000705070L, 0x0000000000e0a0e0L, 0x0000000000c040c0L,
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
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
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

}
