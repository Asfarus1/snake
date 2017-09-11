package ru.asfarus.test.model;

import ru.asfarus.test.model.frogs.Frog;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Snake extends CellFigure {
    private int len;
    private final int minLen;
    private MoveDirections moveDirection = MoveDirections.FORWARD;
    private Directions lastDirection = Directions.RIGHT;
    private final List<Cell> body = new LinkedList<Cell>();
    private Cell tail;

    Snake(Field field, Cell cell, long sleepDelayMs, int length) {
        super(field, cell, sleepDelayMs);
        this.minLen = length;
        this.len = length;
    }

    void setMoveDirection(MoveDirections moveDirection) {
        this.moveDirection = moveDirection;
    }

    private Directions nextDirection() {
        if (MoveDirections.FORWARD.equals(moveDirection)) {
            return lastDirection;
        }

        Directions[] directions = Directions.values();
        return directions[
                (MoveDirections.RIGHT.equals(moveDirection)
                        ? lastDirection.ordinal() + 1
                        : lastDirection.ordinal() - 1 + directions.length
                ) % directions.length];
    }

    protected void move() {
        final Directions direction = nextDirection();
        final Cell cell = getCell();
        int x = cell.getX() + direction.getX();
        int y = cell.getY() + direction.getY();
        if (getField().moveToCell(x, y, this)) {
            lastDirection = direction;
            moveDirection = MoveDirections.FORWARD;
        }
    }

    public boolean moveToCell(Cell cell) {
        CellFigure figure = cell.getFigure();
        if (figure == null){
            step(cell);
        }else if (figure instanceof Frog){
            ((Frog) figure).eat(this);
            step(cell);
        }else {
            die();
        }

        getField().cellTypeChange(cell);
        return true;
    }

    private void step(Cell cell){
        if (!isDead()) {
            Cell tempCell = getCell();
            body.add(0, tempCell);
            setCell(cell);
            getField().cellTypeChange(tempCell);

            int size = body.size();
            if (size > len) {
                final ListIterator<Cell> it = body.listIterator(size);
                while (size-- > len) {
                    tempCell = it.previous();
                    tempCell.setFigure(null);
                    it.remove();
                }
                if (it.hasPrevious()) {
                    tail = it.previous();
                    getField().cellTypeChange(tail);
                }
            }
        }
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len < minLen? minLen : len;
    }

    boolean isTail(Cell cell){
        return tail != null && tail.equals(cell);
    }

    boolean isHead(Cell cell){
        return getCell().equals(cell);
    }

    @Override
    public void die() {
        super.die();
        getField().stop();
    }

    public Directions getLastDirection() {
        return lastDirection;
    }
}
