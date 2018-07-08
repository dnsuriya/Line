package edenz.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Nishshanka on 6/19/17.
 */

public class Pipe extends GameObject {
    private Bitmap image;

    public Pipe(Bitmap res, int w, int h, int px, int py)
    {
        image = res;
        height = h;
        width = w;
        x = px;
        y = py;
        super.type = GameObjectType.PIPE;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image,x,y,null);
    }
}
