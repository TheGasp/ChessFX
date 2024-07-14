package plate;

/**
 * Represents the chessboard and game state.
 *
 */

import pieces.*;
import enums.*;

public class Plate {

    // Matrix to store the pieces on the chessboard.
    public Piece[][] matrixPlate;

    // Keeps track of the color of the player whose turn is next.
    private ColourPiece nextToPlay = ColourPiece.WHITE;

    // Arrays to store the positions of the black and white kings.
    private int[] blackKingPosition;
    private int[] whiteKingPosition;

    // Flags indicating whether white and black have castled.
    private boolean whiteCastled = false;
    private boolean blackCastled = false;

    // Total number of pieces on the board.
    private int nbPiece = 32;

    // Represents the last piece that was played.
    private Piece lastPlayed = null;

    // Represents the last move coords
    private int[][] lastPlayedMove;
    
    // Keeps track of the type of the last move made on the board.
    protected MoveType lastMoveType = MoveType.NORMAL_MOVE;

    // Object responsible for backing up the state of the chessboard.
    public StateBackuper stateBackuper = new StateBackuper(this);

    public Rook rook;
    /**
     * Constructor to initialize the chessboard with pieces.
     */
    public Plate(){        
        this.matrixPlate = new Piece[8][8];

        // Initialize white pieces
        matrixPlate[0][0] = new Rook(ColourPiece.WHITE); 
        matrixPlate[1][0] = new Knight(ColourPiece.WHITE);
        matrixPlate[2][0] = new Bishop(ColourPiece.WHITE);
        matrixPlate[3][0] = new Queen(ColourPiece.WHITE);
        matrixPlate[4][0] = new King(ColourPiece.WHITE);
        this.whiteKingPosition = new int[]{4,0};
        matrixPlate[5][0] = new Bishop(ColourPiece.WHITE);
        matrixPlate[6][0] = new Knight(ColourPiece.WHITE);
        matrixPlate[7][0] = new Rook(ColourPiece.WHITE);

        // Initialize white pawns
        for(int i = 0; i <= 7; i++)
            matrixPlate[i][1] = new Pawn(ColourPiece.WHITE);

        // Initialize black pieces
        matrixPlate[0][7] = new Rook(ColourPiece.BLACK);
        this.rook = (Rook) matrixPlate[0][7];
        matrixPlate[1][7] = new Knight(ColourPiece.BLACK);
        matrixPlate[2][7] = new Bishop(ColourPiece.BLACK);
        matrixPlate[3][7] = new Queen(ColourPiece.BLACK);
        matrixPlate[4][7] = new King(ColourPiece.BLACK);
        this.blackKingPosition = new int[]{4,7};
        matrixPlate[5][7] = new Bishop(ColourPiece.BLACK);
        matrixPlate[6][7] = new Knight(ColourPiece.BLACK);
        matrixPlate[7][7] = new Rook(ColourPiece.BLACK);

        // Initialize black pawns
        for(int i = 0; i <= 7; i++)
          matrixPlate[i][6] = new Pawn(ColourPiece.BLACK);
    }
    
    
    public Plate(Plate originalPlate) {
        // Initialize the matrixPlate and other fields with the same dimensions
        this.matrixPlate = new Piece[8][8];

        // Copy pieces from the original plate to the new plate
        for (int i = 0; i < originalPlate.matrixPlate.length; i++) {
            for (int j = 0; j < originalPlate.matrixPlate[i].length; j++) {
                if (originalPlate.matrixPlate[i][j] != null) {
                    this.matrixPlate[i][j] = originalPlate.matrixPlate[i][j]; // Assuming your Piece class implements cloneable
                }
            }
        }

        // Copy other fields
        this.nextToPlay = originalPlate.nextToPlay;
        this.blackKingPosition = originalPlate.blackKingPosition.clone();
        this.whiteKingPosition = originalPlate.whiteKingPosition.clone();
        this.whiteCastled = originalPlate.whiteCastled;
        this.blackCastled = originalPlate.blackCastled;
        this.nbPiece = originalPlate.nbPiece;
        // Copy other fields as needed

        // Note: You may need to adjust this based on the implementation of your Piece class and other fields.
    }

    /**
     * Checks if a piece can move to the specified position.
     * @param piece The piece to move.
     * @param positionToGo The target position.
     * @return True if the piece can move, false otherwise.
     */
    public boolean canMovePiece(Piece piece, int[] positionToGo){
        return piece.canMoveTo(this, positionToGo);
    }

