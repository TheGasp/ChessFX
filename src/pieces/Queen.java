/**
 * The `Queen` class represents a queen chess piece.
 * Extends the `Piece` class and implements specific behavior for queens.
 * 
 * @param colour The color of the queen, specified using the `ColourPiece` enum.
 */

package pieces;

import java.lang.Math;
import java.util.ArrayList;
import enums.*;
import plate.Plate;

public class Queen extends Piece{
    
    /**
     * Constructor for the Queen class.
     * 
     * @param colour The color of the queen piece.
     */
	public Queen (ColourPiece colour) {
        super(colour, PieceType.QUEEN, 15);
    }
    
    /**
     * Tests if the queen has the right to move from its current position to a new tested position as a bishop.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the queen's movement is tested.
     * @return True if it's a valid move for the queen.
     */
	private boolean canAccessToBishop(Plate plate, int[] endPos) {
		int[] currentPos = this.getPosition(plate);
		
		int currentx = currentPos[0], currenty = currentPos[1];
		int tox = endPos[0], toy = endPos[1];
		
		if(!areCoordsValid(endPos))
			return false;
		
		// Check if the destination position is occupied by a piece of the same color
		if(plate.matrixPlate[tox][toy] != null && plate.matrixPlate[tox][toy].getColour() == plate.matrixPlate[currentx][currenty].getColour())
			return false;
		
	    // Check if the movement is diagonal
	    if(Math.abs(currentx - tox) != Math.abs(currenty - toy))
	        return false;

	    int dist = Math.abs(currentx - tox);

	    int vect_x = Math.abs(tox - currentx)/(tox - currentx); // no risk of division by 0 (tested in isPosAccessible)
	    int vect_y = Math.abs(toy - currenty)/(toy - currenty);

	    // Check if there are any pieces in the diagonal path
	    for(int i = 1; i < dist; i++){
	        if(plate.matrixPlate[currentx + vect_x*i][currenty + vect_y*i] != null)
	            return false;
	    }
	    
	    return true;
	}
    
    /**
     * Tests if the queen has the right to move from its current position to a new tested position as a rook.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the queen's movement is tested.
     * @return True if it's a valid move for the queen.
     */
    private boolean canAccessToRook(Plate plate, int[] endPos) {
		int[] currentPos = this.getPosition(plate);
		
		int currentx = currentPos[0], currenty = currentPos[1];
		int tox = endPos[0], toy = endPos[1];
		
		if(!areCoordsValid(endPos))
			return false;
		
		// Check if the destination position is occupied by a piece of the same color
		if(plate.matrixPlate[tox][toy] != null && plate.matrixPlate[tox][toy].getColour() == plate.matrixPlate[currentx][currenty].getColour())
			return false;
        
        // Check if the movement is either vertical or horizontal
        if(toy != currenty && tox != currentx)
            return false;
        
        // If the movement is vertical
        if(tox == currentx){
            int vect_dir = (toy>currenty?1:-1);
            // Check if there are any pieces in the vertical path
            for(int i = currenty+vect_dir; i != toy; i+=vect_dir){
                if(plate.matrixPlate[currentx][i] != null)
                    return false;
            }
        }

        // If the movement is horizontal
        else{
            int vect_dir = (tox>currentx?1:-1);
            // Check if there are any pieces in the horizontal path
            for(int i = currentx+vect_dir; i != tox; i+=vect_dir){
                if(plate.matrixPlate[i][currenty] != null)
                    return false;
            }
        }

        return true;
    }
    
    /**
     * Tests if the queen has the right to move to a new position.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the queen's movement is tested.
     * @return True if it's a valid move for the queen.
     */
    public boolean canAccessTo(Plate plate, int[] endPos) {
    	int[] currentPos = plate.getPositionPiece(this);
    	
    	int currentx = currentPos[0], currenty = currentPos[1];
		int tox = endPos[0], toy = endPos[1];
    	
		if(!areCoordsValid(endPos))
			return false;
		
		// Check if the destination position is occupied by a piece of the same color
		if(plate.matrixPlate[tox][toy] != null && plate.matrixPlate[tox][toy].getColour() == plate.matrixPlate[currentx][currenty].getColour())
			return false;
		
 		return canAccessToRook(plate, endPos) || canAccessToBishop(plate, endPos);
    }
    
    /**
     * Gets a list of possible moves for the Queen piece.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param currentPos The current position of the Queen.
     * @return ArrayList of int[][] representing possible moves.
     */
	public ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos) {
		int currentx = currentPos[0], currenty = currentPos[1];
    	
    	if(plate.matrixPlate[currentx][currenty] == null)
    		return null;
    	
    	ArrayList<int[][]> result = new ArrayList<>();

    	// Check possible diagonal moves
    	for(int i = 0; i < 8; i++){
	        int[] destPos = {currentx+i, currenty+i};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});

	        destPos = new int[] {currentx+i, currenty-i};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});

	        destPos = new int[] {currentx-i, currenty+i};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});

	        destPos = new int[] {currentx-i, currenty-i};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});
	    }

	    // Check possible vertical and horizontal moves
	    for(int i = 0; i < 8; i++){
	        int[] destPos = new int[] {i, currenty};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});

	        destPos = new int[] {currentx, i};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});
	    }
	    
	    return result;
	}
}
