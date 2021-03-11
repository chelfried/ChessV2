package org.chess.core.move;

import org.chess.core.pieces._Piece;

public class MoveMaker extends _Piece {

    public static long[] makeMove(long[] board, Move move) {
        return new long[]{
                makeMoveUtil(board[0], move, 10),  // white pawn
                makeMoveUtil(board[1], move, 11),  // white knight
                makeMoveUtil(board[2], move, 12),  // white bishop
                makeMoveUtil(board[3], move, 13),  // white rook
                makeMoveUtil(board[4], move, 14),  // white queen
                makeMoveUtil(board[5], move, 15),  // white king

                makeMoveUtil(board[6], move, 20),  // black pawn
                makeMoveUtil(board[7], move, 21),  // black knight
                makeMoveUtil(board[8], move, 22),  // black bishop
                makeMoveUtil(board[9], move, 23),  // black rook
                makeMoveUtil(board[10], move, 24), // black queen
                makeMoveUtil(board[11], move, 25), // black king

                updateEnPassant(move),
                updateCastling(board[13], move)
        };
    }

    public static long makeMoveUtil(long board, Move move, int boardType) {
        int from = move.getFrom();
        int dest = move.getDest();
        int type = move.getType();

        if (((board >>> from) & 1) == 1) { // normal move
            board &= ~ piece[from];
            board |= piece[dest];
        } else {
            board &= ~ piece[dest];
        }

        if (type >= 11 && type <= 14) { // promotion white piece
            if (type == boardType) {
                board |= piece[dest];
            } else if (boardType == 10) {
                board &= ~ piece[dest];
            }
        } else if (type >= 21 && type <= 24) { // promotion black piece
            if (type == boardType) {
                board |= piece[dest];
            } else if (boardType == 20) {
                board &= ~ piece[dest];
            }
        } else if (type == 2) { // en passant
            if (from >= 32 && from <= 39) {
                board &= ~ (1L << dest - 8);
            } else {
                board &= ~ (1L << dest + 8);
            }
        } else if (type == 3) { // castling
            if (move.getFrom() == 60 && move.getDest() == 58 && boardType == 13) {
                board &= 0xfeffffffffffffffL;
                board |= 0x800000000000000L;
            } else if (move.getFrom() == 60 && move.getDest() == 62 && boardType == 13) {
                board &= 0x7fffffffffffffffL;
                board |= 0x2000000000000000L;
            } else if (move.getFrom() == 4 && move.getDest() == 2 && boardType == 23) {
                board &= 0xfffffffffffffffeL;
                board |= 0x8L;
            } else if (move.getFrom() == 4 && move.getDest() == 6 && boardType == 23) {
                board &= 0xffffffffffffff7fL;
                board |= 0x20L;
            }
        }

        return board;
    }

    public static long updateCastling(long castling, Move move) {
        if (move != null) {
            if (move.getFrom() == 0 || move.getFrom() == 4) { // queen side castling black
                castling &= 0xfffffffffffffffeL;
            }
            if (move.getFrom() == 4 || move.getFrom() == 7) { // king side castling black
                castling &= 0xfffffffffffffffdL;
            }
            if (move.getFrom() == 56 || move.getFrom() == 60) { // queen side castling white
                castling &= 0xfffffffffffffffbL;
            }
            if (move.getFrom() == 60 || move.getFrom() == 63) { // king side castling white
                castling &= 0xfffffffffffffff7L;
            }
        }

        return castling;
    }

    public static long updateEnPassant(Move move) {
        long EP;
        int from = move.getFrom();
        int dest = move.getDest();
        if (move.getType() == 1) {
            if (from >= 48 && from <= 55) {
                EP = (1L << dest - 8);
            } else {
                EP = (1L << dest + 8);
            }
        } else {
            EP = 0L;
        }
        return EP;
    }


}