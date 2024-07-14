/**
 * The `Bishop` class represents a bishop chess piece.
 * Extends the `Piece` class and implements specific behavior for bishops.
 * 
 * @param colour The color of the bishop, specified using the `ColourPiece` enum.
 */
package pieces;

import java.util.ArrayList;
import enums.*;
import plate.Plate;

public class Bishop extends Piece {

    /**
     * Constructor for the `Bishop` class.
     * Initializes a bishop with the specified color, piece type, and value.
     * 
     * @param colour The color of the bishop, specified using the `ColourPiece` enum.
     */
    public Bishop(ColourPiece colour) {
        super(colour, PieceType.BISHOP, 3);
    }

    /**
     * Checks if the bishop can access a specified position on the chessboard.
     * 
     * @param plate The chess plate representing the game board.
     * @param endPos The target position to check accessibility.
     * @return `true` if the bishop can access the target position, `false` otherwise.
     */
    public boolean canAccessTo(Plate plate, int[] endPos) {
        // Retrieve the current position of the bishop
        int[] currentPos = this.getPosition(plate);

        int currentx = currentPos[0], currenty = currentPos[1];
        int tox = endPos[0], toy = endPos[1];

        // Validate coordinates
        if (!areCoordsValid(endPos))
            return false;

        // Check for a same-color piece at the destination
        if (plate.matrixPlate[tox][toy] != null && plate.matrixPlate[tox][toy].getColour() == plate.matrixPlate[currentx][currenty].getColour())
            return false;

        // Check diagonal movement
        if (Math.abs(currentx - tox) != Math.abs(currenty - toy))
            return false;

        int dist = Math.abs(currentx - tox);

        int vect_x = Math.abs(tox - currentx) / (tox - currentx);
        int vect_y = Math.abs(toy - currenty) / (toy - currenty);

        // Check for obstacles along the diagonal path
        for (int i = 1; i < dist; i++) {
            if (plate.matrixPlate[currentx + vect_x * i][currenty + vect_y * i] != null)
                return false;
        }

        return true;
    }

    /**
     * Retrieves a list of possible moves for the bishop on the chessboard.
     * 
     * @param plate The chess plate representing the game board.
     * @param currentPos The current position of the bishop on the board.
     * @return An ArrayList containing int[][] arrays representing possible moves.
     */
    public ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos) {
        int currentx = currentPos[0], currenty = currentPos[1];

        // Check if the bishop exists at the current position
        if (plate.matrixPlate[currentx][currenty] == null)
            return null;

        ArrayList<int[][]> result = new ArrayList<>();

        // Check possible moves in all diagonal directions
        for (int i = 0; i < 8; i++) {
            int[] destPos = new int[]{currentx + i, currenty + i};
            if (plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
                result.add(new int[][]{currentPos, destPos});

            destPos = new int[]{currentx + i, currenty - i};
            if (plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
                result.add(new int[][]{currentPos, destPos});

            destPos = new int[]{currentx - i, currenty + i};
            if (plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
                result.add(new int[][]{currentPos, destPos});

            destPos = new int[]{currentx - i, currenty - i};
            if (plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
                result.add(new int[][]{currentPos, destPos});
        }

        return result;
    }
}
