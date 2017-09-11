package ru.asfarus.test.app;

public interface ChangeListener {
    void onCellTypeChange(int x, int y, CellType cellType);

    void onStart();

    void onStop();

    void onPause(boolean pause);

    void onScoreUpdate(int score);
}
