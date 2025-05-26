import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InterrupcaoTeclado extends KeyAdapter {
    RaqueteJogador objetoRaqueteJogador;

    InterrupcaoTeclado(RaqueteJogador obj) {
        objetoRaqueteJogador = obj;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            objetoRaqueteJogador.Mutex.acquire();

            // Direita: seta DIREITA ou tecla D
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                objetoRaqueteJogador.direita = true;
            }
            // Esquerda: seta ESQUERDA ou tecla A
            else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                objetoRaqueteJogador.esquerda = true;
            }

        } catch (InterruptedException e1) {
            e1.printStackTrace();
            
        } finally {
            // Libera a região crítica
            objetoRaqueteJogador.Mutex.release();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            objetoRaqueteJogador.Mutex.acquire();

            // Soltou a seta DIREITA ou tecla D
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                objetoRaqueteJogador.direita = false;
            }
            // Soltou a seta ESQUERDA ou tecla A
            else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                objetoRaqueteJogador.esquerda = false;
            }

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        // Libera a região crítica
        objetoRaqueteJogador.Mutex.release();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }
}