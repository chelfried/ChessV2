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

        StringBuilder pseudoMoves = new StringBuilder();

        if (white) {
            antiPlayerPieces = ~ (wPawn | wKnight | wBishop | wRook | wQueen | wKing | bKing);
            playerPieces = wPawn | wKnight | wBishop | wRook | wQueen;
            pseudoMoves
                    .append(possibleWP(wPawn, enPassant))
                    .append(possibleN(wKnight))
                    .append(possibleB(wBishop))
                    .append(possibleR(wRook))
                    .append(possibleQ(wQueen))
                    .append(possibleK(wKing))
                    .append(possibleCW(board));
        } else {
            antiPlayerPieces = ~ (bPawn | bKnight | bBishop | bRook | bQueen | bKing | wKing);
            playerPieces = bPawn | bKnight | bBishop | bRook | bQueen;
            pseudoMoves
                    .append(possibleBP(bPawn, enPassant))
                    .append(possibleN(bKnight))
                    .append(possibleB(bBishop))
                    .append(possibleR(bRook))
                    .append(possibleQ(bQueen))
                    .append(possibleK(bKing))
                    .append(possibleCB(board));
        }

        return pseudoMoves.toString();
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
        if(white && ((board[5] & attacked(true,  board)) != 0)){
            return true;
        } else return ! white && ((board[11] & attacked(false, board)) != 0);
    }

    public static long attacked(boolean white, long[] board) {

        long underAttack = 0, i, king, queen, rook, bishop, knight, pawn;

        piecesMask = board[0] | board[1] | board[2] | board[3] | board[4] | board[5] |
                board[6] | board[7] | board[8] | board[9] | board[10] | board[11];

        if (white) {
            pawn = board[0];
            king = board[11];
            queen = board[10];
            rook = board[9];
            bishop = board[8];
            knight = board[7];
        } else {
            pawn = board[6];
            king = board[5];
            queen = board[4];
            rook = board[3];
            bishop = board[2];
            knight = board[1];
        }

        i = pawn & - pawn;
        while (i != 0) {
            underAttack |= wPAttack[numberOfTrailingZeros(i)];
            pawn &= ~ i;
            i = pawn & - pawn;
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

        i = knight & - knight;
        while (i != 0) {
            int idx = numberOfTrailingZeros(i);
            underAttack |= knightAttack[idx];
            knight &= ~ i;
            i = knight & - knight;

        }

        int idx = numberOfTrailingZeros(king);
        underAttack |= kingMask[idx];

        return underAttack;
    }


}
