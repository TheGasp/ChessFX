package pieces;

import plate.Plate;

import java.util.ArrayList;

import enums.*;

/**
 * Abstract class representing a chess piece.
 * This class provides common attributes and methods for all chess pieces.
 * Specific pieces should extend this class and implement their own behavior.
 */
public abstract class Piece {

    // Represents the state of the piece (alive or captured).
    protected boolean etat;

    // Array containing all possible piece types.
    public static PieceType[] PieceTypeArray = PieceType.values();

    // Type of the chess piece.
    protected PieceType type;

    // Color of the chess piece.
    protected ColourPiece colour;

    // Flag indicating whether it's the first move of the piece.
    public boolean firstMove;

    // Numerical value assigned to the piece for scoring purposes.
    private int value;

    /**
     * Constructor for the Piece class.
     *
     * @param colour The color of the piece.
     * @param type The type of the piece.
     * @param value The numerical value of the piece.
     */
    public Piece(ColourPiece colour, PieceType type, int value) {
        this.colour = colour;
        this.type = type;
        this.etat = true;
        this.firstMove = true;
        this.value = value;
    }

    /**
     * Checks if the given coordinates are valid on the chessboard.
     *
     * @param coords The coordinates to be validated.
     * @return True if the coordinates are valid, false otherwise.
     */
    public static boolean areCoordsValid(int[] coords) {
        return coords.length == 2 &&
                coords[0] >= 0 &&
                coords[0] < 8 &&
                coords[1] >= 0 &&
                coords[1] < 8;
    }

    /**
     * Retrieves the current position of the piece on the chessboard.
     *
     * @param plate The chessboard.
     * @return The coordinates of the piece on the chessboard.
     */
    public int[] getPosition(Plate plate) {
        return plate.getPositionPiece(this);
    }

    /**
     * Gets the color of the piece.
     *
     * @return The color of the piece.
     */
    public ColourPiece getColour() {
        return this.colour;
    }

    /**
     * Gets the state of the piece (alive or captured).
     *
     * @return True if the piece is alive, false if it's captured.
     */
    public boolean getEtat() {
        return etat;
    }

    /**
     * Gets the type of the piece.
     *
     * @return The type of the piece.
     */
    public PieceType getType() {
        return this.type;
    }

    /**
     * Checks if it's the first move of the piece.
     *
     * @return True if it's the first move, false otherwise.
     */
    public boolean isFirstMove() {
        return this.firstMove;
    }

    /**
     * Gets the numerical value assigned to the piece for scoring purposes.
     *
     * @return The numerical value of the piece.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Abstract method to check if the piece can access a specific position on the chessboard.
     *
     * @param plate The chessboard.
     * @param testedPosition The position to be tested for access.
     * @return True if the piece can access the position, false otherwise.
     */
    public abstract boolean canAccessTo(Plate plate, int[] testedPosition);

    /**
     * Checks if the piece can move to the specified position on the chessboard.
     *
     * @param plate The chessboard.
     * @param endPos The destination position.
     * @return True if the move is valid, false otherwise.
     */
    public boolean canMoveTo(Plate plate, int[] endPos) {
        int[] currentPos = this.getPosition(plate);        
        return this.canAccessTo(plate, endPos) && !plate.doesMoveInvolveChess(currentPos, endPos);
    }

    /**
     * Abstract method to retrieve all possible moves for the piece on the chessboard.
     *
     * @param plate The chessboard.
     * @param currentPos The current position of the piece.
     * @return A list of possible moves represented as 2D arrays of coordinates.
     */
    public abstract ArrayList<int[][]> getPossibleMoves(Plate plate, int[] currentPos);
}
