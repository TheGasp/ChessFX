/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
*/
package pieces;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.ChessGameMenu;


public class Chess extends Application{
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage){
    	ChessGameMenu menu = new ChessGameMenu();
        menu.start(primaryStage);
    	
        //Menu menu = new Menu();
        //menu.start(primaryStage);
    }
}


        /*
//Créer le plateau
        Plate plateau = new Plate();

        // Initialiser la grille
        chessBoard = createChessBoard(plateau);

        // Créer la scène
        Scene scene = new Scene(chessBoard, 800, 800);

        // Configurer la fenêtre
        primaryStage.setTitle("Chess Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}*/

