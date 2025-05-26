import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        Jogo game = new Jogo();

        JFrame janelaprincipal = new JFrame("Breakout");

        janelaprincipal.add(game);
        janelaprincipal.pack();
        janelaprincipal.setLocationRelativeTo(null); // centraliza
        janelaprincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janelaprincipal.setResizable(false);
        janelaprincipal.setBackground(Color.black);
        janelaprincipal.setVisible(true);

        // Garante que a janela venha para frente e receba foco após estar visível
        SwingUtilities.invokeLater(() -> {
            janelaprincipal.toFront();
            janelaprincipal.requestFocus();
            game.requestFocusInWindow(); // << dá o foco ao Canvas
        });


        new Thread(game).start();
    }
}