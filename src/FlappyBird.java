import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

  int larg = 360;
  int altura = 640;

  // Imagens
  Image birdImage;
  Image backgroundImage;
  Image bottomPipeImage;
  Image topPipeImage;

  // Passaro
  int birdX = larg / 8;
  int birdY = altura / 2;
  int birdWidth = 34;
  int birdHeight = 24;

  public class Bird {
    int x = birdX;
    int y = birdY;
    int widht = birdWidth;
    int height = birdHeight;
    Image img;

    Bird(Image img) {
      this.img = img;
    }
  }

  // Canos
  int pipeX = larg;
  int pipeY = 0;
  int pipeWidth = 64; // escala 1/6
  int pipeHeight = 512;

  public class Pipe {
    int x = pipeX;
    int y = pipeY;
    int widht = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe(Image img) {
      this.img = img;
    }
  }

  // Logica
  Bird bird;
  int velocityX = -4;
  int velocityY = 0;
  int gravity = 1;
  boolean gamerOver = false;

  ArrayList<Pipe> pipes;
  Random random = new Random();
  Timer gameloop;
  Timer placesPipesTimer;
  double score = 0;

  FlappyBird() {
    setPreferredSize(new Dimension(larg, altura));
    setFocusable(true);
    addKeyListener(this);

    backgroundImage = new ImageIcon(getClass().getResource("./assets/flappybirdbg.png")).getImage();
    birdImage = new ImageIcon(getClass().getResource("./assets/flappybird.png")).getImage();
    bottomPipeImage = new ImageIcon(getClass().getResource("./assets/bottompipe.png")).getImage();
    topPipeImage = new ImageIcon(getClass().getResource("./assets/toppipe.png")).getImage();

    bird = new Bird(birdImage);
    pipes = new ArrayList<Pipe>();
    placesPipesTimer = new Timer(1500, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        placePipes();
      }
    });
    placesPipesTimer.start();
    gameloop = new Timer(1500 / 60, this);
    gameloop.start();
  }

  public void placePipes() {
    int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
    int openingSpace = altura / 4;
    Pipe topPipe = new Pipe(topPipeImage);
    topPipe.y = randomPipeY;
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipeImage);
    bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
    pipes.add(bottomPipe);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    g.drawImage(backgroundImage, 0, 0, larg, altura, null);
    g.drawImage(birdImage, bird.x, bird.y, bird.widht, bird.height, null);
    for (int i = 0; i < pipes.size(); i++) {
      Pipe pipe = pipes.get(i);
      g.drawImage(pipe.img, pipe.x, pipe.y, pipe.widht, pipe.height, null);
    }

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 32));
    if (gamerOver) {
      g.drawString("GameOver: " + String.valueOf((int) score), 10, 35);
    } else {
      g.drawString(String.valueOf((int) score), 10, 35);
    }
  }

  public void move() {
    // bird
    velocityY += gravity;
    bird.y += velocityY;
    bird.y = Math.max(bird.y, 0);

    for (int i = 0; i < pipes.size(); i++) {
      Pipe pipe = pipes.get(i);
      pipe.x += velocityX;

      if (bird.y > altura) {
        gamerOver = true;
      }
      if (collsion(bird, pipe)) {
        gamerOver = true;
      }
      if (!pipe.passed && bird.x > pipe.x + pipe.widht) {
        pipe.passed = true;
        score += 0.5;
      }

    }

  }

  public boolean collsion(Bird a, Pipe b) {
    return a.x < b.x + b.widht &&
        a.x + a.widht > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height > b.y;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (gamerOver) {
      gameloop.stop();
      placesPipesTimer.stop();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      velocityY += -12;

      if (gamerOver) {
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gamerOver = false;
        gameloop.start();
        placesPipesTimer.start();
      }
    }
  }
}
