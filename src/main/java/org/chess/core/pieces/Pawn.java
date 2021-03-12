package org.chess.core.pieces;

import org.chess.core.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends _Piece {

    public static List<Move> possibleWP(long wPawn, long eP) {
        List<Move> pseudoMoves = new ArrayList<>();
        List<Integer> positions = getBitPositions(wPawn);

        for (Integer pos : positions) {

            long movement;

            // move forward
            movement = wPAdvance[pos] & antiPiecesMask;
            moveUtil(pseudoMoves, movement, pos, 0);

            // captures
            movement = wPAttack[pos] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, movement, pos, 0);

            // double push
            if ((wPAdvance[pos] & antiPiecesMask) != 0) {
                movement = wPDouble[pos] & antiPiecesMask;
                moveUtil(pseudoMoves, movement, pos, 1);
            }

            // en passant
            movement = wPAttack[pos] & eP;
            moveUtil(pseudoMoves, movement, pos, 2);

            // promotion - move forward
            movement = wPPromotionAdv[pos] & antiPiecesMask;

            moveUtil(pseudoMoves, movement, pos, 11); // white bishop
            moveUtil(pseudoMoves, movement, pos, 12); // white knight
            moveUtil(pseudoMoves, movement, pos, 13); // white rook
            moveUtil(pseudoMoves, movement, pos, 14); // white queen

            // promotion - captures
            movement = wPPromotionAtt[pos] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, movement, pos, 11); // white bishop
            moveUtil(pseudoMoves, movement, pos, 12); // white knight
            moveUtil(pseudoMoves, movement, pos, 13); // white rook
            moveUtil(pseudoMoves, movement, pos, 14); // white queen

        }

        return pseudoMoves;
    }

    public static List<Move> possibleBP(long bPawn, long eP) {
        List<Move> pseudoMoves = new ArrayList<>();
        List<Integer> positions = getBitPositions(bPawn);

        for (Integer pos : positions) {

            long movement;

            // move forward
            movement = bPAdvance[pos] & antiPiecesMask;
            moveUtil(pseudoMoves, movement, pos, 0);

            // captures
            movement = bPAttack[pos] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, movement, pos, 0);

            // double push
            if ((bPAdvance[pos] & antiPiecesMask) != 0) {
                movement = bPDouble[pos] & antiPiecesMask;
                moveUtil(pseudoMoves, movement, pos, 1);
            }

            // en passant
            movement = bPAttack[pos] & eP;
            moveUtil(pseudoMoves, movement, pos, 2);

            // promotion - move forward
            movement = bPPromotionAdv[pos] & antiPiecesMask;
            moveUtil(pseudoMoves, movement, pos, 21); // black bishop
            moveUtil(pseudoMoves, movement, pos, 22); // black knight
            moveUtil(pseudoMoves, movement, pos, 23); // black rook
            moveUtil(pseudoMoves, movement, pos, 24); // black queen

            // promotion - captures
            movement = bPPromotionAtt[pos] & antiPlayerPieces & piecesMask;
            moveUtil(pseudoMoves, movement, pos, 21); // black bishop
            moveUtil(pseudoMoves, movement, pos, 22); // black knight
            moveUtil(pseudoMoves, movement, pos, 23); // black rook
            moveUtil(pseudoMoves, movement, pos, 24); // black queen

        }

        return pseudoMoves;
    }


}

