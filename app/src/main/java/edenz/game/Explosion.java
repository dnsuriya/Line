package edenz.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edenz.game.*;

public class Explosion  extends GameObject {
    private float x;
    private float y;
    private float width;
    private float height;
    private float row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Explosion(Bitmap res, float x, float y, float w, float h, int numFrames)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        super.type = GameObjectType.EXPLOSION;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            if(i%5==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, (int)(i-(5*row))*(int)width, (int)row*(int)height,(int) width,(int) height);
        }
        animation.setFrames(image);
        animation.setDelay(10);



    }
    public void draw(Canvas canvas)
    {
        if(!animation.playedOnce())
        {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }

    }
    public void update()
    {
        if(!animation.playedOnce())
        {
            animation.update();
        }
    }
    public float getHeight(){return height;}
}