    /**
     * Moves a piece from the current position to the destination position.
     * @param currentPos The current position of the piece.
     * @param destPos The destination position of the piece.
     * @return The taken piece (null if no piece at destPos).
     */
    public Piece movePiece(int[] currentPos, int[] destPos){
    	//System.out.println("papa1");
        int currentx = currentPos[0], currenty = currentPos[1];
        int tox = destPos[0], toy = destPos[1];
        
        if(currentx == tox && currenty == toy)
        	return null;

        // Return the taken piece (null if no piece at destPos)
        Piece taken = this.matrixPlate[tox][toy];

        this.matrixPlate[tox][toy] = this.matrixPlate[currentx][currenty];
        this.matrixPlate[currentx][currenty] = null;
        this.lastMoveType = MoveType.NORMAL_MOVE;
    	
        // Update king position if needed
        if(this.matrixPlate[tox][toy].getType() == PieceType.KING){
            // If it was a castle move, move the concerned rook
            if(currentx == 4 && tox == 2){
                this.matrixPlate[3][currenty] = this.matrixPlate[0][currenty];
                this.matrixPlate[3][currenty].firstMove = false;
                this.matrixPlate[0][currenty] = null;
                this.lastMoveType = MoveType.BIG_CASTLE;
            }
            else if(currentx == 4 && tox == 6){
                this.matrixPlate[5][currenty] = this.matrixPlate[7][currenty];
                this.matrixPlate[5][currenty].firstMove = false;
                this.matrixPlate[7][currenty] = null;
                this.lastMoveType = MoveType.LITTLE_CASTLE;
            }
            if(this.matrixPlate[tox][toy].getColour() == ColourPiece.WHITE){
                this.whiteKingPosition[0] = tox;
                this.whiteKingPosition[1] = toy;
            }
            else{
                this.blackKingPosition[0] = tox;
                this.blackKingPosition[1] = toy;
            }
        }

        // Promote pawn if needed
        else if(this.matrixPlate[tox][toy].getType() == PieceType.PAWN){
            if(this.matrixPlate[tox][toy].getColour() == ColourPiece.WHITE && toy == 7){
                this.matrixPlate[tox][toy] = new Queen(ColourPiece.WHITE);
                this.lastMoveType = MoveType.PAWN_PROMOTION;
            }
            else if(this.matrixPlate[tox][toy].getColour() == ColourPiece.BLACK && toy == 0){
                this.matrixPlate[tox][toy] = new Queen(ColourPiece.BLACK);
                this.lastMoveType = MoveType.PAWN_PROMOTION;
            }
        }

        return taken;
    }

    /**
     * Checks if a move involves putting the king in check or mate.
     *
     * @param currentPos The current position of the piece.
     * @param destPos The destination position of the piece.
     * @return True if the move involves putting the king in check or mate, false otherwise.
     */
    public boolean doesMoveInvolveChess(int[] currentPos, int[] destPos) {
        // Backup the current game state
        this.stateBackuper.backupState(currentPos, destPos);
        
        // Try to move the piece and check if the king is in check
        this.movePiece(currentPos, destPos);
        
        boolean isChess = this.isKingInChess(this.matrixPlate[destPos[0]][destPos[1]].getColour());

        // Restore the game state to its original state
        this.stateBackuper.restoreState();

        return isChess;
    }


    /**
     * Allows a player to move a piece on the chessboard.
     *
     * @param current_pos The current position of the piece.
     * @param dest_pos The destination position of the piece.
     * @return True if the move is successful, false otherwise.
     */
    public boolean playerMovePiece(int[] current_pos, int[] dest_pos) {
        // Check if there is a piece at the current position
        if (this.matrixPlate[current_pos[0]][current_pos[1]] == null) {
            System.out.printf("No piece at the given position.\n");
            return false;
        }

        // Check if it's the player's turn to move
        if (this.matrixPlate[current_pos[0]][current_pos[1]].getColour() != this.nextToPlay) {
            System.out.printf("playerMovePiece: It's not your turn to play.\n");
            return false;
        }

        // Check if the piece can access the destination position
        if (!this.matrixPlate[current_pos[0]][current_pos[1]].canMoveTo(this, dest_pos)) {
            return false;
        }

        // Move the piece and capture the taken piece
        Piece taken = movePiece(current_pos, dest_pos);
        
        this.lastPlayedMove = new int[][] {current_pos, dest_pos};

        // Update castling status if the move involves castling
        if (this.getLastMoveType() == MoveType.LITTLE_CASTLE || this.getLastMoveType() == MoveType.BIG_CASTLE) {
            if (this.nextToPlay == ColourPiece.WHITE)
                this.whiteCastled = true;
            else
                this.blackCastled = true;
        }

        // Update the piece count if a piece is taken
        if (taken != null)
            this.nbPiece--;

        // Update game state and switch turns
        this.lastPlayed = this.matrixPlate[dest_pos[0]][dest_pos[1]];
        this.matrixPlate[dest_pos[0]][dest_pos[1]].firstMove = false;
        this.nextToPlay = this.nextToPlay == ColourPiece.WHITE ? ColourPiece.BLACK : ColourPiece.WHITE;

        return true;
    }


