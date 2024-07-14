/**
 * The `Pawn` class represents a pawn chess piece.
 * Extends the `Piece` class and implements specific behavior for pawns.
 * 
 * @param colour The color of the pawn, specified using the `ColourPiece` enum.
 */

package pieces;

import java.lang.Math;
import java.util.ArrayList;
import plate.*;
import enums.*;

public class Pawn extends Piece {
    /**
     * Constructor for the Pawn class.
     * 
     * @param colour The color of the pawn piece.
     */
	public Pawn (ColourPiece colour) {
        super(colour, PieceType.PAWN, 1);
    }
    
    /**
     * Tests if the pawn has the right to move from its current position to a new tested position.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the pawn's movement is tested.
     * @return True if it's a valid move for the pawn.
     */
	public boolean canAccessTo(Plate plate, int[] endPos) {
		int[] currentPos = this.getPosition(plate);
		
		int currentx = currentPos[0], currenty = currentPos[1];
		int tox = endPos[0], toy = endPos[1];
		
		// Check if the destination coordinates are valid
		if(!areCoordsValid(endPos))
			return false;
		
		// Check if the destination position is occupied by a piece of the same color
		if(plate.matrixPlate[tox][toy] != null && plate.matrixPlate[tox][toy].getColour() == plate.matrixPlate[currentx][currenty].getColour())
			return false;
		
		// Forward move of one case
	    if(toy == currenty + (plate.matrixPlate[currentx][currenty].getColour() == ColourPiece.WHITE ? 1 : -1)){
	        if(tox == currentx)
	            return plate.matrixPlate[tox][toy] == null;
	        // Move diagonally to capture a piece
	        else if(Math.abs(tox - currentx) == 1)
	            return plate.matrixPlate[tox][toy] != null;
	        return false;
	    }
	    
	 // If it's the first move of the pawn, it can move two cases
	    if(plate.matrixPlate[currentx][currenty].isFirstMove()){
	        if(plate.matrixPlate[currentx][currenty].getColour() == ColourPiece.WHITE){
	            if(toy == currenty + 2 && tox == currentx){
	                    return plate.matrixPlate[tox][toy-1] == null && plate.matrixPlate[tox][toy] == null;
	            }
	        }
	        else{
	            if(toy == currenty - 2 && tox == currentx){
	                    return plate.matrixPlate[tox][toy+1] == null && plate.matrixPlate[tox][toy] == null;
	            }
	        }
	    }

	    return false;
	}
	
	/**
     * Gets a list of possible moves for the Pawn piece.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param currentPos The current position of the Pawn.
     * @return ArrayList of int[][] representing possible moves.
     */
	public ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos){
		Piece currentPiece = plate.matrixPlate[currentPos[0]][currentPos[1]];
		
		ArrayList<int[][]> result = new ArrayList<>();
		
	    int vect_dir = currentPiece.getColour() == ColourPiece.WHITE ? 1 : -1;

	    // Forward move of one case
	    int[] destPos = {currentPos[0], currentPos[1]+vect_dir};
	    if(plate.canMovePiece(currentPiece, destPos))
	    	result.add(new int[][] {currentPos, destPos});

	    // Forward move of two cases (if it's the first move)
	    destPos = new int[] {currentPos[0], currentPos[1]+2*vect_dir};
	    if(plate.canMovePiece(currentPiece, destPos))
	    	result.add(new int[][] {currentPos, destPos});
	        
	    // Diagonal move to capture a piece on the right
	    destPos = new int[] {currentPos[0]+1, currentPos[1]+vect_dir};
	    if(plate.canMovePiece(currentPiece, destPos))
	    	result.add(new int[][] {currentPos, destPos});
	        
	    // Diagonal move to capture a piece on the left
	    destPos = new int[] {currentPos[0]-1, currentPos[1]+vect_dir};
	    if(plate.canMovePiece(currentPiece, destPos))
	    	result.add(new int[][] {currentPos, destPos});
	    
	    return result;
	}
}
