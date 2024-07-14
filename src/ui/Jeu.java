package ui;

import enums.ColourPiece;
import enums.GameStatus;
import enums.PieceType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pieces.Piece;
import plate.Plate;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main class representing the chess Jeu UI.
 */
public class Jeu extends Application {
	
	private Stage primaryStage;

    // Constants
    private static final int BOARD_SIZE = 8;
    private int[] FIRST_CLICK = new int[]{9, 9}; // Initial values for first click coordinates

    // UI components
    private GridPane chessBoard;
    private TextArea infoTextArea;

    // Jeu details
    private int nbPlayers;
    private ColourPiece colour;
    String movesList;
    GameStatus gameState;
    
    boolean alertWhileChess = true;
    
    /**
     * Constructor for the Jeu class.
     * @param nbPlayers Number of players (1 or 2)
     * @param colour Color of the player (WHITE or BLACK)
     */
    Jeu(int nbPlayers, ColourPiece colour) {
        this.nbPlayers = nbPlayers;
        this.colour = colour;
        this.movesList = "";
    }

    /**
     * Entry point for launching the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	
    	// Initialize Jeu board
        Plate board = new Plate();
        chessBoard = createChessBoard(board);

        // Initialize info text area
        infoTextArea = new TextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setWrapText(true);
        // Set VBox to stretch the TextArea in height
        VBox.setVgrow(infoTextArea, javafx.scene.layout.Priority.ALWAYS);
        
        // Initialize menu button
        Button goMenu = new Button("Retour menu");
        goMenu.setFont(new Font(16));
        goMenu.setMaxWidth(Double.MAX_VALUE);
        goMenu.setOnAction(e -> toMenu());

        // Create layout with two VBoxes side by side
        HBox root = new HBox(20);
        root.setPadding(new Insets(10));

        // First VBox for chessboard
        VBox chessBoardVBox = new VBox(10);
        chessBoardVBox.getChildren().add(chessBoard);

        // Second VBox for information
        VBox infoVBox = new VBox(10);
        infoVBox.getChildren().addAll(infoTextArea, goMenu);

        // Set a constraint to limit the size of infoVBox to half of chessBoardVBox
        infoVBox.setMaxWidth(240);

        // Add both VBoxes to the HBox
        root.getChildren().addAll(chessBoardVBox, infoVBox);

        // Set up the scene
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
 
        
        // If playing against the AI and IA is WHITE, IA starts to play
        if (this.nbPlayers == 1 && colour == ColourPiece.BLACK) {
            Task<Object> makeIAPlay = new Task<>() {
                @Override
                protected Void call() {
                    int[][] IAsMove = minmax.Minmax.getBestMove(board, 3); // AI makes a move
                    board.playerMovePiece(IAsMove[0], IAsMove[1]); // Apply the AI's move
                    return null;
                }
            };

            makeIAPlay.setOnSucceeded(event -> {
                Platform.runLater(() -> updateChessBoard(board)); // Update the UI after AI's move
            });
            
            new Thread(makeIAPlay).start();
        }
        else
        	updateChessBoard(board);
    }

    /**
     * Creates a label representing a chessboard square.
     * @param board The Jeu board
     * @param matrixPlate 2D array representing the chessboard
     * @param x X-coordinate of the square
     * @param y Y-coordinate of the square
     * @return The created label
     */
    private Label createSquare(Plate board, Piece[][] matrixPlate, int x, int y) {
        Label square = new Label();
        square.setMinSize(60, 60);
        square.setFont(new Font(50));

        Piece piece = matrixPlate[x][y];

        // Set the background color based on the square's position
        if ((x + y) % 2 == 0) {
            square.setStyle("-fx-alignment: center; -fx-background-color: moccasin;");
        } else {
            square.setStyle("-fx-alignment: center; -fx-background-color: saddlebrown;");
        }
        
        if(alertWhileChess && piece != null && piece.getType() == PieceType.KING) {
	        if(this.gameState == GameStatus.BLACK_CHESS
	        			&& piece.getColour() == ColourPiece.BLACK)
	            square.setStyle("-fx-alignment: center; -fx-background-color: red;");
	        else if(this.gameState == GameStatus.WHITE_CHESS
	    			&& piece.getColour() == ColourPiece.WHITE)
	        	square.setStyle("-fx-alignment: center; -fx-background-color: red;");
        }

        // Display the piece symbol if the square is not empty
        if (piece != null) {
            if (piece.getColour() == ColourPiece.WHITE) {
                square.setTextFill(Color.WHITE);
            } else {
                square.setTextFill(Color.BLACK);
            }
            square.setText(getPieceSymbol(piece.getType()));
        }

        // Handle square click event
        square.setOnMouseClicked(event -> handleSquareClick(board, matrixPlate, x, y, square));

        return square;
    }

