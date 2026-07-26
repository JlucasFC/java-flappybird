import javax.swing.*;

public class App {
  public static void main(String[] args) {
    int larg = 360;
    int altura = 640;

    JFrame janela = new JFrame("Flappy bird");
    janela.setSize(larg, altura);
    janela.setLocationRelativeTo(null);
    janela.setResizable(false);
    janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    FlappyBird flappyBird = new FlappyBird();
    janela.add(flappyBird);
    janela.pack();
    flappyBird.requestFocus();
    janela.setVisible(true);
  }
}
