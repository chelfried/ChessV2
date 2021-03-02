package org.chess.core.ai;

import org.chess.core.GameBoard;
import org.chess.core.GameMechanics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.chess.core.History.getHistory;

public class OpeningBook {

    public static String findMoveInOpeningBook() {

        BufferedReader br;

        String history = getHistory();

        String line;

        StringBuilder move = new StringBuilder();

        try {
            br = new BufferedReader(new FileReader("openingBook.bin"));
            while ((line = br.readLine()) != null) {
                if (line.contains(history)) {
                    move.append(line.charAt(history.length()) - 48);
                    move.append(line.charAt(history.length() + 1) - 48);
                    move.append(line.charAt(history.length() + 2) - 48);
                    move.append(line.charAt(history.length() + 3) - 48);
                    move.append('d');
                    System.out.print("\n***********************\n");
                    System.out.printf("BOOK MOVE FOUND %7s\n", move);
                    System.out.print("***********************\n\n");
                    GameMechanics.wait(2000);
                    return move.toString();
                }
            }
        } catch (IOException e) {
            return null;
        }

        return null;
    }

}
