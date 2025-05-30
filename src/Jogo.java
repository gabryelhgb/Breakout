// Importando as bibliotecas
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private BotaoSair botaoSair;

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

        botaoSair = new BotaoSair(Jogo.LARGURA - 20, 2, 16, 16); // ajuste o tamanho conforme seu PNG
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
                Som.tocar("vida.wav"); // Toca som de perda de vida
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
        botaoSair.desenhar(g);

        // Transferir imagem do backbuffer para o frontbuffer
        g = bs.getDrawGraphics();
        g.drawImage(TelaDoJogo, 0, 0, LARGURA * ESCALA, ALTURA * ESCALA, null);
        bs.show();
    }

    // Loop principal do jogo
    @Override
    public void run() {
        // Tela inicial animada
        while (!jogoIniciado) {
            animarTelaInicial();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean fimDeJogo = false;

        // Loop principal do jogo
        while (true) {
            if (!fimDeJogo) {
                try {
                    AtualizarPosicoesObjetos();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            DesenharJogoNaTela();

            // Verifica se perdeu todas as vidas ou venceu quebrando todos os blocos
            if (!fimDeJogo && (interfaceJogo.jogoAcabou() || todosBlocosDestruídos())) {
                interfaceJogo.pararTemporizador();
                Som.pararMusicaFundo(); // Para a música de fundo
                if (todosBlocosDestruídos()) {
                    Som.tocar("venceu.wav");
                }
                else if (interfaceJogo.jogoAcabou()) {
                    Som.tocar("perdeu.wav");
                } 
                fimDeJogo = true;
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método que desenha a animação da tela inicial
    public void animarTelaInicial() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        // 1. Atualiza apenas a posição da bola (sem colisão com blocos)
        objetoBola.AtualizarPosicaoSemQuebrarBlocos();

        // 2. Faz a raquete ocupar todo o eixo X
        objetoRaqueteJogador.x = 0;
        objetoRaqueteJogador.largura_raquete = LARGURA;

        // 3. Congela o temporizador em 0
        InterfaceJogo interfaceTemp = interfaceJogo;
        int segundosOriginais = interfaceTemp.segundos;
        interfaceTemp.segundos = 0;

        Graphics g = TelaDoJogo.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, LARGURA, ALTURA);

        objetoRaqueteJogador.Desenhar(g);
        objetoBola.Desenhar(g);
        for (Bloco bloco : blocos) {
            bloco.desenhar(g);
        }
        interfaceTemp.desenharInformacoes(g);

        // Mensagem centralizada
        g.setColor(Color.white);
        Font fonte = new Font("Arial", Font.BOLD, 12);
        g.setFont(fonte);

        String texto1 = "PRESSIONE QUALQUER TECLA";
        String texto2 = "OU CLIQUE PARA INICIAR";

        int larguraTexto1 = g.getFontMetrics(fonte).stringWidth(texto1);
        int larguraTexto2 = g.getFontMetrics(fonte).stringWidth(texto2);

        int xCentralizado1 = (LARGURA - larguraTexto1) / 2;
        int xCentralizado2 = (LARGURA - larguraTexto2) / 2;
        int yMeio = ALTURA / 2;

        g.drawString(texto1, xCentralizado1, yMeio - 8);
        g.drawString(texto2, xCentralizado2, yMeio + 8);

        // Desenha o botão sair sempre
        botaoSair.desenhar(g);

        g = bs.getDrawGraphics();
        g.drawImage(TelaDoJogo, 0, 0, LARGURA * ESCALA, ALTURA * ESCALA, null);
        bs.show();

        // Restaura o valor do temporizador (caso necessário)
        interfaceTemp.segundos = segundosOriginais;
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
        if (!jogoIniciado) {
            jogoIniciado = true;
            objetoRaqueteJogador.largura_raquete = 40; // valor padrão da raquete
            objetoRaqueteJogador.x = (LARGURA - objetoRaqueteJogador.largura_raquete) / 2; // centraliza
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() / ESCALA;
        int mouseY = e.getY() / ESCALA;
        if (botaoSair.foiClicado(mouseX, mouseY)) {
            System.exit(0);
        }
        if (!jogoIniciado) {
            jogoIniciado = true;
            objetoRaqueteJogador.largura_raquete = 40; // valor padrão da raquete
            objetoRaqueteJogador.x = (LARGURA - objetoRaqueteJogador.largura_raquete) / 2; // centraliza
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}