    /**
     * Gets the current game status.
     *
     * @return The current game status.
     */
    public GameStatus getGameStatus() {
        // Check for a stalemate if only two pieces are left
        if (this.nbPiece == 2)
            return GameStatus.STALEMATE;

        // Check the status for the white side
        boolean whiteChess = isKingInChess(ColourPiece.WHITE);
        boolean whiteMate = isKingMate(ColourPiece.WHITE);

        if (whiteChess) {
            // Check if it's a checkmate or just a check for the white side
            if (whiteMate)
                return GameStatus.WHITE_MATE;
            return GameStatus.WHITE_CHESS;
        }

        // Check for stalemate if the white side is not in check
        if (whiteMate)
            return GameStatus.STALEMATE;

        // Check the status for the black side
        boolean blackChess = isKingInChess(ColourPiece.BLACK);
        boolean blackMate = isKingMate(ColourPiece.BLACK);

        if (blackChess) {
            // Check if it's a checkmate or just a check for the black side
            if (blackMate)
                return GameStatus.BLACK_MATE;
            return GameStatus.BLACK_CHESS;
        }

        // Check for stalemate if the black side is not in check
        if (blackMate)
            return GameStatus.STALEMATE;

        // No specific status, game continues normally
        return GameStatus.RAS;
    }
    
    public String gameStatusToString(GameStatus gamestatus) {
    	switch(gamestatus) {
			case BLACK_CHESS:
				return "Black king is in chess";
			case BLACK_MATE:
				return "Black king is Mate, white wins !";
			case STALEMATE:
				return "Stalemate, game done.";
			case WHITE_CHESS:
				return "White king is in chess";
			case WHITE_MATE:
				return "White king is Mate, black wins !";
			default:
				return "";
    	}
    }

    /**
     * Gets the position of a piece on the chessboard.
     * @param piece The piece.
     * @return An array containing the two coordinates.
     */
    public int[] getPositionPiece(Piece piece) {
        for (int i = 0; i < matrixPlate.length; i++) {
            for (int j = 0; j < matrixPlate[i].length; j++) {
                if (matrixPlate[i][j] == piece) {
                    return new int[]{i, j};
                }
            }
        }
        return null; // Returns null if the object is not found
    }

    /**
     * Gets the position of the king of a specific color.
     * @param colour The color of the king.
     * @return An array containing the king's position.
     */
    public int[] getKingPos(ColourPiece colour) {
        return colour == ColourPiece.WHITE ? this.whiteKingPosition : this.blackKingPosition;
    }

    /**
     * Checks if the king of a specific color is in check.
     * @param colour The color of the king.
     * @return True if the king is in check, false otherwise.
     */
    public boolean isKingInChess(ColourPiece colour) {
        int[] kingPos = getKingPos(colour);
        return ((King) this.matrixPlate[kingPos[0]][kingPos[1]]).isInChess(this);
    }

    /**
     * Checks if the king of a specific color is in checkmate.
     * @param colour The color of the king.
     * @return True if the king is in checkmate, false otherwise.
     */
    public boolean isKingMate(ColourPiece colour) {
        int[] kingPos = getKingPos(colour);
        return ((King) this.matrixPlate[kingPos[0]][kingPos[1]]).isMate(this);
    }

    /**
     * Checks if a player has castled.
     * @param colour The color of the player.
     * @return True if the player has castled, false otherwise.
     */
    public boolean hasCastled(ColourPiece colour) {
        return colour == ColourPiece.WHITE ? this.whiteCastled : this.blackCastled;
    }

    /**
     * Gets the type of the last move performed.
     * @return The type of the last move.
     */
    public MoveType getLastMoveType() {
        return lastMoveType;
    }

    /**
     * Gets the color of the player who is next to play.
     * @return The color of the player who is next to play.
     */
    public ColourPiece getNextToPlay() {
        return this.nextToPlay;
    }
    
    public int[][] getLastPlayedMove() {
    	return this.lastPlayedMove;
    }
    
    public Piece getLastPieceMoved() {
    	return this.lastPlayed;
    }
    
    public int getNbPiecesLeft() {
    	return this.nbPiece;
    }
}
