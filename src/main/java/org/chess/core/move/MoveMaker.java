package org.chess.core.move;

import org.chess.core.pieces._Piece;

import static java.lang.Character.getNumericValue;

public class MoveMaker extends _Piece {

    public static long[] makeMove(long[] board, String move) {
        return new long[]{
                makeMoveUtil(board[0], move, 'P'),
                makeMoveUtil(board[1], move, 'N'),
                makeMoveUtil(board[2], move, 'B'),
                makeMoveUtil(board[3], move, 'R'),
                makeMoveUtil(board[4], move, 'Q'),
                makeMoveUtil(board[5], move, 'K'),
                makeMoveUtil(board[6], move, 'p'),
                makeMoveUtil(board[7], move, 'n'),
                makeMoveUtil(board[8], move, 'b'),
                makeMoveUtil(board[9], move, 'r'),
                makeMoveUtil(board[10], move, 'q'),
                makeMoveUtil(board[11], move, 'k'),
                updateEnPassant(move),
                updateCastling(board[13], move)
        };
    }

    public static long makeMoveUtil(long board, String move, char boardType) {
        int from = (getNumericValue(move.charAt(0)) * 8) + (getNumericValue(move.charAt(1)));
        int to = (getNumericValue(move.charAt(2)) * 8) + (getNumericValue(move.charAt(3)));

        if (((board >>> from) & 1) == 1) { // normal move
            board &= ~ piece[from];
            board |= piece[to];
        } else {
            board &= ~ piece[to];
        }

        if (move.charAt(4) == 'Q' || move.charAt(4) == 'R' || move.charAt(4) == 'B' || move.charAt(4) == 'N') { // promotion white piece
            if (move.charAt(4) == boardType) {
                board |= piece[to];
            } else if (boardType == 'P') {
                board &= ~ piece[to];
            }
        } else if (move.charAt(4) == 'q' || move.charAt(4) == 'r' || move.charAt(4) == 'b' || move.charAt(4) == 'n') { // promotion black piece
            if (move.charAt(4) == boardType) {
                board |= piece[to];
            } else if (boardType == 'p') {
                board &= ~ piece[to];
            }
        } else if (move.charAt(4) == 'E') { // en passant
            if (getNumericValue(move.charAt(0)) == 4) {
                board &= ~ (1L << to - 8);
            } else {
                board &= ~ (1L << to + 8);
            }
        } else if (move.charAt(4) == 'C') { // castling
            if (move.equals("7472C") && boardType == 'R') {
                board &= 0xfeffffffffffffffL;
                board |= 0x800000000000000L;
            } else if (move.equals("7476C") && boardType == 'R') {
                board &= 0x7fffffffffffffffL;
                board |= 0x2000000000000000L;
            } else if (move.equals("0402C") && boardType == 'r') {
                board &= 0xfffffffffffffffeL;
                board |= 0x8L;
            } else if (move.equals("0406C") && boardType == 'r') {
                board &= 0xffffffffffffff7fL;
                board |= 0x20L;
            }
        }

        return board;
    }

    public static long updateCastling(long CT, String move) {
        if (move != null && ! move.equals("")) {
            if (move.startsWith("00") || move.startsWith("04")) { // queen side castling black
                CT &= 0xfffffffffffffffeL;
            }

            if (move.startsWith("04") || move.startsWith("07")) { // king side castling black
                CT &= 0xfffffffffffffffdL;
            }

            if (move.startsWith("70") || move.startsWith("74")) { // queen side castling white
                CT &= 0xfffffffffffffffbL;
            }

            if (move.startsWith("74") || move.startsWith("77")) { // queen side castling white
                CT &= 0xfffffffffffffff7L;
            }
        }

        return CT;
    }

    public static long updateEnPassant(String move) {
        long EP;
        int to = (getNumericValue(move.charAt(2)) * 8) + (getNumericValue(move.charAt(3)));
        if (move.charAt(4) == 'e') {
            if (getNumericValue(move.charAt(0)) == 1) {
                EP = (1L << to - 8);
            } else {
                EP = (1L << to + 8);
            }
        } else {
            EP = 0L;
        }
        return EP;
    }


}