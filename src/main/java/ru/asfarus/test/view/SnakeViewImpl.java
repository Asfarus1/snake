package ru.asfarus.test.view;

import ru.asfarus.test.app.CellType;
import ru.asfarus.test.app.Controller;
import ru.asfarus.test.app.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeViewImpl extends JFrame implements View{
    private static final int CELL_SIZE = 50;
    private JButton btnStart = new JButton("Start");
    private JButton btnPause = new JButton("Pause");
    private JButton btnStop = new JButton("Stop");
    private JLabel lblScore = new JLabel("0");
    private final FieldCell[][] cells;

    private Controller controller;

    private boolean isStopped = true;
    private boolean isPaused = false;

    public SnakeViewImpl(int width, int height) {
        super("Snake");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        setResizable(false);
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(btnStart);
        controlPanel.add(btnPause);
        controlPanel.add(btnStop);
        JLabel lblScoreTitle = new JLabel("Score:");
        controlPanel.add(lblScoreTitle);
        controlPanel.add(lblScore);
        getContentPane().add(controlPanel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new GridLayout(height, width));
        cells = new FieldCell[width][height];
        for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
                final FieldCell cell = new FieldCell();
                cells[i][j] = cell;
                cell.setSize(CELL_SIZE, CELL_SIZE);
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                gamePanel.add(cell);
            }
        }
        gamePanel.setPreferredSize(new Dimension(CELL_SIZE *width, CELL_SIZE *height));

        JScrollPane areaScrollPane = new JScrollPane(gamePanel);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        gamePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    controller.setSnakeDirectionLeft();
                }else if(e.getButton() == MouseEvent.BUTTON3){
                    controller.setSnakeDirectionRight();
                }
            }
        });

        getContentPane().add(areaScrollPane, BorderLayout.CENTER);
        pack();

        for (FieldCell[] cell : cells) {
            for (FieldCell fieldCell : cell) {
                fieldCell.setCellType(CellType.EMPTY);
            }
        }

        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isPaused){
                    controller.pauseGame(false);
                }else {
                    controller.start();
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.stop();
            }
        });

        btnPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    controller.pauseGame(isPaused = true);
            }
        });

        updateBtnView();
    }

    private void updateBtnView(){
        btnStop.setEnabled(!isStopped);
        btnPause.setEnabled(!isStopped && !isPaused);
        btnStart.setEnabled(isStopped || isPaused);
    }

    public void updateCellUI(int x, int y, CellType cellType) {
        cells[x][y].setCellType(cellType);
    }

    public void updateScore(int score) {
        lblScore.setText(String.valueOf(score));
    }

    public void setPaused(boolean pause) {
        isPaused = pause;
        updateBtnView();
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
        isPaused = false;
        updateBtnView();
        if (stopped){
            JOptionPane.showMessageDialog(this, "GAME OVER! YOUR SCORE IS " + lblScore.getText(), "SNAKE", JOptionPane.INFORMATION_MESSAGE);
        }else {
            lblScore.setText("0");
        }
    }

    public void start() {
        setVisible(true);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
