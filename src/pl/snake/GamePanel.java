package pl.snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75; //im mniejsza wartość DELAY tym wąż porusza się szybciej
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int appleEaten;
    int appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Graphics g;
    JButton button;
    JButton resetButton;

    GamePanel() {
        Random random = new Random();
        this.random = random;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); //ustawienie rozmiaru ramki
        this.setBackground(Color.black);
        this.setFocusable(true); //wskazuje czy dany komponent może uzyskać focus, jeśli zostanie o to poproszony
        this.addKeyListener(new MyKeyAdapter()); //dodanie 'słuchacza' przycisków
        startGame();
    }

    public void startGame() {
        newApple(); //stworzenie nowego jabłka
        running = true;
        timer = new Timer(DELAY, this); //inicjalizacja zmiennej typu Timer
        timer.start(); //rozpoczęcie odliczania czasu
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            //rysowanie siatki na ramce
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);  //x1-poz. poczatkowa,
                // x2-poz.koncowa; y1,y2 analogicznie
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED); //kolor jabłka
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            //stworzenie węża
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }
            //stworzenie tablicy wyników
            g.setColor(Color.RED);
            g.setFont(new Font("Futura", Font.PLAIN, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2,
                    g.getFont().getSize());

        } else {
            gameOver(g);
        }

    }

    public void newApple() {
        //losowanie miejsca pojawienia się jabłka
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        //mechanika ruchu węża
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE; //jedna kratka w górę
            case 'D' -> y[0] = y[0] + UNIT_SIZE; //jedna kratka w dół
            case 'R' -> x[0] = x[0] + UNIT_SIZE; //jedna kratka w prawo
            case 'L' -> x[0] = x[0] - UNIT_SIZE; //jedna kratka w lewo
        }
    }

    public void checkApple() {
        //detekcja kolizji z jabłkiem
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        //sprawdzanie kolizji
        //kolizja: wąż zjada się sam
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //kolizja: wyjście za mapę
        if (x[0] < 0) {
            running = false;
        }
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        //tworzenie napisu w przypadku zakończenia gry
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT/2);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        //nadpisanie metody odpowiedzialnej za reakcje programu na przyciski klawiatury
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
            if(e.getSource()==button) {
                startGame();
            }
        }
    }
}


