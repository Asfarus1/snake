package ru.asfarus.test.model.frogs;

import ru.asfarus.test.model.*;

import java.util.Random;

public class Frog extends CellFigure {
    private final Random rnd = new Random();
    private final float fixedBestDirectionProbably;
    private final Snake snake;

    Frog(Field field, Cell cell, int sleepDelayMs, float bestDirectionProbably, Snake snake) {
        super(field, cell, sleepDelayMs);
        this.fixedBestDirectionProbably = bestDirectionProbably - (1 - bestDirectionProbably) / (Directions.values().length - 1);
        this.snake = snake;
    }

    public void eat(Snake snake) {
        this.die();
        snake.setLen(snake.getLen() + getAddedLen());
        final Field field = getField();
        field.addScore(getAddedScore());
        field.createFrog();
    }

    protected int getAddedLen() {
        return 1;
    }

    protected int getAddedScore() {
        return 1;
    }

    private int chooseDirectionOrdinal(Directions bestDirection) {
        float rndFloat = rnd.nextFloat();
        if (rndFloat < fixedBestDirectionProbably) {
            return bestDirection.ordinal();
        } else {
            final Directions[] directions = Directions.values();
            return directions[rnd.nextInt(directions.length)].ordinal();
        }
    }

    @Override
    public void move() {
        final Cell cell = getCell();
        final Cell snakeCell = snake.getCell();
        final Directions snakeDirection = snake.getLastDirection();

        int newSnakeX = snakeCell.getX() + snakeDirection.getX();
        int newSnakeY = snakeCell.getY() + snakeDirection.getY();

        final int xDistance = cell.getX() - newSnakeX;
        final int yDistance = cell.getY() - newSnakeY;

        int x = cell.getX() + xDistance > 0 ? 1 : -1;
        int y = cell.getY() + yDistance > 0 ? 1 : -1;

        Directions direction;
        if (Math.sqrt(Math.pow(x - newSnakeX, 2) + Math.pow(cell.getY() - newSnakeY, 2)) > Math.sqrt(Math.pow(cell.getX() - newSnakeX, 2) + Math.pow(y - newSnakeY, 2))) {
            direction = xDistance > 0 ? Directions.RIGHT : Directions.LEFT;
        } else {
            direction = yDistance > 0 ? Directions.DOWN : Directions.TOP;
        }

        int firstOrdinal = chooseDirectionOrdinal(direction);

        final Directions[] directions = Directions.values();
        for (int i = 0; i < directions.length; i++) {
            direction = directions[(firstOrdinal + i) % directions.length];
            if (getField().moveToCell(cell.getX() + direction.getX(), cell.getY() + direction.getY(), this)) {
                break;
            }
        }
    }

    public boolean moveToCell(Cell cell) {
        if (cell.getFigure() == null) {
            getCell().setFigure(null);
            setCell(cell);
            return true;
        }
        return false;
    }
}
