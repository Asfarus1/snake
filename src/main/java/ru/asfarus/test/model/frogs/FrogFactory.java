package ru.asfarus.test.model.frogs;

import ru.asfarus.test.model.Cell;
import ru.asfarus.test.model.Field;
import ru.asfarus.test.model.Snake;

import java.util.Random;

public class FrogFactory {
    private final Field field;
    private final int sleepDelayMs;
    private final Random rnd = new Random();
    private final float bestDirectionProbably;

    public FrogFactory(Field field, int sleepDelayMs, float bestDirectionProbably) {
        this.field = field;
        this.sleepDelayMs = sleepDelayMs;
        this.bestDirectionProbably = bestDirectionProbably;
    }

    public Frog generateRandomTypeFrog(Cell cell, Snake snake) {
        int frogTypeCount = 3;
        switch (rnd.nextInt(frogTypeCount)) {
            case 1:
                return createFrog(cell, snake);
            case 2:
                return createRedFrog(cell, snake);
            default:
                return createBlueFrog(cell, snake);
        }
    }

    private BlueFrog createBlueFrog(Cell cell, Snake snake) {
        return new BlueFrog(field, cell, sleepDelayMs, bestDirectionProbably, snake);
    }

    private RedFrog createRedFrog(Cell cell, Snake snake) {
        return new RedFrog(field, cell, sleepDelayMs, bestDirectionProbably, snake);
    }

    private Frog createFrog(Cell cell, Snake snake) {
        return new Frog(field, cell, sleepDelayMs, bestDirectionProbably, snake);
    }
}
