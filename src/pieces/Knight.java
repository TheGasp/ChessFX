/**
 * The `Knight` class represents a knight chess piece.
 * Extends the `Piece` class and implements specific behavior for knights.
 * 
 * @param colour The color of the knight, specified using the `ColourPiece` enum.
 */

package pieces;

import java.lang.Math;
import java.util.ArrayList;
import plate.*;
import enums.*;

public class Knight extends Piece{

    /**
     * Constructor for the Knight class.
     * 
     * @param colour The color of the knight piece.
     */
	public Knight(ColourPiece colour) {
        super(colour, PieceType.KNIGHT, 3);
    }
    
    /**
     * Tests if the knight has the right to move from its current position to a new tested position.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the knight's movement is tested.
     * @return True if it's a valid move for the knight.
     */
	public boolean canAccessTo(Plate plate, int[] endPos) {
        int [] currentPos = this.getPosition(plate);
        int currentx = currentPos[0], currenty = currentPos[1];
        
        int tox = endPos[0], toy = endPos[1];
		
		if(!areCoordsValid(endPos))
			return false;
		
		// Check if the destination position is occupied by a piece of the same color
		if(plate.matrixPlate[tox][toy] != null && plate.matrixPlate[tox][toy].getColour() == plate.matrixPlate[currentx][currenty].getColour())
			return false;
		
        // Check if the movement is L-shaped (knight's valid moves)
        if ((Math.abs(currentPos[0] - endPos[0]) == 1 && Math.abs(currentPos[1] - endPos[1]) == 2)
                ||(Math.abs(currentPos[0] - endPos[0]) == 2 && Math.abs(currentPos[1] - endPos[1]) == 1)){
            return true;
        }
        return false;
		
	}
	
	/**
     * Gets a list of possible moves for the Knight piece.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param currentPos The current position of the Knight.
     * @return ArrayList of int[][] representing possible moves.
     */
	public ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos) {
    	int currentx = currentPos[0], currenty = currentPos[1];
    	
    	ArrayList<int[][]> result = new ArrayList<>();

        // Loop through possible relative positions for the knight's moves
        for(int i = -2; i <= 2; i++){
            for(int j = -2; j <= 2; j++){
                int[] destPos = {currentx+i, currenty+j};
                if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
        	    	result.add(new int[][] {currentPos, destPos});
            }
        }
    	return result;
    }
}
