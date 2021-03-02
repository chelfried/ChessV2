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

public class _Piece extends _Masks{

    static long playerPieces;
    static long antiPlayerPieces;
    static long piecesMask;
    static long antiPiecesMask;

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

    static void moveUtil(StringBuilder list, long movement, int utilIdx, char type) {
        long m = movement & - movement;
        while (m != 0) {
            int idx = numberOfTrailingZeros(m);
            list.append(utilIdx / 8).append(utilIdx % 8).append(idx / 8).append(idx % 8).append(type);
            movement &= ~ m;
            m = movement & - movement;
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

        long queenRook = queen | rook; // queen orthogonal and rook
        i = queenRook & - queenRook;
        while (i != 0) {
            underAttack |= orthogonalMoves(numberOfTrailingZeros(i));
            queenRook &= ~ i;
            i = queenRook & - queenRook;
        }

        long queenBishop = queen | bishop; // queen diagonal and bishop
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
        underAttack |= kingMask[idx];

        return underAttack;
    }


}
