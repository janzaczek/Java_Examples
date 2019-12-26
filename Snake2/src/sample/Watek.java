package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Watek extends Thread{
    static int minuty = 0;
    static int sekundy = 0;
    static boolean exit = false;

    Timeline time = new Timeline(new KeyFrame(Duration.millis(1000),ev ->{
        sekundy++;
        if(sekundy==60){
            minuty++;
            sekundy = 0;
        }
    }));

    @Override
    public void run(){
        while(!exit){
            time.play();
        }
        if(Rozgrywka.isGameOver)
            exit = true;
    }
}
