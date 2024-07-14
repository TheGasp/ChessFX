package minmax;

import java.util.ArrayList;
import java.util.Random;

import enums.*;
import pieces.*;
import plate.*;
import tree.*;

/**
 * The Minmax class implements the Minimax algorithm with alpha-beta pruning
 * to find the best move for a given game board.
 */
public class Minmax {

	/**
	 * Evaluates the current game board and assigns a score.
	 * Higher scores favor white, while lower scores favor black.
	 *
	 * @param plate The game board
	 * @return The score for the current board position
	 */
	public static int evaluateBoard(Plate plate) {
	    // Define a malus value for a checkmate and castle
	    int MATE_MALUS = 9999;
	    int CASTLE_BONUS = 10;

	    // Initialize the score with a castle bonus
	    int score = CASTLE_BONUS * ((plate.hasCastled(ColourPiece.WHITE) ? 1 : 0) - (plate.hasCastled(ColourPiece.BLACK) ? 1 : 0));

	    // Get the current game status
	    GameStatus status = plate.getGameStatus();

	    // Adjust the score based on the game status
	    switch (status) {
	        case WHITE_MATE:
	            return -MATE_MALUS; // Penalize for white checkmate
	        case BLACK_MATE:
	            return MATE_MALUS; // Reward for black checkmate
	        case STALEMATE:
	            return 0; // Stalemate, no advantage for either side
	        default:
	            break;
	    }

	    // Adjust the score according to piece values and positions on the board
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            Piece current = plate.matrixPlate[i][j];
	            if (current != null) {
	                // Assign higher scores to pieces in the center of the board
	                int center_score = (i >= 2 && i <= 5 && j >= 2 && j <= 5) ? 2 : 1;
	                // Update the score based on piece color, center score, and piece value
	                score += (current.getColour() == ColourPiece.WHITE ? 1 : -1) * (center_score * current.getValue());
	            }
	        }
	    }

	    return score;
	}


    /**
     * Implements the Minmax algorithm with alpha-beta pruning.
     *
     * @param plate         The game board
     * @param colourToPlay  The color of the player to play
     * @param resultTree    The tree to fill with the best move
     * @param depth         The depth of the algorithm
     * @param alpha         The alpha value for alpha-beta pruning
     * @param beta          The beta value for alpha-beta pruning
     * @return The best score for the current position
     */
    private static int minmax(Plate plate, ColourPiece colourToPlay, Tree resultTree, int depth, int alpha, int beta) {
    	// Check if the current node in the tree already has children
        if (resultTree.nbChildren() > 0) {
            // Initialize the best score with the score of the first child
            int bestScore = resultTree.children.get(0).score;

            // Iterate through the children to find the best score
            for (int i = 1; i < resultTree.nbChildren(); i++) {
                int childScore = minmax(plate, colourToPlay == ColourPiece.WHITE ? ColourPiece.BLACK : ColourPiece.WHITE, resultTree.children.get(i), depth - 1, alpha, beta);
                bestScore = colourToPlay == ColourPiece.WHITE
                        ? Math.max(bestScore, childScore)
                        : Math.min(bestScore, childScore);

                
                // Update alpha or beta values based on the current player's turn
                if (colourToPlay == ColourPiece.WHITE) {
                    alpha = Math.max(alpha, bestScore);
                    if (beta < alpha)
                        break; // Alpha-beta pruning
                } else {
                    beta = Math.min(beta, bestScore);
                    if (beta < alpha)
                        break; // Alpha-beta pruning
                }
                
            }

            // Update the score of the current node in the tree
            resultTree.score = bestScore;

            return bestScore;
        }
        
        // If the current node has no children, generate possible moves
        ArrayList<int[][]> possibleMoves = AccessiblePositions.getAllPossibleMoves(plate, colourToPlay);        
        int possibleMovesLength = possibleMoves.size();

        // Check if the depth limit is reached
        if (depth == 0) {
            resultTree.score = evaluateBoard(plate) + possibleMovesLength / 5;
            return resultTree.score;
        }

        // Check for a endgame (no possible moves)
        if (possibleMovesLength == 0) {
            resultTree.score = evaluateBoard(plate);
            return resultTree.score;
        }
        
        int bestScore = colourToPlay == ColourPiece.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // Perform Minmax for the current player's turn
        if (colourToPlay == ColourPiece.WHITE) {
            int maxEval = Integer.MIN_VALUE;
            for (int[][] move : possibleMoves) {
                int[] startPos = move[0];
                int[] endPos = move[1];

                // Backup the current state of the board
                plate.stateBackuper.backupState(startPos, endPos);

                // Make the move and create a new child node in the tree
                plate.movePiece(startPos, endPos);
                Tree newChild = resultTree.addChild(move);

                // Recursively call Minmax for the opponent's turn
                int childScore = minmax(plate, ColourPiece.BLACK, newChild, depth - 1, alpha, beta);

                // Update the maximum evaluation value
                maxEval = Math.max(childScore, maxEval);

                // Restore the previous state and update alpha value
                plate.stateBackuper.restoreState();
                
                alpha = Math.max(alpha, maxEval);

                // Alpha-beta pruning
                if (beta < alpha)
                    break;
            }

            bestScore = maxEval;
            
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[][] move : possibleMoves) {
                int[] startPos = move[0];
                int[] endPos = move[1];
                
                plate.stateBackuper.backupState(startPos, endPos);
                
            	plate.movePiece(startPos, endPos);
                
                Tree newChild = resultTree.addChild(move);

                int childScore = minmax(plate, ColourPiece.WHITE, newChild, depth - 1, alpha, beta);

                minEval = Math.min(childScore, minEval);

                plate.stateBackuper.restoreState();
                
                alpha = Math.min(alpha, minEval);

                if (beta < alpha)
                    break;
            }

            bestScore = minEval;
        }

        // Update the score of the current node in the tree
        resultTree.score = bestScore;

        return bestScore;
    }

	
	/**
	 * Finds the best move for the current game board using the Minmax algorithm.
	 *
	 * @param plate            The game board
	 * @param evaluationDepth  The depth of evaluation for the Minmax algorithm
	 * @return The best move as a 2D array representing start and end positions
	 */
	public static int[][] getBestMove(Plate plate, int evaluationDepth) {	    
	    // Initialize a random object for making random decisions
	    Random random = new Random();
	    
	    // Create a new empty tree to store possible moves
	    Tree treeEvaluation = new Tree(new int[][] {{}, {}});
	    
	    int bestScore = minmax(plate, plate.getNextToPlay(), treeEvaluation, evaluationDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
	    
	    /*
	    ExecutorService executor = Executors.newSingleThreadExecutor();

        // Call function minmax with a max delay of 10 seconds
        try {
            Future<Integer> future = executor.submit(() -> {
            	// Perform Minmax algorithm to determine the best move and its score
                //return minmax(plate, plate.getNextToPlay(), treeEvaluation, evaluationDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            	return 1;
            });

            bestScore = future.get(10, TimeUnit.SECONDS);
            
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println(e);
            
        } finally {
            executor.shutdown();
            bestScore = treeEvaluation.score;
        }
        */

	    // Create a list to store the best moves
	    ArrayList<int[][]> bestMoves = new ArrayList<>();
	    int bestMovesSize = 0;

	    // Iterate through the children of the evaluation tree
	    for (int i = 0; i < treeEvaluation.nbChildren(); i++) {
	        // Check if the score of the child is equal to the best score or if it's a random selection
	        if (treeEvaluation.children.get(i).score == bestScore || random.nextDouble() > 1) {
	            // Add the move to the list of best moves
	            bestMoves.add(treeEvaluation.children.get(i).data);
	            bestMovesSize++;
	        }
	    }

	    // Generate a random index to select a move from the list of best moves
	    int randomIndex = Math.abs(random.nextInt()) % bestMovesSize;

	    // Return the randomly selected best move
	    return bestMoves.get(randomIndex);
	}

	
}
