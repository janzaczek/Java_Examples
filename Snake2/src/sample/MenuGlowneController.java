package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuGlowneController {
    @FXML
    Button button1,button2,button3;
    @FXML
    VBox box;
    @FXML
    Label label;

    public void newGame(){
        try {
            Stage newgame = new Stage();
            Parent root = (Parent)FXMLLoader.load(getClass().getResource("NewGame.fxml"));
            newgame.setTitle("Mega Wonsz 9");
            Scene scene = new Scene(root,1302,900);
            newgame.setScene(scene);
            newgame.show();

            Stage zamknij = (Stage)button1.getScene().getWindow();
            zamknij.close();


        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    public void closeGame(){
        Stage stage = (Stage)button3.getScene().getWindow();
        stage.close();
    }

    public void showScores(){
        Stage newgame = new Stage();
        try {
            Parent root =(Parent) FXMLLoader.load(getClass().getResource("HighScores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HighScores.pokazScores(newgame);
        } catch (Exception e) {
            System.err.println("Nie udalo sie");
        }

    }

}