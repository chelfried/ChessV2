package org.chess.core.pieces;

import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Long.reverse;
import static org.chess.core.pieces.Bishop.possibleB;
import static org.chess.core.pieces.King.*;
import static org.chess.core.pieces.Knight.possibleN;
import static org.chess.core.pieces.Pawn.possibleBP;
import static org.chess.core.pieces.Pawn.possibleWP;
import static org.chess.core.pieces.Queen.possibleQ;
import static org.chess.core.pieces.Rook.possibleR;

public class _Piece {

    static long playerPieces;
    static long antiPlayerPieces;
    static long piecesMask;
    static long antiPiecesMask;

    static long[] rankMask = {
            0x00000000000000FFL, 0x000000000000FF00L, 0x0000000000FF0000L, 0x00000000FF000000L,
            0x000000FF00000000L, 0x0000FF0000000000L, 0x00FF000000000000L, 0xFF00000000000000L
    };

    static long[] fileMask = {
            0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
            0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
    };

    static long[] mainDiagMask = {
            0x0000000000000001L, 0x0000000000000102L, 0x0000000000010204L, 0x0000000001020408L,
            0x0000000102040810L, 0x0000010204081020L, 0x0001020408102040L, 0x0102040810204080L,
            0x0204081020408000L, 0x0408102040800000L, 0x0810204080000000L, 0x1020408000000000L,
            0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
    };

    static long[] antiDiagMask = {
            0x0000000000000080L, 0x0000000000008040L, 0x0000000000804020L, 0x0000000080402010L,
            0x0000008040201008L, 0x0000804020100804L, 0x0080402010080402L, 0x8040201008040201L,
            0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L, 0x0804020100000000L,
            0x0402010000000000L, 0x0201000000000000L, 0x0100000000000000L
    };

    public static long[] piece = {
            0x0000000000000001L, 0x0000000000000002L, 0x0000000000000004L, 0x0000000000000008L,
            0x0000000000000010L, 0x0000000000000020L, 0x0000000000000040L, 0x0000000000000080L,
            0x0000000000000100L, 0x0000000000000200L, 0x0000000000000400L, 0x0000000000000800L,
            0x0000000000001000L, 0x0000000000002000L, 0x0000000000004000L, 0x0000000000008000L,
            0x0000000000010000L, 0x0000000000020000L, 0x0000000000040000L, 0x0000000000080000L,
            0x0000000000100000L, 0x0000000000200000L, 0x0000000000400000L, 0x0000000000800000L,
            0x0000000001000000L, 0x0000000002000000L, 0x0000000004000000L, 0x0000000008000000L,
            0x0000000010000000L, 0x0000000020000000L, 0x0000000040000000L, 0x0000000080000000L,
            0x0000000100000000L, 0x0000000200000000L, 0x0000000400000000L, 0x0000000800000000L,
            0x0000001000000000L, 0x0000002000000000L, 0x0000004000000000L, 0x0000008000000000L,
            0x0000010000000000L, 0x0000020000000000L, 0x0000040000000000L, 0x0000080000000000L,
            0x0000100000000000L, 0x0000200000000000L, 0x0000400000000000L, 0x0000800000000000L,
            0x0001000000000000L, 0x0002000000000000L, 0x0004000000000000L, 0x0008000000000000L,
            0x0010000000000000L, 0x0020000000000000L, 0x0040000000000000L, 0x0080000000000000L,
            0x0100000000000000L, 0x0200000000000000L, 0x0400000000000000L, 0x0800000000000000L,
            0x1000000000000000L, 0x2000000000000000L, 0x4000000000000000L, 0x8000000000000000L
    };

    public static String getPseudoMoves(boolean white, long[] board) {

        long wPawn = board[0], wKnight = board[1], wBishop = board[2], wRook = board[3], wQueen = board[4], wKing = board[5];
        long bPawn = board[6], bKnight = board[7], bBishop = board[8], bRook = board[9], bQueen = board[10], bKing = board[11];
        long enPassant = board[12];

        piecesMask = wPawn | wKnight | wBishop | wRook | wQueen | wKing | bPawn | bKnight | bBishop | bRook | bQueen | bKing;
        antiPiecesMask = ~ piecesMask;

        String pseudoMoves;

        if (white) {
            antiPlayerPieces = ~ (wPawn | wKnight | wBishop | wRook | wQueen | wKing | bKing);
            playerPieces = wPawn | wKnight | wBishop | wRook | wQueen;
            pseudoMoves = possibleWP(wPawn, enPassant)
                    + possibleN(wKnight)
                    + possibleB(wBishop)
                    + possibleR(wRook)
                    + possibleQ(wQueen)
                    + possibleK(wKing)
                    + possibleCW(board);
        } else {
            antiPlayerPieces = ~ (bPawn | bKnight | bBishop | bRook | bQueen | bKing | wKing);
            playerPieces = bPawn | bKnight | bBishop | bRook | bQueen;
            pseudoMoves = possibleBP(bPawn, enPassant)
                    + possibleN(bKnight)
                    + possibleB(bBishop)
                    + possibleR(bRook)
                    + possibleQ(bQueen)
                    + possibleK(bKing)
                    + possibleCB(board);
        }

        return pseudoMoves;
    }

