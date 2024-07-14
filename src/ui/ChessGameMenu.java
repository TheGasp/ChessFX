package ui;

import enums.ColourPiece;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChessGameMenu extends Application {

    private Stage primaryStage;
    private VBox mainLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Jeu d'échec");

        // Initialize the main layout
        initMainLayout();

        // Display the main scene
        Scene scene = new Scene(mainLayout, 300, 200);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void initMainLayout() {
        // Title in black
        Text title = new Text("Jeu d'échec");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.BLACK);

        // Buttons
        Button playAgainstIAButton = new Button("  Jouer contre l'IA  ");
        Button playWithFriendButton = new Button("Jouer avec un ami");
        
        // Buttons font
        playAgainstIAButton.setFont(new Font(16));
        playWithFriendButton.setFont(new Font(16));
        
        playAgainstIAButton.setMinWidth(playWithFriendButton.getWidth());

        // Button actions
        playAgainstIAButton.setOnAction(e -> showChessGameIASelection());

        playWithFriendButton.setOnAction(e -> startGameWithFriend());

        // Layout
        mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(title, playAgainstIAButton, playWithFriendButton);
    }

    private void showChessGameIASelection() {
        // Clear the main layout
        mainLayout.getChildren().clear();

        // Title in black
        Text title = new Text("Jeu d'échec");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.BLACK);

        // Buttons for IA selection
        Button playAsWhiteButton = new Button("Jouer les Blancs");
        Button playAsBlackButton = new Button("Jouer les Noirs");
        
        // Buttons font
        playAsWhiteButton.setFont(new Font(16));
        playAsBlackButton.setFont(new Font(16));

        // Button actions
        playAsWhiteButton.setOnAction(e -> startGameAgainstIAasWhite());

        playAsBlackButton.setOnAction(e -> startGameAgainstIAasBlack());

        // Layout
        mainLayout.getChildren().addAll(title, playAsWhiteButton, playAsBlackButton);
    }
    

    private void startGameWithFriend() {
        // Hide the menu
        primaryStage.hide();

        // Start the game with a friend
        Jeu jeu = new Jeu(2, ColourPiece.WHITE); // Assuming 2 players and starting with WHITE
        jeu.start(primaryStage);
    }
    
    private void startGameAgainstIAasWhite() {
        // Hide the menu
        primaryStage.hide();

        // Start the game with a friend
        Jeu jeu = new Jeu(1, ColourPiece.WHITE); // Assuming 2 players and starting with WHITE
        jeu.start(primaryStage);
    }
    
    private void startGameAgainstIAasBlack() {
        // Hide the menu
        primaryStage.hide();

        // Start the game with a friend
        Jeu jeu = new Jeu(1, ColourPiece.BLACK); // Assuming 2 players and starting with WHITE
        jeu.start(primaryStage);
    }
}
