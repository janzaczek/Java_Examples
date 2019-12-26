package sample;

import java.util.Random;

public class Grid {
    int height;
    int width;
    int pixelsPerSquare;
    Food food;
    int counter=0;
    int zmiennay;
    int zmiennax;

    public Grid(int width, int height, int pixelsPerSquare) {
        this.width = width;
        this.height = height;
        this.pixelsPerSquare = pixelsPerSquare;
        food = new Food(width / 2, height / 2);
    }

    public void reset() {
        food = new Food(width / 2, height / 2);
    }

    public boolean foundFood(Snake snake) {
        boolean isIntersected = false;

        if (snake.getHeadLocation().equals(food.getLocation())) {
            isIntersected = true;
        }

        return isIntersected;
    }

    public void addFood() {
        Random rand = new Random();
        int y = rand.nextInt(height);
        int x = rand.nextInt(width);
        if(counter==0){
            zmiennax=x;
            zmiennay=y;
            counter++;
        }

        if (counter == 1) {
            if(x==zmiennax&&y==zmiennay){
                 y = rand.nextInt(height);
                 x = rand.nextInt(width);
            }
            counter=0;
        }

        x = Math.round(x / pixelsPerSquare) * pixelsPerSquare;
        y = Math.round(x / pixelsPerSquare) * pixelsPerSquare;


        food = new Food(x, y);
        System.out.println(food.toString());
    }

    public Food getFood() {
        return food;
    }

}