package org.chess.core.ai;

import org.chess.core.GameMechanics;
import org.chess.core.move.Move;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.chess.core.History.getHistory;

public class OpeningBook {

    public static Move findMoveInOpeningBook() {

        BufferedReader br;

        String history = getHistory();

        String line;

        StringBuilder moveAlgebraic = new StringBuilder();

        try {
            br = new BufferedReader(new FileReader("openingBook.bin"));
            while ((line = br.readLine()) != null) {
                if (line.contains(history)) {
                    int idx = history.length();
                    int xFrom = line.charAt(idx) - 48;
                    int yFrom = line.charAt(idx + 1) - 48;
                    int xDest = line.charAt(idx + 2) - 48;
                    int yDest = line.charAt(idx + 3) - 48;
                    System.out.print("\n***********************\n");
                    moveAlgebraic.append((char) (97 + yFrom));
                    moveAlgebraic.append((char) (48 + Math.abs(xFrom - 8)));
                    moveAlgebraic.append((char) (97 + yDest));
                    moveAlgebraic.append((char) (48 + Math.abs(xDest - 8)));
                    System.out.printf("BOOK MOVE FOUND %7s\n", moveAlgebraic.toString());
                    System.out.print("***********************\n\n");
                    GameMechanics.wait(2000);
                    int from = xFrom * 8 + yFrom;
                    int dest = xDest * 8 + yDest;
                    return new Move(from, dest, 0, 0);
                }
            }
        } catch (IOException e) {
            return null;
        }

        return null;
    }

}
