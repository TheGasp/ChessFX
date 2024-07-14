package plate;

import java.util.ArrayDeque;
import java.util.Deque;

import pieces.*;
import enums.*;

/**
 * Represents the state of the game board.
 */
class State {
    public int[] startPos;
    public int[] endPos;
    public Piece takenPiece;
    public MoveType lastMoveType;

    /**
     * Initializes a new instance of the State class.
     * @param startPos The starting position of the piece.
     * @param endPos The ending position of the piece.
     * @param takenPiece The piece taken during the move.
     * @param lastMoveType The type of the last move performed.
     */
    State(int[] startPos, int[] endPos, Piece takenPiece, MoveType lastMoveType) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.takenPiece = takenPiece;
        this.lastMoveType = lastMoveType;
    }
}

/**
 * Handles the backup and restoration of the game board state.
 */
public class StateBackuper {
    private Plate plate;

    protected Deque<State> stateStack = new ArrayDeque<>(); // Stack of states of the board as moves are made

    /**
     * Initializes a new instance of the StateBackuper class.
     * @param plate The game plate.
     */
    public StateBackuper(Plate plate) {
        this.plate = plate;
    }

    /**
     * Backs up the state of the plate.
     * @param currentPos The starting position of the piece.
     * @param endPos The ending position of the piece.
     */
    public void backupState(int[] currentPos, int[] endPos) {
        // Pushes a new State onto the stack with information about the move
        this.stateStack.push(new State(currentPos, endPos, this.plate.matrixPlate[endPos[0]][endPos[1]], plate.getLastMoveType()));
    }

    /**
     * Restores the state of the plate.
     */
    public void restoreState() {
        // Pops the last State from the stack and restores the game board state based on the stored information
        State toRestore = this.stateStack.pop();

        switch (plate.lastMoveType) {
            case PAWN_PROMOTION:
                // If the last move was a pawn promotion, recreate the pawn at the ending position
                this.plate.matrixPlate[toRestore.endPos[0]][toRestore.endPos[1]] =
                        new Pawn(this.plate.matrixPlate[toRestore.endPos[0]][toRestore.endPos[1]].getColour());
                break;
            case LITTLE_CASTLE:
                // If the last move was a little castle, perform the necessary updates on the board
                this.plate.matrixPlate[7][toRestore.endPos[1]] = this.plate.matrixPlate[5][toRestore.endPos[1]];
                this.plate.matrixPlate[7][toRestore.endPos[1]].firstMove = true;
                this.plate.matrixPlate[5][toRestore.endPos[1]] = null;
                break;
            case BIG_CASTLE:
                // If the last move was a big castle, perform the necessary updates on the board
                this.plate.matrixPlate[0][toRestore.endPos[1]] = this.plate.matrixPlate[3][toRestore.endPos[1]];
                this.plate.matrixPlate[0][toRestore.endPos[1]].firstMove = true;
                this.plate.matrixPlate[3][toRestore.endPos[1]] = null;
                break;
            default:
                break;
        }

        // Move the piece back to its original position
        this.plate.movePiece(toRestore.endPos, toRestore.startPos);

        // Restore the taken piece to its original position
        this.plate.matrixPlate[toRestore.endPos[0]][toRestore.endPos[1]] = toRestore.takenPiece;

        // Update the lastMoveType in the Plate based on the restored state
        plate.lastMoveType = toRestore.lastMoveType;
    }
}