    /**
     * Creates the chessboard UI.
     * @param board The Jeu board
     * @return The created GridPane representing the chessboard
     */
    private GridPane createChessBoard(Plate board) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        Piece[][] matrixPlate = board.matrixPlate;

     // Repopulate the gridPane with updated squares
        if(this.nbPlayers == 2 || this.colour == ColourPiece.WHITE) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    Label square = createSquare(board, matrixPlate, x, BOARD_SIZE-1-y);
                    gridPane.add(square, x, y);
                }
            }
        }else {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    Label square = createSquare(board, matrixPlate, x, y);
                    gridPane.add(square, x, y);
                }
            }
        }

        return gridPane;
    }

    /**
     * Returns the Unicode symbol for a given piece type.
     * @param type The type of chess piece
     * @return The Unicode symbol representing the piece
     */
    private String getPieceSymbol(PieceType type) {
        switch (type) {
            case PAWN: return "\u265F";
            case ROOK: return "\u265C";
            case KNIGHT: return "\u265E";
            case BISHOP: return "\u265D";
            case QUEEN: return "\u265B";
            case KING: return "\u265A";
            default: return "";
        }
    }

    /**
     * Handles the click event on a chessboard square.
     * @param board The Jeu board
     * @param matrixPlate 2D array representing the chessboard
     * @param x X-coordinate of the clicked square
     * @param y Y-coordinate of the clicked square
     * @param square The clicked Label representing the square
     */
    private void handleSquareClick(Plate board, Piece[][] matrixPlate, int x, int y, Label square) {

        // If it's the first click, highlight the square if it contains a piece
        if (FIRST_CLICK[0] == 9) {
        	// Check if player click on a right colour piece (the playing one)
            if (matrixPlate[x][y] != null && matrixPlate[x][y].getColour() == board.getNextToPlay()) {
            	// If player click on adverse piece while IA is playing => return
            	if(this.nbPlayers == 1 && matrixPlate[x][y].getColour() != this.colour)
            		return;
                FIRST_CLICK = new int[]{x, y};
                square.setStyle("-fx-alignment: center; -fx-background-color: orange;");
            }
        } else {
            // If it's the second click, attempt to move the selected piece
            int x2 = FIRST_CLICK[0];
            int y2 = FIRST_CLICK[1];
    	    
            boolean movedPiece = board.playerMovePiece(new int[]{x2, y2}, new int[]{x, y});
            FIRST_CLICK = new int[]{9, 9}; // Reset first click coordinates
            
            // Update the chessboard UI
            updateChessBoard(board);

            // If playing against the AI and the player successfully moved a piece, let the AI make a move
            if(movedPiece) {
	            if (this.nbPlayers == 1) {
	            	Task<Object> makeIAPlay = new Task<>() {
	                    @Override
	                    protected Void call() {
	                        int[][] IAsMove = minmax.Minmax.getBestMove(board, board.getNbPiecesLeft() > 8 ? 3 : 4); // AI makes a move
	                        board.playerMovePiece(IAsMove[0], IAsMove[1]); // Apply the AI's move
	                    	
	                        return null;
	                    }
	                };
	
	                makeIAPlay.setOnSucceeded(event -> {
	                    Platform.runLater(() -> updateChessBoard(board)); // Update the UI after AI's move
	                });
	                
	                new Thread(makeIAPlay).start();
	            }
            }
        }
    }

    /**
     * Updates the chessboard UI based on the current Jeu state.
     * @param board The Jeu board
     */
    private void updateChessBoard(Plate board) {
        this.gameState = board.getGameStatus();
        
        chessBoard.getChildren().clear();

        Piece[][] matrixPlate = board.matrixPlate;

        // Repopulate the gridPane with updated squares
        if(this.nbPlayers == 2 || this.colour == ColourPiece.WHITE) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    Label square = createSquare(board, matrixPlate, x, BOARD_SIZE-1-y);
                    chessBoard.add(square, x, y);
                }
            }
        }else {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    Label square = createSquare(board, matrixPlate, x, y);
                    chessBoard.add(square, x, y);
                }
            }
        }
        
        //Display infos
        String title = "        ##### Infos de jeu ######";
        String colourToPlay = board.getNextToPlay() == ColourPiece.WHITE ? "Au tour des Blancs" : "Au tour des Noirs";
        
        // Get last played move as string
        if(board.getLastPlayedMove() != null) {
            int x1 = board.getLastPlayedMove()[0][0], y1 = board.getLastPlayedMove()[0][1];
            int x2 = board.getLastPlayedMove()[1][0], y2 = board.getLastPlayedMove()[1][1];
            String move = "  " + (char) (((int) getPieceSymbol(board.getLastPieceMoved().getType()).charAt(0)) - (board.getNextToPlay() == ColourPiece.BLACK ? 6 : 0))
            			+ Character.toString(x1+'A')+ (y1+1)
            			+ " -> "
            			+ Character.toString(x2+'A') + (y2+1);
            if(this.movesList.length() < 11 || !move.equals(this.movesList.subSequence(0, 11)))
            	this.movesList = move + "\n" + this.movesList;

        }
        
        // get game state and colour to play (which is not displayed if mate or stalemate)
        String state = "";
        
        // If 2 player Game
        if(this.nbPlayers == 2) {
        	if(gameState != GameStatus.BLACK_MATE && gameState != GameStatus.WHITE_MATE & gameState != GameStatus.STALEMATE)
        		state = colourToPlay;
        }
        else {
        	if(board.getNextToPlay() == this.colour)
        		state = "A ton tour de jouer";
        	else
        		state = "L'IA joue...";
        }
        
        state = (board.gameStatusToString(gameState) == "" ? "" : board.gameStatusToString(gameState)+"\n\n") + state;
        
        String toDisplay = title + "\n\n"
        			+ state
        			+ "\n\nCoups précédents :\n" + this.movesList;
        
        infoTextArea.clear();
        infoTextArea.appendText(toDisplay);
        
        if(gameState == GameStatus.BLACK_MATE || gameState == GameStatus.WHITE_MATE || gameState == GameStatus.STALEMATE) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Jeu d'échec");
            
            if(gameState == GameStatus.STALEMATE) {
                alert.setHeaderText("Pat");
                alert.setContentText("Le jeu s'est terminé sur un Pat, bravo.");
            }
            else {
                alert.setHeaderText("Echec... et mat !");
                if(gameState == GameStatus.BLACK_MATE)
                	alert.setContentText("Victoire des Blancs, bravo !");
                else
                	alert.setContentText("Victoire des Noirs, Bravo !");
            }

            // Afficher et attendre la réponse de l'utilisateur
            alert.showAndWait();
        }
    }
    
    private void toMenu() {
        // Hide the menu
        primaryStage.hide();

        // Start the game with a friend
        ChessGameMenu menu = new ChessGameMenu(); // Assuming 2 players and starting with WHITE
        menu.start(primaryStage);
    }
}
