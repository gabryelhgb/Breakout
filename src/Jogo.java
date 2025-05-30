// Importando as bibliotecas
import java.awt.Canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Jogo extends Canvas implements KeyListener, MouseListener, Runnable {

    // Declarando constantes e objetos do jogo
    public static int LARGURA = 235;
    public static int ALTURA = 290;
    public static int ESCALA = 2;

    public BufferedImage TelaDoJogo = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);

    public static RaqueteJogador objetoRaqueteJogador;
    public static Bola objetoBola;
    public static InterfaceJogo interfaceJogo;
    public static ArrayList<Bloco> blocos;

    // Variável para controlar se o jogo já começou
    private boolean jogoIniciado = false;

    // Construtor
    public Jogo() {
        this.setPreferredSize(new Dimension(LARGURA * ESCALA, ALTURA * ESCALA));

        // Iniciar os objetos bola, raquete e interface
        objetoRaqueteJogador = new RaqueteJogador(100, ALTURA - 30);
        objetoBola = new Bola(100, ALTURA / 2 - 1);
        interfaceJogo = new InterfaceJogo();

        // Criar os blocos do jogo
        blocos = new ArrayList<>();
        
        int linhas = 4;
        int colunas = 7;
        int largura = 30;
        int altura = 10;
        int espacamento = 5;

        // Calcular margem para centralizar os blocos
        int totalLargura = colunas * (largura + espacamento) - espacamento;
        int margemLateral = (Jogo.LARGURA - totalLargura) / 2;
        int margemSuperior = 20;

        // Definir cores por linha
        Color[] cores = {Color.red, Color.orange, Color.yellow, Color.green};

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                int x = margemLateral + j * (largura + espacamento);
                int y = margemSuperior + i * (altura + espacamento);
                Color cor = cores[i % cores.length];
                blocos.add(new Bloco(x, y, largura, altura, cor));
            }
        }

        // Adicionar manipuladores de evento (teclado e mouse)
        this.addKeyListener(new InterrupcaoTeclado(objetoRaqueteJogador)); // Teclado para raquete
        this.addKeyListener(this); // Teclado para iniciar jogo
        this.addMouseListener(this); // Mouse para iniciar jogo

        // Permitir que o Canvas receba foco
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    // Atualizar a posição dos objetos do jogo
    public void AtualizarPosicoesObjetos() throws InterruptedException {
        // Se o jogo ainda não acabou
        if (!interfaceJogo.jogoAcabou()) {
            objetoRaqueteJogador.AtualizarPosicao();
            objetoBola.AtualizarPosicao();

            // Verifica se a bola caiu no fundo da tela
            if (objetoBola.y >= ALTURA) {
                System.out.println("Você perdeu uma vida!");
                interfaceJogo.perderVida();
                objetoBola = new Bola(100, ALTURA / 2 - 1); // Reiniciar bola
            }
        }
    }

    // Desenhar todos os objetos do jogo na tela
    public void DesenharJogoNaTela() {
        // Criar o front buffer
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        // Desenhar os objetos no backbuffer
        Graphics g = TelaDoJogo.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, LARGURA, ALTURA);

        objetoRaqueteJogador.Desenhar(g);
        objetoBola.Desenhar(g);

        // Desenhar os blocos
        for (Bloco bloco : blocos) {
            bloco.desenhar(g);
        }

        // Desenhar vidas, pontos e tempo
        interfaceJogo.desenharInformacoes(g);

        // Transferir imagem do backbuffer para o frontbuffer
        g = bs.getDrawGraphics();
        g.drawImage(TelaDoJogo, 0, 0, LARGURA * ESCALA, ALTURA * ESCALA, null);
        bs.show();
    }

    // Loop principal do jogo
    @Override
    public void run() {
        // 🔄 Tela inicial animada antes do jogo começar
        while (!jogoIniciado) {
            animarTelaInicial();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 🔁 Loop principal do jogo após início
        while (true) {
            try {
                AtualizarPosicoesObjetos();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            DesenharJogoNaTela();

            // Verifica se perdeu todas as vidas ou venceu quebrando todos os blocos
            if (interfaceJogo.jogoAcabou() || todosBlocosDestruídos()) {
                interfaceJogo.pararTemporizador();
                break;
            }

            try {
                Thread.sleep(5); // Delay entre atualizações
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método que desenha a animação da tela inicial
    public void animarTelaInicial() {
        objetoBola.AtualizarPosicao();

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = TelaDoJogo.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, LARGURA, ALTURA);

        objetoBola.Desenhar(g);

        g.setColor(Color.white);
        Font fonte = new Font("Arial", Font.BOLD, 12);
        g.setFont(fonte);

    // Centraliza horizontalmente a frase
        String texto = "Clique para começar";
        int larguraTexto = g.getFontMetrics(fonte).stringWidth(texto);
        int xCentralizado = (LARGURA - larguraTexto) / 2;
        int yMeio = ALTURA / 2;

        g.drawString(texto, xCentralizado, yMeio);

        g = bs.getDrawGraphics();
        g.drawImage(TelaDoJogo, 0, 0, LARGURA * ESCALA, ALTURA * ESCALA, null);
        bs.show();
    }

    // Verificar se todos os blocos foram destruídos
    public boolean todosBlocosDestruídos() {
        for (Bloco bloco : blocos) {
            if (bloco.visivel) {
                return false;
            }
        }
        return true;
    }

    // Eventos para iniciar o jogo
    @Override
    public void keyPressed(KeyEvent e) {
        jogoIniciado = true;
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        jogoIniciado = true;
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}