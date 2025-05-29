import java.awt.*;

public class Bloco {
    public int x, y, largura, altura;
    public boolean visivel = true;
    public Color cor;

    public Bloco(int x, int y, int largura, int altura, Color cor) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.cor = cor;
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            g.setColor(cor);
            g.fillRect(x, y, largura, altura);
            g.setColor(Color.black);
            g.drawRect(x, y, largura, altura);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }
}
