package ru.asfarus.test.controller;

import ru.asfarus.test.app.*;

public class SnakeController implements ChangeListener, Controller {
    private SnakeModel model;
    private View view;

    public SnakeController(SnakeModel model, View view) {
        this.model = model;
        this.view = view;
    }

    public void onCellTypeChange(int x, int y, CellType cellType) {
        view.updateCellUI(x, y, cellType);
    }

    public void onStart() {
        view.setStopped(false);
    }

    public void onStop() {
        view.setStopped(true);
    }

    public void onScoreUpdate(int score) {
        view.updateScore(score);
    }

    public void start() {
        model.start();
    }

    public void stop() {
        model.stop();
    }

    public void setSnakeDirectionLeft() {
        model.setSnakeDirectionLeft();
    }

    public void setSnakeDirectionRight() {
        model.setSnakeDirectionRight();
    }

    public void init() {
        model.setChangeListener(this);
        view.setController(this);
        view.start();
    }

    public void pauseGame(boolean pause) {
        model.setPaused(pause);
    }

    public void onPause(boolean pause) {
        view.setPaused(pause);
    }
}
