package org.chess.core;

import static org.chess.core.pieces._Masks.getPieceAt;

public class GameBoard {

    protected static char[] charBoard = new char[64];

    private static char[] setupCharBoard() {
        return new char[]{
                'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r',
                'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P',
                'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R',
        };
    }

    protected static long[] bitboard = {
            0x0L,   //  board[0]   white pawn board
            0x0L,   //  board[1]   white knight board
            0x0L,   //  board[2]   white bishop board
            0x0L,   //  board[3]   white rook board
            0x0L,   //  board[4]   white queen board
            0x0L,   //  board[5]   white king board
            0x0L,   //  board[6]   black pawn board
            0x0L,   //  board[7]   black knight board
            0x0L,   //  board[8]   black bishop board
            0x0L,   //  board[9]   black rook board
            0x0L,   //  board[10]  black queen board
            0x0L,   //  board[11]  black king board
            0x0L,   //  board[12]  en passant rights
            0xfL,   //  board[13]  castling rights
    };

    private static final char[] charUtil = {'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'};

    public static void charBoardToBitboard() {
        charBoard = setupCharBoard();
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 12; j++) {
                if (charUtil[j] == charBoard[i]) {
                    bitboard[j] += getPieceAt(i);
                }
            }
        }
    }

    public static void bitboardToCharBoard() {
        for (int i = 0; i < 64; i++) {
            charBoard[i] = '.';
            for (int j = 0; j < 12; j++) {
                if ((getPieceAt(i) & bitboard[j]) != 0) {
                    charBoard[i] = charUtil[j];
                }
            }
        }
    }

    public static char[] getCharBoard() {
        return charBoard;
    }

}
