//importando as bibliotecas
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Bola {
    public double x, y;
    public int largura_bola, altura_bola;
    public double dx, dy; //Deslocamento nos eixos x e y (calculando angulos)
    public double velocidade = 1;

    public Bola(int x, int y){
        this.x = x;
        this.y = y;
        this.altura_bola = 4;
        this.largura_bola = 4;

        CalcularAnguloDeslocamento();
    }

    private void CalcularAnguloDeslocamento(){
        //Gera um angulo aleatorio para dar a sensação de um movimento mais realístico para a bola
        int angle = new Random().nextInt(120 - 45) + 45 + 1;
        dx = Math.cos(Math.toRadians(angle));
        dy = Math.sin(Math.toRadians(angle));
    }

    public void AtualizarPosicao(){
        //Se a bola atingiu o limite do lado direito
        if (x+(dx*velocidade) + largura_bola >= Jogo.LARGURA)
            dx*= -1;
        //Se a bola atingiu o lado esquerdo
        else if (x+(dx*velocidade) < 0)
            dx*= -1;

        if (y >= Jogo.ALTURA){
        System.out.println("Você perdeu!");
        x = 0;
        y = 0;
        return;
        }

        //Definição do Rectangle: (x_cantoSuperior, y_cantoSuperior, largura, altura)
        //Delimita as regiões em que estão a bola e a raquete
        Rectangle regiaoBola = new Rectangle ((int) (x+(dx*velocidade)), (int) (y+(dy*velocidade)), largura_bola, altura_bola);
        Rectangle regiaoRaqueteJogador = new Rectangle(Jogo.objetoRaqueteJogador.x, Jogo.objetoRaqueteJogador.y, Jogo.objetoRaqueteJogador.largura_raquete, Jogo.objetoRaqueteJogador.altura_raquete);

        //Verificar a colisão da bola com a raquete do jogador
        if(regiaoBola.intersects(regiaoRaqueteJogador))
        {
            CalcularAnguloDeslocamento();
            if(dy > 0)
            dy *= -1;
        }

        //Verificar se a bola ultrapassou o topo da janela do jogo
        if(y < 0)
        {
            CalcularAnguloDeslocamento();
            if (dy <0)
            dy *= -1;
        }

        //Atualizar a posição da bola
        x+=dx*velocidade;
        y+=dy*velocidade;
    }

    public void Desenhar (Graphics g){
        g.setColor(Color.yellow);
        g.fillRect((int)x, (int)y , largura_bola, altura_bola);
    }
}