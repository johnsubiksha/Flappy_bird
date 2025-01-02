import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class flappyBird extends JPanel implements ActionListener,KeyListener{
    int width = 978;
    int height = 550;

    Image backGroundImage;
    Image pipeTopImage;
    Image PipeBottomImage;
    Image birdImage;

    int birdX = width/8;
    int birdY = height/2;
    int birdWidth = 44;
    int birdHeight = 34;

    int pipeX = width;
    int pipeY =0;
    int pipeWidth =64;
    int pipeHeight = 512;
    int velocity_X = -4;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    int velocity_Y = 0;
    int gravity =1;

    Timer gameLoop;
    Timer pipeTimer;
    boolean gameOver = false;
    double score =0;

    class Pipe{
        int X = pipeX;
        int Y = pipeY;
        int p_width = pipeWidth;
        int p_Height = pipeHeight;
        Image img;
        boolean passed =false;

        Pipe(Image img){
            this.img = img;
        }
    }

    flappyBird()
    {
        setPreferredSize(new Dimension(width,height));
        setFocusable(true);
        addKeyListener(this);

        backGroundImage = new ImageIcon(getClass().getResource("./assets/background.jpg")).getImage();
        pipeTopImage = new ImageIcon(getClass().getResource("./assets/top_pipe.png")).getImage();
        PipeBottomImage = new ImageIcon(getClass().getResource("./assets/bottom_pipe.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./assets/bird_img.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipeTimer = new Timer(1500,new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    placepipe();
                }
        });
        pipeTimer.start();


        
        gameLoop = new Timer(1000/60,this);
        gameLoop.start();
         
    }

    public void placepipe(){
        //(0-1) * pipeHeight/2 -> 256 (0-256)
        //0-128 -(0-256)
        int rand = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));

        int openingSpace = height/4;

        Pipe toppipe = new Pipe(pipeTopImage);
        toppipe.Y = rand;
        pipes.add(toppipe);

        Pipe bottomPipe = new Pipe(PipeBottomImage);
        bottomPipe.Y = toppipe.Y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        g.drawImage(backGroundImage,0,0,width,height,null);
        g.drawImage(birdImage, birdX, birdY,birdWidth,birdHeight,null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe p =pipes.get(i);
            g.drawImage(p.img, p.X, p.Y, p.p_width,p.p_Height,null);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver)
        {
            g.drawString("Game Over",width/3 +8 ,height/3 + 50);
            g.drawString("Score : "+String.valueOf((int)score),width/3+10 +8 ,height/2);
        }
        else{
            g.drawString("score: "+String.valueOf((int)score),10,35);
        }

    }

    public void move()
    {
        velocity_Y += gravity;
        birdY += velocity_Y;
        birdY = Math.max(birdY,0);

        for(int i=0;i<pipes.size();i++)
        {
            Pipe p = pipes.get(i);
            p.X += velocity_X;

            if(!p.passed && birdX > p.X+p.p_width)
            {
                p.passed = true;
                score += 0.5;
            }
            if(Collision(p)){
                gameOver = true;
            }
        }
        if(birdY > height)
        {
            gameOver =true;
        }
    }

    public boolean Collision(Pipe b)
    {
        return birdX < b.X+birdWidth && //birds's top leftcorner doesn't reach pipe's top right corner
         birdWidth+birdX >b.X && //birds's top rightcorner doesn't reach pipe's top left corner
         birdY < b.Y+b.p_Height && //birds's top leftcorner doesn't reach pipe's top left corner
          birdY + birdHeight > b.Y; //birds's top rightcorner doesn't reach pipe's top right corner
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if(gameOver)
        {
            pipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!gameOver){
            velocity_Y = -9;
        }

        if(gameOver)
        {
            birdY = height/2;
            velocity_Y = 0;
            pipes.clear();
            score =0;
            gameOver = false;
            gameLoop.start();
            pipeTimer.start();
        }
       }

    @Override
    public void keyReleased(KeyEvent e) {
           }

    @Override
    public void keyTyped(KeyEvent e) {
       }

}
