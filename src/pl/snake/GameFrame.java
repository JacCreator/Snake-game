package pl.snake;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {
    JButton resetButton;

    GameFrame() {
        this.add(new GamePanel()); //utworzenie nowego panelu gry
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); //ograniczenie dotyczące zmiany wielkości okna gry
        this.pack(); //metoda zwiększająca rozmiar danej ramki, aby cała jej zawartość
                     //była w preferowanych rozmiarach
        resetButton = new JButton();
        resetButton.addActionListener(this);
        this.add(resetButton);
        this.setVisible(true);
        this.setLocationRelativeTo(null); //ustawienie panelu na środku monitora
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == resetButton) {
            new GameFrame();
        }
    }
}
