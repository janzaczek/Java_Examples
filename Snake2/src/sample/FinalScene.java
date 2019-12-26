package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FinalScene {
    @FXML
    Label label;
    @FXML
    TextField pole;
    @FXML
    Button button;
    FileWriter fw;
    final static KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.F4,
            KeyCombination.SHIFT_ANY);


    public void zapisz(){
        try{
            fw = new FileWriter("D:\\studia\\2SEM\\GUI\\Cwiczenia\\Snake2/Wyniki.txt",true);
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(Rozgrywka.score + "---" + pole.getText()+"---Czas--"+Rozgrywka.watekScore + "---rozmiar mapy---" +
                    NewGameController.s1+"x"+NewGameController.s2);
            bw.newLine();
        }catch(IOException e) {
            e.printStackTrace();
        }
        try{
            bw.close();
            fw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        Stage zamknij = (Stage)button.getScene().getWindow();
        zamknij.close();
    }



}