    static long orthogonalMoves(int s) {
        long movesHorizontal = (piecesMask - 2 * piece[s]) ^ reverse(reverse(piecesMask) - 2 * reverse(piece[s]));
        long movesVertical = ((piecesMask & fileMask[s % 8]) - (2 * piece[s])) ^ reverse(reverse(piecesMask & fileMask[s % 8]) - (2 * reverse(piece[s])));
        return (movesHorizontal & rankMask[s / 8]) | (movesVertical & fileMask[s % 8]);
    }

    static long diagonalMoves(int s) {
        long movesDiagonal = ((piecesMask & mainDiagMask[(s / 8) + (s % 8)]) - (2 * piece[s])) ^ reverse(reverse(piecesMask & mainDiagMask[(s / 8) + (s % 8)]) - (2 * reverse(piece[s])));
        long movesAntiDiagonal = ((piecesMask & antiDiagMask[(s / 8) + 7 - (s % 8)]) - (2 * piece[s])) ^ reverse(reverse(piecesMask & antiDiagMask[(s / 8) + 7 - (s % 8)]) - (2 * reverse(piece[s])));
        return (movesDiagonal & mainDiagMask[(s / 8) + (s % 8)]) | (movesAntiDiagonal & antiDiagMask[(s / 8) + 7 - (s % 8)]);
    }

    static void attackUtil(StringBuilder list, long movement, int utilIdx, char type) {
        long j = movement & - movement;
        while (j != 0) {
            int idx = numberOfTrailingZeros(j);
            list.append(utilIdx / 8).append(utilIdx % 8).append(idx / 8).append(idx % 8).append(type);
            movement &= ~ j;
            j = movement & - movement;
        }
    }

    public static boolean isCheck(boolean white, long[] board) {
        if(white && ((board[5] & opponentAttack(true,  board)) != 0)){
            return true;
        } else return ! white && ((board[11] & opponentAttack(false, board)) != 0);
    }

    public static long opponentAttack(boolean white, long[] board) {
        long wPawn = board[0], wKnight = board[1], wBishop = board[2], wRook = board[3], wQueen = board[4], wKing = board[5];
        long bPawn = board[6], bKnight = board[7], bBishop = board[8], bRook = board[9], bQueen = board[10], bKing = board[11];

        long underAttack, possibility, i, king, queen, rook, bishop, knight;
        piecesMask = wPawn | wKnight | wBishop | wRook | wQueen | wKing | bPawn | bKnight | bBishop | bRook | bQueen | bKing;

        if (white) {
            underAttack = 0x7f7f7f7f7f7f7f7fL & (bPawn << 7); // pawn capture to right
            underAttack |= 0xfefefefefefefefeL & (bPawn << 9); // pawn capture to left
            king = bKing;
            queen = bQueen;
            rook = bRook;
            bishop = bBishop;
            knight = bKnight;
        } else {
            underAttack = 0xfefefefefefefefeL & (wPawn >> 7); // pawn capture to right
            underAttack |= 0x7f7f7f7f7f7f7f7fL & (wPawn >> 9); // pawn capture to left
            king = wKing;
            queen = wQueen;
            rook = wRook;
            bishop = wBishop;
            knight = wKnight;
        }

        long queenRook = queen | rook; // queen and rook
        i = queenRook & - queenRook;
        while (i != 0) {
            underAttack |= orthogonalMoves(numberOfTrailingZeros(i));
            queenRook &= ~ i;
            i = queenRook & - queenRook;
        }

        long queenBishop = queen | bishop; // queen and bishop
        i = queenBishop & - queenBishop;
        while (i != 0) {
            underAttack |= diagonalMoves(numberOfTrailingZeros(i));
            queenBishop &= ~ i;
            i = queenBishop & - queenBishop;
        }

        i = knight & - knight; // knight
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            if (idx > 18) {
                possibility = 0xa1100110aL << (idx - 18);
            } else {
                possibility = 0xa1100110aL >> (18 - idx);
            }
            if (idx % 8 < 4) {
                possibility &= 0x3f3f3f3f3f3f3f3fL;
            } else {
                possibility &= 0xfcfcfcfcfcfcfcfcL;
            }
            underAttack |= possibility;
            knight &= ~ i;
            i = knight & - knight;

        }

        int idx = numberOfTrailingZeros(king); // king
        if (idx > 9) {
            possibility = 0x70507L << (idx - 9);
        } else {
            possibility = 0x70507L >> (9 - idx);
        }
        if (idx % 8 < 4) {
            possibility &= 0x3f3f3f3f3f3f3f3fL;
        } else {
            possibility &= 0xfcfcfcfcfcfcfcfcL;
        }

        underAttack |= possibility;

        return underAttack;
    }


}
