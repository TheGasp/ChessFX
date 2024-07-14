/**
 * The `King` class represents a king chess piece.
 * Extends the `Piece` class and implements specific behavior for kings.
 * 
 * @param colour The color of the king, specified using the `ColourPiece` enum.
 */

package pieces;

import java.lang.Math;
import java.util.ArrayList;
import plate.*;
import enums.*;

public class King extends Piece{

    /**
     * Constructor for the King class.
     * 
     * @param colour The color of the King piece.
     */
    public King(ColourPiece colour) {
        super(colour, PieceType.KING, 0);
    }
    
    /**
     * Tests if the King has the right to move from its current position to a new tested position.
     *
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the King's movement is tested.
     * @return True if it's a valid move for the King.
     */
    public boolean canAccessTo(Plate plate, int[] endPos) {
        int[] currentPos = this.getPosition(plate);

        int currentX = currentPos[0], currentY = currentPos[1];
        int toX = endPos[0], toY = endPos[1];

        // Check if the destination coordinates are valid
        if (!areCoordsValid(endPos))
            return false;

        // Check if the destination square is occupied by a friendly piece
        if (plate.matrixPlate[toX][toY] != null && plate.matrixPlate[toX][toY].getColour() == plate.matrixPlate[currentX][currentY].getColour())
            return false;

        // Check for castling moves
        if (Math.abs(currentX - toX) > 1) {
            // Castle moves
            if (currentY == toY && plate.matrixPlate[currentX][currentY].isFirstMove()) {
                // Check if it's a big castle
                if (toX == 2) {
                    if (plate.matrixPlate[1][currentY] == null && plate.matrixPlate[3][currentY] == null)
                        return plate.matrixPlate[0][currentY] != null && plate.matrixPlate[0][currentY].isFirstMove();
                    return false;
                }
                // Check if it's a little castle
                else if (toX == 6) {
                    if (plate.matrixPlate[5][currentY] == null)
                        return plate.matrixPlate[7][currentY] != null && plate.matrixPlate[7][currentY].isFirstMove();
                    return false;
                }
                return false;
            }
            return false;
        }

        // Check for regular moves
        if (Math.abs(currentY - toY) > 1)
            return false;

        // Check if the destination is attacked by an opposing King
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (toX + i == currentX && toY + j == currentY)
                    continue;
                int[] currentPosCheck = {toX + i, toY + j};
                if (areCoordsValid(currentPosCheck)) {
                    if (plate.matrixPlate[toX + i][toY + j] != null && plate.matrixPlate[toX + i][toY + j].getType() == PieceType.KING)
                        return false;
                }
            }
        }

        return true;
    }


    /**
     * Gets a list of possible moves for the King piece.
     *
     * @param plate The chessboard on which the pieces are placed.
     * @param currentPos The current position of the King.
     * @return ArrayList of int[][] representing possible moves.
     */
    public ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos) {
        int currentX = currentPos[0], currentY = currentPos[1];

        // Check if there is a King at the specified position
        if (plate.matrixPlate[currentX][currentY] == null)
            return null;

        ArrayList<int[][]> result = new ArrayList<>();

        // Iterate through adjacent squares to find possible moves
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int[] destPos = new int[]{currentX + i, currentY + j};
                // Check if the move is valid according to the chess rules
                if (plate.canMovePiece(plate.matrixPlate[currentX][currentY], destPos))
                    result.add(new int[][]{currentPos, destPos});
            }
        }

        if(this.isFirstMove()) {
            // Check for castling moves
            int[] littleCastle = new int[]{6, currentY};
            int[] bigCastle = new int[]{2, currentY};

            // Check if the castling moves are valid according to the chess rules
            if (plate.canMovePiece(plate.matrixPlate[currentX][currentY], littleCastle))
                result.add(new int[][]{currentPos, littleCastle});
            
            if (plate.canMovePiece(plate.matrixPlate[currentX][currentY], bigCastle))
                result.add(new int[][]{currentPos, bigCastle});
            
        }

        return result;
    }

    
	/**
	 * Checks if the King is in a check position on the chessboard.
	 *
	 * @param plate The chessboard on which the pieces are placed.
	 * @return True if the King is in check.
	 */
	public boolean isInChess(Plate plate) {
	    // Get the current position of the King
	    int[] kingPos = plate.getKingPos(this.colour);

	    // Iterate through the chessboard to check if any opposing piece can access the King
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            // Skip empty squares or pieces of the same color
	            if (plate.matrixPlate[i][j] == null || plate.matrixPlate[i][j].getColour() == this.colour)
	                continue;

	            // Check if the opposing piece can access the King's position
	            if (plate.matrixPlate[i][j].canAccessTo(plate, kingPos))
	                return true;
	        }
	    }

	    // The King is not in check if no opposing piece can access its position
	    return false;
	}

    /**
     * Checks if the King is in a checkmate position on the chessboard.
     *
     * @param plate The chessboard on which the pieces are placed.
     * @return True if the King is in checkmate.
     */
    public boolean isMate(Plate plate) {
        // Get the current position of the King
        int[] kingPos = plate.getKingPos(this.colour);
        int kingPosX = kingPos[0], kingPosY = kingPos[1];

        // Check if there is a King at the specified position
        if (plate.matrixPlate[kingPosX][kingPosY] == null || plate.matrixPlate[kingPosX][kingPosY].getType() != PieceType.KING) {
            System.out.printf("King.isMate: No King found at position (%d, %d).\n", kingPosX, kingPosY);
            return false;
        }

        // Define relative positions for accessible moves around the King
        int[][] relativeAccessible = {
                {0, 1},
                {1, 1},
                {1, 0},
                {1, -1},
                {0, -1},
                {-1, -1},
                {-1, 0},
                {-1, 1},
        };

        // Check if there is any accessible move that can prevent checkmate
        for (int i = 0; i < 8; i++) {
            int[] destPos = {kingPosX + relativeAccessible[i][0], kingPosY + relativeAccessible[i][1]};
            if (areCoordsValid(destPos)) {
                if (this.canMoveTo(plate, destPos))
                    return false;
            }
        }

        // Check if any friendly piece can block or capture the threatening piece
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (plate.matrixPlate[i][j] == null)
                    continue;

                if (plate.matrixPlate[i][j].getColour() == plate.matrixPlate[kingPosX][kingPosY].getColour()) {
                    if (i == kingPosX && j == kingPosY)
                        continue;

                    ArrayList<int[][]> accessList = AccessiblePositions.getAllPossibleMoves(plate, colour);

                    if (accessList.size() != 0)
                        return false;
                }
            }
        }

        // The King is in checkmate if no moves or blocking actions can prevent checkmate
        return true;
    }

}
