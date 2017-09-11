package ru.asfarus.test.app;

public interface View {

    void updateCellUI(int x, int y, CellType cellType);

    void updateScore(int Score);

    void setPaused(boolean pause);

    void setStopped(boolean stopped);

    void start();

    void setController(Controller controller);
}
