package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HighScores{

    static ArrayList<String> lista;

    final static KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.F4,
            KeyCombination.SHIFT_ANY);

    public static void pokazScores(Stage primaryStage){
        lista = new ArrayList<>();
        primaryStage.setTitle("Scores");
        HBox hbox = new HBox();
        Label label = new Label();
        dodanieDoListy();
        show();
        for(int i=0; i<lista.size(); i++) {
            label.setText(label.getText() + System.lineSeparator() + lista.get(i));
        }

        hbox.getChildren().add(label);

        ScrollPane scrollPane = new ScrollPane(hbox);

        BorderPane root = new BorderPane(scrollPane);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root,400,400);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, (EventHandler) event -> {
            if (keyComb1.match((KeyEvent) event)) {
                System.exit(0);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void dodanieDoListy(){
        try {
            FileReader f = new FileReader("D:\\studia\\2SEM\\GUI\\Cwiczenia\\Snake2/Wyniki.txt");
                StringBuffer sb = new StringBuffer();
            while(f.ready())

                {
                    char c = (char) f.read();
                    if (c == '\n') {
                        lista.add(sb.toString());
                        sb = new StringBuffer();
                    } else {
                        sb.append(c);
                    }
                }
            if(sb.length()>0)

                {
                    lista.add(sb.toString());
                }
        }catch(IOException ex){
            ex.getMessage();
        }
    }

    public static void show(){
        Collections.sort(lista,Collections.reverseOrder());
        for(int i=0; i<lista.size(); i++){
            System.out.println(lista.get(i));
        }
    }


}
