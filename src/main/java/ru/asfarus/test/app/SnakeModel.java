package ru.asfarus.test.app;

public interface SnakeModel {

    void start();

    void stop();

    void setPaused(boolean paused);

    void setSnakeDirectionLeft();

    void setSnakeDirectionRight();

    void setChangeListener(ChangeListener listener);
}
