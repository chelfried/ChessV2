package org.chess.core.pieces;

import org.chess.core.move.Move;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Long.reverse;
import static org.chess.core.pieces.Bishop.possibleB;
import static org.chess.core.pieces.King.*;
import static org.chess.core.pieces.Knight.possibleN;
import static org.chess.core.pieces.Pawn.possibleBP;
import static org.chess.core.pieces.Pawn.possibleWP;
import static org.chess.core.pieces.Queen.possibleQ;
import static org.chess.core.pieces.Rook.possibleR;

public class _Piece extends _Masks {

    static long playerPieces;
    static long antiPlayerPieces;
    static long piecesMask;
    static long antiPiecesMask;
    static long opponentPawn;
    static long opponentKnight;
    static long opponentBishop;
    static long opponentRook;
    static long opponentQueen;
    static long opponentKing;
    static int pieceType;
    static boolean onlyCapt;

    private static final int[][] MVVLVA = { // Most Valuable Victim - Least Valuable Aggressor
            // pawn    knight  bishop  rook    queen   king
            {0xa006, 0xb006, 0xc006, 0xd006, 0xe006, 0x0000}, // pawn
            {0xa005, 0xb005, 0xc005, 0xd005, 0xe005, 0x0000}, // knight
            {0xa004, 0xb004, 0xc004, 0xd004, 0xe004, 0x0000}, // bishop
            {0xa003, 0xb003, 0xc003, 0xd003, 0xe003, 0x0000}, // rook
            {0xa002, 0xb002, 0xc002, 0xd002, 0xe002, 0x0000}, // queen
            {0xa001, 0xb001, 0xc001, 0xd001, 0xe001, 0x0000}  // king
    };

    public static List<Move> getPseudoMoves(boolean white, long[] board, boolean onlyCapt) {
        _Piece.onlyCapt = onlyCapt;

        long wPawn = board[0], wKnight = board[1], wBishop = board[2], wRook = board[3], wQueen = board[4], wKing = board[5];
        long bPawn = board[6], bKnight = board[7], bBishop = board[8], bRook = board[9], bQueen = board[10], bKing = board[11];
        long enPassant = board[12];

        piecesMask = wPawn | wKnight | wBishop | wRook | wQueen | wKing | bPawn | bKnight | bBishop | bRook | bQueen | bKing;
        antiPiecesMask = ~ piecesMask;

        List<Move> pseudoMoves = new ArrayList<>();

        if (white) {
            antiPlayerPieces = ~ (wPawn | wKnight | wBishop | wRook | wQueen | wKing | bKing);
            playerPieces = wPawn | wKnight | wBishop | wRook | wQueen;
            opponentPawn = bPawn;
            opponentKnight = bKnight;
            opponentBishop = bBishop;
            opponentRook = bRook;
            opponentQueen = bQueen;
            opponentKing = bKing;
            pseudoMoves.addAll(possibleWP(wPawn, enPassant));
            pseudoMoves.addAll(possibleN(wKnight));
            pseudoMoves.addAll(possibleB(wBishop));
            pseudoMoves.addAll(possibleR(wRook));
            pseudoMoves.addAll(possibleQ(wQueen));
            pseudoMoves.addAll(possibleK(wKing));
            pseudoMoves.addAll(possibleCW(board));
        } else {
            antiPlayerPieces = ~ (bPawn | bKnight | bBishop | bRook | bQueen | bKing | wKing);
            playerPieces = bPawn | bKnight | bBishop | bRook | bQueen;
            opponentPawn = wPawn;
            opponentKnight = wKnight;
            opponentBishop = wBishop;
            opponentRook = wRook;
            opponentQueen = wQueen;
            opponentKing = wKing;
            pseudoMoves.addAll(possibleBP(bPawn, enPassant));
            pseudoMoves.addAll(possibleN(bKnight));
            pseudoMoves.addAll(possibleB(bBishop));
            pseudoMoves.addAll(possibleR(bRook));
            pseudoMoves.addAll(possibleQ(bQueen));
            pseudoMoves.addAll(possibleK(bKing));
            pseudoMoves.addAll(possibleCB(board));
        }

        return pseudoMoves;
    }

    public static List<Integer> getBitPositions(long bitboard) {
        List<Integer> positions = new ArrayList<>();
        long i = bitboard & - bitboard;
        while (i != 0) {
            positions.add(numberOfTrailingZeros(i));
            bitboard &= ~ i;
            i = bitboard & - bitboard;
        }
        return positions;
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

    static void moveUtil(List<Move> moves, long movement, int utilIdx, int type) {
        long m = movement & - movement;
        while (m != 0) {
            int idx = numberOfTrailingZeros(m);
            int capt = checkForCapture(idx);
            if (onlyCapt) {
                if (capt != 0) {
                    moves.add(new Move(utilIdx, idx, type, capt));
                }
            } else {
                moves.add(new Move(utilIdx, idx, type, capt));
            }
            movement &= ~ m;
            m = movement & - movement;
        }
    }

    static int checkForCapture(int idx) {
        long bit = 1L << idx;
        if ((opponentPawn & bit) != 0) {
            return MVVLVA[pieceType][0];
        } else if ((opponentKnight & bit) != 0) {
            return MVVLVA[pieceType][1];
        } else if ((opponentBishop & bit) != 0) {
            return MVVLVA[pieceType][2];
        } else if ((opponentRook & bit) != 0) {
            return MVVLVA[pieceType][3];
        } else if ((opponentQueen & bit) != 0) {
            return MVVLVA[pieceType][4];
        } else if ((opponentKing & bit) != 0) {
            return MVVLVA[pieceType][5];
        } else {
            return 0;
        }
    }

    public static boolean isCheck(boolean white, long[] board) {
        if (white && ((board[5] & attacked(true, board)) != 0)) {
            return true;
        } else return ! white && ((board[11] & attacked(false, board)) != 0);
    }

    public static long attacked(boolean white, long[] board) {

        long underAttack = 0, i, king, queen, rook, bishop, knight, pawn;

        piecesMask = board[0] | board[1] | board[2] | board[3] | board[4] | board[5] |
                board[6] | board[7] | board[8] | board[9] | board[10] | board[11];

        if (white) {
            pawn = board[6];
            knight = board[7];
            bishop = board[8];
            rook = board[9];
            queen = board[10];
            king = board[11];
        } else {
            pawn = board[0];
            knight = board[1];
            bishop = board[2];
            rook = board[3];
            queen = board[4];
            king = board[5];
        }

        i = pawn & - pawn;
        while (i != 0) {
            if (white) {
                underAttack |= bPAttack[numberOfTrailingZeros(i)];
                underAttack |= bPPromotionAtt[numberOfTrailingZeros(i)];
            } else {
                underAttack |= wPAttack[numberOfTrailingZeros(i)];
                underAttack |= wPPromotionAtt[numberOfTrailingZeros(i)];
            }
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

        if (king != 0) {
            int idx = numberOfTrailingZeros(king);
            underAttack |= kingMask[idx];
        }

        return underAttack;
    }


}
