package sample;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    Direction direction;
    Point headLocation = new Point(0, 0);
    List<Point> tail = new ArrayList<>();
    int height;
    int width;
    int blockSize;
    boolean kolizjaZeSciana = false;

    public Snake(int width, int height, int blockSize) {
        this.width = width;
        this.height = height;
        this.blockSize = blockSize;
        this.direction = Direction.RIGHT;
    }

    public void snakeUpdate() {

        if (tail.size() > 0) {
            tail.remove(tail.size() - 1);
            tail.add(0, new Point(headLocation.getX(), headLocation.getY()));
        }

        switch (direction) {
            case UP:
                headLocation.setY(headLocation.getY() - blockSize);
                if (headLocation.getY() < 0) {
                    kolizjaZeSciana = true;
                    headLocation.setY(0);
                }
                break;

            case DOWN:
                headLocation.setY(headLocation.getY() + blockSize);
                if (headLocation.getY() >= height) {
                    kolizjaZeSciana = true;
                    headLocation.setY(height - blockSize);
                }
                break;

            case LEFT:
                headLocation.setX(headLocation.getX() - blockSize);
                if (headLocation.getX() < 0) {
                    kolizjaZeSciana = true;
                    headLocation.setX(0);
                }
                break;

            case RIGHT:
                headLocation.setX(headLocation.getX() + blockSize);
                if (headLocation.getX() >= width) {
                    kolizjaZeSciana = true;
                    headLocation.setX(width - blockSize);
                }
                break;

            default:
                break;
        }
    }

    public boolean kolizjaZeSciana() {
        return kolizjaZeSciana;
    }

    public boolean kolizjaZeSoba() {
        boolean isCollision = false;

        for (Point tailSegment : tail) {
            if (headLocation.equals(tailSegment)) {
                isCollision = true;
                break;
            }
        }

        return isCollision;
    }

    public void addTail() {
        tail.add(0, new Point(headLocation.getX(), headLocation.getY()));
        System.out.println("Dodałem ogon");
    }

    public void setDirection(Direction myDirection) {
        this.direction = myDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setHeadLocation(int x, int y) {
        headLocation.setX(x);
        headLocation.setY(y);
    }

    public Point getHeadLocation() {
        return headLocation;
    }

    public List<Point> getTail() {
        return tail;
    }

}