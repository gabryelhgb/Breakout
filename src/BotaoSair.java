import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BotaoSair {
    private int x, y, largura, altura;
    private BufferedImage imagemBotao;

    public BotaoSair(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        
        try {
            imagemBotao = ImageIO.read(getClass().getResource("/images/sair.png"));
        } catch (IOException | IllegalArgumentException e) {
            imagemBotao = null;
        }
    }

    public void desenhar(Graphics g) {
        if (imagemBotao != null) {
            g.drawImage(imagemBotao, x, y, largura, altura, null);
        }
    }

    public boolean foiClicado(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + largura && mouseY >= y && mouseY <= y + altura;
    }
}