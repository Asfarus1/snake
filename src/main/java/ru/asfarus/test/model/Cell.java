package ru.asfarus.test.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final Lock lock = new ReentrantLock();
    private CellFigure figure;
    private final int x;
    private final int y;
    private final Field field;

    Cell(int x, int y, Field field) {
        this.x = x;
        this.y = y;
        this.field = field;
    }

    Lock getLock() {
        return lock;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellFigure getFigure() {
        return figure;
    }

    public void setFigure(CellFigure figure) {
        lock.lock();
        try {
            this.figure = figure;
            field.cellTypeChange(this);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        return getX() == cell.getX() && getY() == cell.getY();
    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        return result;
    }
}
