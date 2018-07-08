package edenz.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edenz.game.*;


public class Player extends GameObject{
    private Bitmap spritesheet;
    private int time;
    private int score;

    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private boolean piped;

    public Player(Bitmap res, int w, int h, int numFrames, int px, int py) {

        x = px;
        y = py;
        dy = 5;
        dx = 0;
        time = 0;
        height = h;
        width = w;
        setScore(0);
        piped = false;
        super.type = GameObjectType.BALL;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*(int)width, 0, (int)width, (int)height);
        }

        animation.setFrames(image);
        animation.setDelay(200);
        startTime = System.nanoTime();

    }

    public void setUp(boolean b){up = b;}

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            time++;
            startTime = System.nanoTime();
        }
        animation.update();

       /* if(up){
            dy -=1;

        }
        else{
            dy +=1;
        }

        if(dy>14)dy = 14;
        if(dy<-14)dy = -14;

        y += dy*2;*/

        x += dx;
        y += dy;

    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getTime(){return time;}
    public int getScore(){return score;}
    public void piped() {
        setPiped(true);
        setScore(score + 1);
    }
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDY(){dy = 0;}
    public void reset()
    {
        time = 0;
    }

    public boolean isPiped() {
        return piped;
    }

    public void setPiped(boolean piped) {
        this.piped = piped;
    }

    public void setScore(int score) {
        this.score = score;
    }
}