/**
 * The `Rook` class represents a rook chess piece.
 * Extends the `Piece` class and implements specific behavior for rooks.
 * 
 * @param colour The color of the rook, specified using the `ColourPiece` enum.
 */
package pieces;

import plate.Plate;
import java.util.ArrayList;
import enums.*;

public class Rook extends Piece{
    
    /**
     * Constructor for the Rook class.
     * 
     * @param colour The color of the rook piece.
     */
    public Rook (ColourPiece colour) {
        super(colour, PieceType.ROOK, 4);
    }
    
    /**
     * Tests if the rook has the right to move from its current position to a new tested position.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param endPos The position to which the rook's movement is tested.
     * @return True if it's a valid move for the rook.
     */
    public boolean canAccessTo(Plate plate, int[] endPos) {
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
            // Check if there are any pieces in the path
            for(int i = currenty+vect_dir; i != toy && i >= 0 && i < 8; i+=vect_dir){
                if(plate.matrixPlate[currentx][i] != null)
                    return false;
            }
        }

        // If the movement is horizontal
        else{
            int vect_dir = (tox>currentx?1:-1);
            // Check if there are any pieces in the path
            for(int i = currentx+vect_dir; i != tox && i >= 0 && i < 8; i+=vect_dir){
                if(plate.matrixPlate[i][currenty] != null)
                    return false;
            }
        }

        return true;
    }
    
    /**
     * Gets a list of possible moves for the Rook piece.
     * 
     * @param plate The chessboard on which the pieces are placed.
     * @param currentPos The current position of the Rook.
     * @return ArrayList of int[][] representing possible moves.
     */
	public ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos) {
		int currentx = currentPos[0], currenty = currentPos[1];
    	
    	if(plate.matrixPlate[currentx][currenty] == null)
    		return null;
    	
    	ArrayList<int[][]> result = new ArrayList<>();
    	
        // Check possible vertical moves
        for(int i = 0; i < 8; i++){
            int[] destPos = new int[] {i, currenty};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});

            // Check possible horizontal moves
            destPos = new int[] {currentx, i};
    		if(plate.canMovePiece(plate.matrixPlate[currentx][currenty], destPos))
    			result.add(new int[][] {currentPos, destPos});
        }

    	
    	return result;
	}
}
