import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class InterfaceJogo {
    private int vidas = 3;
    private int pontuacao = 0;
    private int segundos = 0;

    private Timer temporizador;

    public InterfaceJogo() {
        temporizador = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                segundos++;
            }
        });
        temporizador.start();
    }

    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public void adicionarPonto(int valor) {
        pontuacao += valor;
    }

    public boolean jogoAcabou() {
        return vidas <= 0;
    }

    public void pararTemporizador() {
        temporizador.stop();
    }

    public void desenharInformacoes(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Vidas: " + vidas, 10, 20);
        g.drawString("Pontuação: " + pontuacao, 90, 20);
        g.drawString("Tempo: " + segundos + "s", 180, 20);

        if (jogoAcabou()) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("FIM DE JOGO", 50, 150);
        }
    }
}