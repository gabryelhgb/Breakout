
import java.awt.Canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Jogo extends Canvas implements KeyListener, Runnable {

    public static int LARGURA = 235;
    public static int ALTURA = 290;
    public static int ESCALA = 2;
    public BufferedImage TelaDoJogo = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);

    public static RaqueteJogador objetoRaqueteJogador;
    public static Bola objetoBola;

    // Adicionado: objeto da interface do jogo (vidas, pontos, tempo)
    public static InterfaceJogo interfaceJogo;

    public Jogo() {
        this.setPreferredSize(new Dimension(LARGURA * ESCALA, ALTURA * ESCALA));

        // Iniciar os objetos bola, raquete e interface do jogo
        objetoRaqueteJogador = new RaqueteJogador(100, ALTURA - 30);
        objetoBola = new Bola(100, ALTURA / 2 - 1);
        interfaceJogo = new InterfaceJogo();

        // Adicionar manipulador de eventos (leitura do teclado)
        this.addKeyListener(new InterrupcaoTeclado(objetoRaqueteJogador));

        // Permitir que o Canvas receba foco
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void AtualizarPosicoesObjetos() throws InterruptedException {
        // Atualiza posições normalmente, exceto se o jogo tiver acabado
        if (!interfaceJogo.jogoAcabou()) {
            objetoRaqueteJogador.AtualizarPosicao();
            objetoBola.AtualizarPosicao();

            // Verifica se a bola passou do limite inferior da tela
            if (objetoBola.y >= ALTURA) {
                System.out.println("Você perdeu uma vida!");
                interfaceJogo.perderVida();

                // Reinicia a bola no centro
                objetoBola = new Bola(100, ALTURA / 2 - 1);
            }
        }
    }

    public void DesenharJogoNaTela() {
        // Criar o front buffer
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        // Desenhar objetos no backbuffer
        Graphics g = TelaDoJogo.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, LARGURA, ALTURA);

        objetoRaqueteJogador.Desenhar(g);
        objetoBola.Desenhar(g);

        // Desenhar HUD com vidas, pontos e cronômetro
        interfaceJogo.desenharInformacoes(g);

        // Transferir imagem para o front buffer
        g = bs.getDrawGraphics();
        g.drawImage(TelaDoJogo, 0, 0, LARGURA * ESCALA, ALTURA * ESCALA, null);
        bs.show();
    }

    @Override
    public void run() {
        while (true) {
            try {
                AtualizarPosicoesObjetos();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            DesenharJogoNaTela();

            // Parar loop se o jogo acabou
            if (interfaceJogo.jogoAcabou()) {
                interfaceJogo.pararTemporizador();
                break;
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
