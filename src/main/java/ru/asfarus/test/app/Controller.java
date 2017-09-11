package ru.asfarus.test.app;

public interface Controller {

    void start();

    void stop();

    void pauseGame(boolean pause);

    void setSnakeDirectionLeft();

    void setSnakeDirectionRight();

    void init();
}
