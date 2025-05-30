//importando as bibliotecas
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;

public class RaqueteJogador {
    //Declarando os atributos
    public boolean direita, esquerda;
    public int x, y;
    public int largura_raquete, altura_raquete;
    public Semaphore Mutex;
    private BufferedImage imagemRaquete;

    //Construtor da raquete
    public RaqueteJogador(int x, int y){
        this.x = x;
        this.y = y;
        this.largura_raquete = 40;
        this.altura_raquete = 5;
        Mutex = new Semaphore(1);

        try {
            imagemRaquete = ImageIO.read(getClass().getResource("/images/Prancha.png"));
        } catch (IOException | IllegalArgumentException e) {
            imagemRaquete = null; // Se não encontrar, usa o desenho padrão
        }
    }

    //Método para fazer a raquete mexer
    public void AtualizarPosicao() throws InterruptedException{
        //Região crítica
        Mutex.acquire();

            if (direita) //A tecla "->" foi acionada?
            {
                x++; //Mover a posição da raquete em 1 posição para direita
            }
            else if (esquerda)//A tecla "<-" foi acionada?
            {
                x--; //Mover a posição da raquete em 1 posição para esquerda
            }

        Mutex.release();

        if(x+largura_raquete > Jogo.LARGURA)//Se a posição da raquete ultrapassou o canto direito da tela
        {
            x = Jogo.LARGURA - largura_raquete;//Posiciona o canto da raquete no final do lado direito
        }
        else if(x < 0)//Se a posição da raquete ultrapassou o canto esquerdo da tela
        {
            x = 0;//Posiciona o canto da raquete no inicio do lado esquerdo
        }
    }

    public void Desenhar (Graphics g){
        if (imagemRaquete != null) {
            g.drawImage(imagemRaquete, x, y, largura_raquete, altura_raquete, null);
        } else {
            //setar a cor da raquete
            g.setColor(Color.white);
            //Desenhar o retangulo que representa a raquete
            g.fillRect(x, y, largura_raquete, altura_raquete);
        }
    }

}