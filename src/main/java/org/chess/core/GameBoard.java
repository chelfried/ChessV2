package org.chess.core;

public class GameBoard {

    protected static char[] charBoard = new char[64];

    protected static char[] startingCharBoard = {
            'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r',
            'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P',
            'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R',
    };

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

    public static long[] position = {
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

    public static void charBoardToBitboard() {
        charBoard = startingCharBoard;
        for (int i = 0; i < 64; i++) {
            if (charBoard[i] == 'P') {
                bitboard[0] += position[i];
            } else if (charBoard[i] == 'N') {
                bitboard[1] += position[i];
            } else if (charBoard[i] == 'B') {
                bitboard[2] += position[i];
            } else if (charBoard[i] == 'R') {
                bitboard[3] += position[i];
            } else if (charBoard[i] == 'Q') {
                bitboard[4] += position[i];
            } else if (charBoard[i] == 'K') {
                bitboard[5] += position[i];
            } else if (charBoard[i] == 'p') {
                bitboard[6] += position[i];
            } else if (charBoard[i] == 'n') {
                bitboard[7] += position[i];
            } else if (charBoard[i] == 'b') {
                bitboard[8] += position[i];
            } else if (charBoard[i] == 'r') {
                bitboard[9] += position[i];
            } else if (charBoard[i] == 'q') {
                bitboard[10] += position[i];
            } else if (charBoard[i] == 'k') {
                bitboard[11] += position[i];
            }
        }
    }

    public static void bitboardToCharBoard() {
        for (int i = 0; i < 64; i++) {
            if (((bitboard[0] >> i) & 1) == 1) {
                charBoard[i] = 'P';
            } else if (((bitboard[1] >> i) & 1) == 1) {
                charBoard[i] = 'N';
            } else if (((bitboard[2] >> i) & 1) == 1) {
                charBoard[i] = 'B';
            } else if (((bitboard[3] >> i) & 1) == 1) {
                charBoard[i] = 'R';
            } else if (((bitboard[4] >> i) & 1) == 1) {
                charBoard[i] = 'Q';
            } else if (((bitboard[5] >> i) & 1) == 1) {
                charBoard[i] = 'K';
            } else if (((bitboard[6] >> i) & 1) == 1) {
                charBoard[i] = 'p';
            } else if (((bitboard[7] >> i) & 1) == 1) {
                charBoard[i] = 'n';
            } else if (((bitboard[8] >> i) & 1) == 1) {
                charBoard[i] = 'b';
            } else if (((bitboard[9] >> i) & 1) == 1) {
                charBoard[i] = 'r';
            } else if (((bitboard[10] >> i) & 1) == 1) {
                charBoard[i] = 'q';
            } else if (((bitboard[11] >> i) & 1) == 1) {
                charBoard[i] = 'k';
            } else {
                charBoard[i] = ' ';
            }
        }
    }

    public static char[] getCharBoard() {
        return charBoard;
    }

    public static void setupCharBoard(){
        charBoard = startingCharBoard;
    }

}
