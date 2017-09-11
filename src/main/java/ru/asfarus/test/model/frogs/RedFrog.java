package ru.asfarus.test.model.frogs;

import ru.asfarus.test.model.Cell;
import ru.asfarus.test.model.Field;
import ru.asfarus.test.model.Snake;

public class RedFrog extends Frog {
    RedFrog(Field field, Cell cell, int sleepDelayMs, float bestDirectionProbably, Snake snake) {
        super(field, cell, sleepDelayMs, bestDirectionProbably, snake);
    }

    @Override
    protected int getAddedLen() {
        return -1;
    }

    @Override
    protected int getAddedScore() {
        return 2;
    }
}
