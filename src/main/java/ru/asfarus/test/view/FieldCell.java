package ru.asfarus.test.view;

import ru.asfarus.test.app.CellType;

import javax.swing.*;
import java.awt.*;

public class FieldCell extends JComponent {
    private CellType cellType;
    private Image buf;

    void setCellType(CellType cellType) {
        this.cellType = cellType;
        if (!isDoubleBuffered()){
            if (buf == null) {
                initBuf();
            }
            draw(buf.getGraphics());
        }
        repaint();
    }

    private void draw(Graphics g){
        int w = getWidth();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, w);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, w, w);
        int c = w/2;
        int r;
        switch (cellType){
            case SNAKE_HEAD:
                g.setColor(Color.YELLOW);
                r = w/2;
                c = c - r / 2;
                g.fillOval(c, c, r, r);
                break;
            case SNAKE_BODY:
                g.setColor(Color.YELLOW);
                r = w / 3;
                c = c - r / 2;
                g.fillOval(c, c, r, r);
                break;
            case SNAKE_TAIL:
                g.setColor(Color.YELLOW);
                r = w/4;
                c = c - r / 2;
                g.fillOval(c, c, r, r);
                break;
            case GREEN_FROG:
                g.setColor(Color.GREEN);
                r = w/4;
                c = c - r / 2;
                g.fillOval(c, c, r, r);
                break;
            case RED_FROG:
                g.setColor(Color.RED);
                r = w/4;
                c = c - r / 2;
                g.fillOval(c, c, r, r);
                break;
            case BLUE_FROG:
                g.setColor(Color.BLUE);
                r = w / 4;
                c = c - r / 2;
                g.fillOval(c, c, r, r);
                break;
        }
    }

    private void initBuf(){
        int w = getWidth();
        int h = getHeight();
        buf = createImage(w, h);
    }

    @Override
    public void paint(Graphics g) {
        if (!isDoubleBuffered()){
            g.drawImage(buf, 0, 0, null);
        }else {
            draw(g);
        }
    }
}
