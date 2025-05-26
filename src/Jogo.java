
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


public class Jogo extends Canvas implements KeyListener, Runnable{

    public static int LARGURA = 235;
    public static int ALTURA = 290;
    public static int ESCALA = 2;
    public BufferedImage TelaDoJogo = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);

    public static RaqueteJogador objetoRaqueteJogador;
    public static Bola objetoBola;

    public Jogo(){
        this.setPreferredSize(new Dimension(LARGURA*ESCALA, ALTURA*ESCALA));

        // Iniciar os objetos bola e raquete
        objetoRaqueteJogador = new RaqueteJogador(100, ALTURA-30);
        objetoBola = new Bola(100, ALTURA/2 -1);

        // Adicionar manipulador de eventos (leitura do teclado)
        this.addKeyListener(new InterrupcaoTeclado(objetoRaqueteJogador));

        // Permitir que o Canvas receba foco
        this.setFocusable(true);
        this.requestFocusInWindow();
    }


    public void AtualizarPosicoesObjetos() throws InterruptedException{
        //Calcula as novas posições da raquete e da bola
        objetoRaqueteJogador.AtualizarPosicao();
        objetoBola.AtualizarPosicao();
    }

    public void DesenharJogoNaTela(){
        //criar o front Buffer
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }

        //Desenhar objetos do jogo no backbuffer
        Graphics g = TelaDoJogo.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, LARGURA, ALTURA);
        objetoRaqueteJogador.Desenhar(g);
        objetoBola.Desenhar(g);

        //Transferir a imagem do backbuffer para o frontbuffer
        g = bs.getDrawGraphics();
        g.drawImage(TelaDoJogo, 0, 0, LARGURA*ESCALA, ALTURA*ESCALA, null);
        bs.show(); //Mostrar o frontbuffer
    }

    @Override
    public void run(){
        while(true){
            //atualizar as posições de todo objeto do jogo
            try{
                AtualizarPosicoesObjetos();
            }catch (InterruptedException e1){
                e1.printStackTrace();
            }

            DesenharJogoNaTela();
            try{
                Thread.sleep(5); //Aguardar 5 milisegundos e continuar a execução
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){

    }

    @Override
    public void keyReleased(KeyEvent e){
        
    }
}
