import java.net.URL;
import javax.sound.sampled.*;

public class Som {
    private static Clip musicaFundo;

    public static void tocar(String nomeArquivo) {
        try {
            URL url = Som.class.getResource("/audio/" + nomeArquivo);
            if (url == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Se for o som de bloco, aumenta o volume
            if (nomeArquivo.equals("bloco.wav")) {
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(+2.0f);
            }
            // Se for o som de parede, aumenta o volume
            if (nomeArquivo.equals("parede.wav")) {
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-8.0f);
            }

            clip.start();
        } catch (Exception e) {
            System.err.println("Erro ao tocar som: " + nomeArquivo);
        }
    }

    public static void tocarMusicaFundo(String nomeArquivo) {
        pararMusicaFundo();
        try {
            URL url = Som.class.getResource("/audio/" + nomeArquivo);
            if (url == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            musicaFundo = AudioSystem.getClip();
            musicaFundo.open(audioIn);

            // Diminui o volume da música de fundo (-10.0f é mais baixo, ajuste como quiser)
            FloatControl volume = (FloatControl) musicaFundo.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-20.0f);

            musicaFundo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFundo.start();
        } catch (Exception e) {
            System.err.println("Erro ao tocar música de fundo: " + nomeArquivo);
        }
    }

    public static void pararMusicaFundo() {
        if (musicaFundo != null && musicaFundo.isRunning()) {
            musicaFundo.stop();
            musicaFundo.close();
        }
    }
}