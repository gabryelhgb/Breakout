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
    int baseY = Jogo.ALTURA - 5;

    //Fonte e cor
    g.setColor(Color.white);
    g.setFont(new Font("Arial", Font.PLAIN, 10));

    //Texto na tela
    g.drawString("Vidas: " + vidas, 20, baseY);
    g.drawString("Pontuação: " + pontuacao, 85, baseY); 
    g.drawString("Tempo: " + segundos + "s", 170, baseY);

    //Coração vermelho na esquerda de Vida
    g.setColor(Color.red);
    int x = 5;
    int y = baseY - 8;

    int[] xPoints = {x + 4, x, x - 4, x, x + 4, x + 8, x + 12, x + 8};
    int[] yPoints = {y, y - 4, y, y + 4, y + 8, y + 4, y, y - 4};
    g.fillPolygon(xPoints, yPoints, xPoints.length);

    //Fim de jogo
        if (jogoAcabou()) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("FIM DE JOGO", 50, 150);
        }else if (Jogo.blocos != null && Jogo.blocos.stream().noneMatch(b -> b.visivel)) {
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("VOCÊ VENCEU!", 40, 150);
        }
    }

}
