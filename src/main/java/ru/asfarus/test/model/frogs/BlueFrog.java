package ru.asfarus.test.model.frogs;

import ru.asfarus.test.model.Cell;
import ru.asfarus.test.model.Field;
import ru.asfarus.test.model.Snake;

public class BlueFrog extends Frog {
    BlueFrog(Field field, Cell cell, int sleepDelayMs, float bestDirectionProbably, Snake snake) {
        super(field, cell, sleepDelayMs, bestDirectionProbably, snake);
    }

    @Override
    public void eat(Snake snake) {
        getField().stop();
    }
}
