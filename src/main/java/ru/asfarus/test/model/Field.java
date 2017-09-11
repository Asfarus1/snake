package ru.asfarus.test.model;

import ru.asfarus.test.app.CellType;
import ru.asfarus.test.app.ChangeListener;
import ru.asfarus.test.app.SnakeModel;
import ru.asfarus.test.model.frogs.BlueFrog;
import ru.asfarus.test.model.frogs.Frog;
import ru.asfarus.test.model.frogs.FrogFactory;
import ru.asfarus.test.model.frogs.RedFrog;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class Field implements SnakeModel {

    private final int height;
    private final int width;
    private final int snakeLen;
    private final long snakeSleepDelayMs;
    private final int frogsCount;

    private ExecutorService executor;

    private Snake snake;
    private final Cell[][] cells;
    private final Random rnd = new Random();
    private final FrogFactory frogFactory;
    private final AtomicInteger score = new AtomicInteger();

    private ChangeListener listener;

    private volatile boolean isPaused = true;
    private volatile boolean isStopped = false;

    public Field(int width, int height, int snakeLen, long snakeSleepDelayMs, int frogsCount, int frogSleepDelayMs, float bestDirectionProbably) {
        this.height = height;
        this.width = width;
        this.snakeLen = snakeLen;
        this.snakeSleepDelayMs = snakeSleepDelayMs;
        this.frogsCount = frogsCount;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell(i, j, this);
            }
        }
        frogFactory = new FrogFactory(this, frogSleepDelayMs, bestDirectionProbably);
    }

    public void createFrog() {
        int x, y;
        while (true) {
            x = rnd.nextInt(width);
            y = rnd.nextInt(height);
            final Cell cell = cells[x][y];
            final Lock lock = cell.getLock();
            lock.lock();
            try {
                if (cell.getFigure() == null) {
                    executor.submit(frogFactory.generateRandomTypeFrog(cell, snake));
                    break;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public boolean moveToCell(int x, int y, CellFigure figure){
        final Cell targetCell = cells[(width + x) % width][(height + y) % height];
        final Lock lock = targetCell.getLock();
        lock.lock();
        try{
            return figure.moveToCell(targetCell);
        }finally {
            lock.unlock();
        }
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        if (listener != null){
            listener.onPause(isPaused);
        }
    }

    public void stop(){
        setPaused(true);
        isStopped = true;
        executor.shutdown();
        if (listener != null){
            listener.onStop();
        }
    }

    private void clear(){
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setFigure(null);
            }
        }
    }

    public void addScore(int count){
        score.addAndGet(count);
        if (listener != null){
            listener.onScoreUpdate(score.intValue());
        }
    }

    public void start() {
        clear();
        isPaused = false;
        isStopped = false;
        score.set(0);
        snake = new Snake(this, cells[0][0], snakeSleepDelayMs , snakeLen);
        executor = Executors.newFixedThreadPool(frogsCount + 1);
        executor.submit(snake);
        for (int i = 0; i < frogsCount; i++) {
            createFrog();
        }
        if (listener != null){
            listener.onStart();
        }
    }

    public void setSnakeDirectionLeft() {
        snake.setMoveDirection(MoveDirections.LEFT);
    }

    public void setSnakeDirectionRight() {
        snake.setMoveDirection(MoveDirections.RIGHT);
    }

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    void cellTypeChange(Cell cell){
        if (listener != null) {
            CellType cellType;
            CellFigure figure = cell.getFigure();
            if (figure instanceof BlueFrog) {
                cellType = CellType.BLUE_FROG;
            } else if (figure instanceof RedFrog) {
                cellType = CellType.RED_FROG;
            } else if (figure instanceof Frog) {
                cellType = CellType.GREEN_FROG;
            } else if (figure instanceof Snake) {
                cellType = snake.isHead(cell) ? CellType.SNAKE_HEAD : snake.isTail(cell) ? CellType.SNAKE_TAIL : CellType.SNAKE_BODY;
            } else {
                cellType = CellType.EMPTY;
            }
            listener.onCellTypeChange(cell.getX(), cell.getY(), cellType);
        }
    }

    boolean isPaused() {
        return isPaused;
    }

    boolean isStopped() {
        return isStopped;
    }

    public Snake getSnake() {
        return snake;
    }
}
