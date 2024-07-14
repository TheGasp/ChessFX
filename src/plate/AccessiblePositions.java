package plate;

import java.util.ArrayList;

import pieces.*;
import enums.*;

/**
 * This class provides methods for retrieving possible moves on the chessboard.
 */
public class AccessiblePositions {

    /**
     * Get possible moves for a given piece at the specified position.
     *
     * @param plate      The game board.
     * @param currentPos The current position of the piece.
     * @return An ArrayList containing possible move positions.
     */
    public static ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos) {
        if (!Piece.areCoordsValid(currentPos) || plate.matrixPlate[currentPos[0]][currentPos[1]] == null)
            return new ArrayList<int[][]>();

        return plate.matrixPlate[currentPos[0]][currentPos[1]].getPossibleMoves(plate, currentPos);
    }

    /**
     * Get all possible moves for a given color on the chessboard.
     *
     * @param plate  The game board.
     * @param colour The color of the player.
     * @return An ArrayList containing all possible move positions for the specified color.
     */
    public static ArrayList<int[][]> getAllPossibleMoves(Plate plate, ColourPiece colour) {
        ArrayList<int[][]> result = new ArrayList<>();

        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 8; l++) {
                // If the piece is of the right color
                if (plate.matrixPlate[k][l] != null && plate.matrixPlate[k][l].getColour() == colour) {
                    // Get all possible moves for this piece
                    int currentx = k, currenty = l;

                    if (plate.matrixPlate[currentx][currenty] != null)
                        result.addAll(plate.matrixPlate[currentx][currenty].getPossibleMoves(plate, new int[]{currentx, currenty}));
                }
            }
        }

        return result;
    }
}
