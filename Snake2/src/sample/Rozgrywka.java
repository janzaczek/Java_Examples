package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataInput;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Rozgrywka {

    static final long task_Uptade = 100; // ustala mi predkosc weza - im nizsze tym szybciej snake sie porusza
    //  opóznienie
    static final long task_Delay = 0;// - opóznienie miedzy task execution 

    static final int windowH = 20*NewGameController.s1;
    static final int windowW = 20*NewGameController.s2;
    static final int gridSize = 20;

    static GraphicsContext graphicsContext,gc;
    static Button startButton;
    static Snake snake;
    static Grid grid;
    static AnimationTimer animationTimer;
    static Timer timer;
    static TimerTask task; // to jest ten moj task ktory bedzie sie wykonywal
    static int licznik;
    static int licznikMinus;
    static double score;
//    static Timer czas;
//    static int second = 0;
    static Thread watek;
    static double watekScore;
    static double scoreWatka;
    static double scoreSizeMap = windowH+windowW/1000;

    static boolean isGameInProgress = false;
    static boolean isGameOver = false;
    static boolean isPaused = false;

    final static KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.F4,
            KeyCombination.SHIFT_ANY);


    public static void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(windowW, windowH);
        graphicsContext = canvas.getGraphicsContext2D();
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);


        grid = new Grid(windowW, windowH, gridSize);
        snake = new Snake(windowW, windowH, gridSize);
        snake.setHeadLocation(gridSize, gridSize);

        drawGrid();

        startButton = new Button("Start!");
        startButton.setMinWidth(100);
        startButton.setMinHeight(36);

        VBox vBox = new VBox();
        vBox.prefWidthProperty().bind(canvas.widthProperty());
        vBox.prefHeightProperty().bind(canvas.heightProperty());
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                isGameInProgress = true;
                isGameOver = false;
                startButton.setVisible(false);
                licznik = 0;
                licznikMinus = 0;
                watek = new Watek();
                watek.start();
                score = 0;
            }
        });

        root.getChildren().add(vBox);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.UP) {
                if(snake.getDirection()!=Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
            } else if (e.getCode() == KeyCode.DOWN) {
                if(snake.getDirection()!=Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
            } else if (e.getCode() == KeyCode.LEFT) {
                if(snake.getDirection()!= Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
            } else if (e.getCode() == KeyCode.RIGHT) {
                if(snake.getDirection()!=Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
            } else if (e.getCode() == KeyCode.P) {
                if (isPaused) {
                    task = createTimerTask();
                    timer = new Timer("Timer");
                    timer.scheduleAtFixedRate(task, task_Delay, task_Uptade);
                    isPaused = false;
                } else {
                    timer.cancel();
                    isPaused = true;
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (isGameInProgress) {
                    drawGrid();
                    drawSnake();
                    drawFood();
                } else if (isGameOver) {
                    animationTimer.stop();
                    scoreWatka = Watek.sekundy + Watek.minuty*60;
                    watekScore = scoreWatka;
                    scoreWatka /= 50;
                    showEndGame();
                    watek.stop();
//                    startButton.setVisible(true);
                    grid.reset();
//                    snake = new Snake(windowW, windowH, gridSize);
//                    snake.setHeadLocation(gridSize, gridSize);
                    System.out.println("Score Watka: " + watekScore);
                    try {
                        Stage newgame = new Stage();
                        Parent root = (Parent) FXMLLoader.load(getClass().getResource("FinalScene.fxml"));
                        newgame.setTitle("Mega Wonsz 9");
                        Scene scene = new Scene(root,400,400);
                        newgame.setScene(scene);

                        newgame.show();
                    }catch(IOException ex){
                        System.err.println(ex);
                    }
                }


            }
        };
        animationTimer.start();

        task = createTimerTask();
        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, task_Delay, task_Uptade);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, (EventHandler) event -> {
            if (keyComb1.match((KeyEvent) event)) {
                System.exit(0);
            }
        });
    }

    public static void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public static TimerTask createTimerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isGameInProgress) {
                    snake.snakeUpdate();

                    if (snake.kolizjaZeSciana()) {
                        endGame("Kolizja ze sciana");
                    } else if (snake.kolizjaZeSoba()) {
                        endGame("Kolizja ze soba");
                    }

                    boolean foundFood = grid.foundFood(snake);
                    if (foundFood) {
                        snake.addTail();
                        grid.addFood();
                        System.out.println("Snake length: " + snake.getTail().size());
                    }
                }
            }
        };
        return task;
    }

    public static void endGame(String reason) {
        timer.cancel();
        timer = null;
        isGameInProgress = false;
        isGameOver = true;
        System.out.println("Game over: " + reason);
    }

    public static void showEndGame() {
        score = ((double)snake.getTail().size() + 1 - licznikMinus+(scoreSizeMap/10*scoreWatka/10))/100;
        String gameOverText = "Game Over! Score: " + score;
        double textWidth = getTextWidth(gameOverText);

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText(gameOverText, (windowW / 2) - (textWidth / 2), windowH / 2 - 24);
    }

    public static void drawGrid() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, windowW, windowH);

        graphicsContext.setStroke(Color.LIMEGREEN);
        graphicsContext.setLineWidth(0.5);

        for (int x = 0; x < windowW; x += gridSize) {
            graphicsContext.strokeLine(x, 0, x, x + windowH);
        }

        for (int y = 0; y < windowH; y += gridSize) {
            graphicsContext.strokeLine(0, y, y + windowW, y);
        }
    }

    public static void drawSnake() {
        gc.setFill(Color.RED);
        gc.fillRect(snake.getHeadLocation().getX(), snake.getHeadLocation().getY(), snake.getBlockSize(),
                snake.getBlockSize());
        graphicsContext.setFill(Color.GREEN);
        for (Point tail : snake.getTail()) {
            graphicsContext.fillRect(tail.getX(), tail.getY(), snake.getBlockSize(),
                    snake.getBlockSize());
        }
    }

    public static void drawFood() {
        licznik++;
        graphicsContext.setFill(Color.MAGENTA);
        graphicsContext.fillRect(grid.getFood().getLocation().getX(), grid.getFood().getLocation().getY(),
                gridSize, gridSize);
    }

    public static double getTextWidth(String string) {
        Text text = new Text(string);
        new Scene(new Group(text));
        text.applyCss();
        return text.getLayoutBounds().getWidth();
    }

   
}
