package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class NewGameController {

    @FXML
    GridPane pane;
    @FXML
    Label opis,wiersze,kolumny,error;
    @FXML
    TextField pole1,pole2;
    @FXML
    Button button1;

    static Stage newgame;
    static Parent root;
    static int s1,s2;

    public void snake() throws Exception
    {
        s1 = Integer.parseInt(pole1.getText());
        s2 = Integer.parseInt(pole2.getText());
        if(s1<=8||s2<=8){
            error.setText("Can't make so small board");
            throw new Exception("Cant make so small board");
        }
        try {
            newgame = new Stage();
            root =(Parent) FXMLLoader.load(getClass().getResource("Rozgrywka.fxml"));
            try {
                Rozgrywka.start(newgame);
            } catch (Exception e) {
                System.err.println("Nie udalo sie");
            }
            Stage zamknij = (Stage)button1.getScene().getWindow();
            zamknij.close();

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}
