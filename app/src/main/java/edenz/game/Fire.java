package edenz.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edenz.game.*;

/**
 * Created by Nishshanka on 6/19/17.
 */

public class Fire extends GameObject {

    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Fire(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        super.type = GameObjectType.FIRE;
        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            if(i%4==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, (i-(4*row))*(int)width, row*(int)height, (int)width, (int)height);
        }
        animation.setFrames(image);
        animation.setDelay(100);



    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
    public void update()
    {
        animation.update();
    }
    public float getHeight(){return height;}
}
