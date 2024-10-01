import java.awt.*;
import java.awt.event.*;
import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class snakegames extends JPanel implements ActionListener , KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }

    int boardHeight;
    int boardWidth;
    int tileSize =  25 ;

    Tile snakeHead;
    ArrayList<Tile> snakeBody; 

    Tile food;
    Random random; 
    Timer gameLoop; 

    int velocitx;
    int velocity; 
    
    boolean gameOver =  false;

    snakegames(int boardHeight,int boardWidth){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardHeight,this.boardWidth));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocitx = 0;
        velocity = 0;

        gameLoop = new Timer(100,this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);  
    }
    public void draw(Graphics g){

        for(int i=0;i < boardWidth/tileSize;i++){

            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
        }

        g.setColor(Color.red);
        g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);

        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize);
   
        for(int i=0 ; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize);
        }

        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over:" + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }else{
            g.drawString("Score:" + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1 , Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    } 

    public void move(){
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }   

        for(int i=snakeBody.size()-1;i >= 0 ; i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
    
        snakeHead.x += velocitx;
        snakeHead.y += velocity; 

        for(int i=0;i<snakeBody.size();i++){
            Tile snakepart = snakeBody.get(i);

            if(collision(snakeHead,snakepart)){
                gameOver = true;
            }
        }
        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth||
           snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight){
            gameOver = true;
           }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    repaint();
    if(gameOver){
        gameLoop.stop();
    }
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_UP && velocity != 1){
        velocitx = 0;
        velocity = -1;
      }

      else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocity != -1){
        velocitx = 0;
        velocity = 1;
      }

      else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocitx != 1){
        velocitx = -1;
        velocity = 0;
      }

      else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocitx != -1){
        velocitx = 1;
        velocity = 0;
      }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

   

    @Override
    public void keyReleased(KeyEvent e) {
   
    }

}
