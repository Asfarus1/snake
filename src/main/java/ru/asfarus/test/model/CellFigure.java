package ru.asfarus.test.model;

public abstract class CellFigure implements Runnable{

    private final Field field;
    private Cell cell;
    private final long sleepDelayMs;

    private volatile boolean isDead = false;

    private final static long SLEEP_TIME = 100;

    public CellFigure(Field field, Cell cell, long sleepDelayMs) {
        this.field = field;
        this.cell = cell;
        this.sleepDelayMs = sleepDelayMs;
    }

    boolean isDead() {
        return isDead;
    }

    public void run(){
        try {
            final Thread thread = Thread.currentThread();
            thread.setName(getClass().getSimpleName() + "_" + thread.getName());
            cell.setFigure(this);
            Thread.sleep(sleepDelayMs);
            while (!isDead && !field.isStopped()) {
                if (field.isPaused()){
                    Thread.sleep(SLEEP_TIME);
                }else {
                    move();
                    Thread.sleep(sleepDelayMs);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Cell getCell() {
        return cell;
    }

    protected void setCell(Cell cell) {
        this.cell = cell;
        cell.setFigure(this);
    }

    protected abstract void move();

    public abstract boolean moveToCell(Cell cell);

    public void die(){
        isDead = true;
    }

    protected Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "isDead=" + isDead +
                '}';
    }
}
