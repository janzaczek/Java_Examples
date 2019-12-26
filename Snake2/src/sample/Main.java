package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    Parent root;
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.F4,
            KeyCombination.SHIFT_ANY);

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("MenuGlowne.fxml"));
        primaryStage.setTitle("Snake the Game");
        Scene scene = new Scene(root,1302,900);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.addEventHandler(KeyEvent.KEY_RELEASED, (EventHandler) event -> {
            if (keyComb1.match((KeyEvent) event)) {
                System.exit(0);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
