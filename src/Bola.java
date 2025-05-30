import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Bola {
    public double x, y;
    public int largura_bola, altura_bola;
    public double dx, dy;
    public double velocidade = 1;
    private BufferedImage imagemBola;

    public Bola(int x, int y) {
        this.x = x;
        this.y = y;
        this.largura_bola = 4;
        this.altura_bola = 4;

        try {
            imagemBola = ImageIO.read(getClass().getResource("/images/Ball.png"));
        } catch (IOException | IllegalArgumentException e) {
            imagemBola = null; // Se não encontrar, usa o desenho padrão
        }

        CalcularAnguloDeslocamento();
    }

    private void CalcularAnguloDeslocamento() {
        int angle = new Random().nextInt(120 - 45) + 45 + 1;
        dx = Math.cos(Math.toRadians(angle));
        dy = Math.sin(Math.toRadians(angle));
    }

    public void AtualizarPosicao() {
        // Colisão com bordas laterais
        if (x + (dx * velocidade) + largura_bola >= Jogo.LARGURA || x + (dx * velocidade) < 0) {
            dx *= -1;
            Som.tocar("parede.wav");
        }

        // Colisão com raquete
        Rectangle regiaoBola = new Rectangle((int) (x + (dx * velocidade)), (int) (y + (dy * velocidade)), largura_bola, altura_bola);
        Rectangle regiaoRaqueteJogador = new Rectangle(
                Jogo.objetoRaqueteJogador.x,
                Jogo.objetoRaqueteJogador.y,
                Jogo.objetoRaqueteJogador.largura_raquete,
                Jogo.objetoRaqueteJogador.altura_raquete
        );

        if (regiaoBola.intersects(regiaoRaqueteJogador)) {
            CalcularAnguloDeslocamento();
            if (dy > 0) dy *= -1;
            Som.tocar("raquete.wav");
        }

        // Colisão com o topo da tela
        if (y < 0) {
            CalcularAnguloDeslocamento();
            if (dy < 0) dy *= -1;
            Som.tocar("parede.wav");
        }

        // Colisão com blocos
        for (int i = 0; i < Jogo.blocos.size(); i++) {
            Bloco bloco = Jogo.blocos.get(i);
            if (bloco.visivel) {
                Rectangle regiaoBloco = bloco.getBounds();
                if (regiaoBola.intersects(regiaoBloco)) {
                    bloco.visivel = false;
                    dy *= -1;
                    Jogo.interfaceJogo.adicionarPonto(10);
                    Som.tocar("bloco.wav");
                    break;
                }
            }
        }

        // Atualiza a posição da bola
        x += dx * velocidade;
        y += dy * velocidade;
    }

    public void AtualizarPosicaoSemQuebrarBlocos() {
        // Atualiza posição normalmente, mas sem colisão com blocos
        // Colisão com raquete
        Rectangle regiaoBola = new Rectangle((int) (x + (dx * velocidade)), (int) (y + (dy * velocidade)), largura_bola, altura_bola);
        Rectangle regiaoRaqueteJogador = new Rectangle(
                Jogo.objetoRaqueteJogador.x,
                Jogo.objetoRaqueteJogador.y,
                Jogo.objetoRaqueteJogador.largura_raquete,
                Jogo.objetoRaqueteJogador.altura_raquete
        );

        if (regiaoBola.intersects(regiaoRaqueteJogador)) {
            dy *= -1;
        }
        // Colisão com topo
        if (y < 0 && dy < 0) {
            dy *= -1;
        }
        // Colisão com laterais
        if (x < 0 && dx < 0) {
            dx *= -1;
        }
        if (x + largura_bola > Jogo.LARGURA && dx > 0) {
            dx *= -1;
        }
        // Atualiza posição
        x += dx * velocidade;
        y += dy * velocidade;
    }

    public void Desenhar(Graphics g) {
        if (imagemBola != null) {
            g.drawImage(imagemBola, (int) x, (int) y, largura_bola, altura_bola, null);
        } else {
            g.setColor(Color.yellow);
            g.fillRect((int) x, (int) y, largura_bola, altura_bola);
        }
    }
}