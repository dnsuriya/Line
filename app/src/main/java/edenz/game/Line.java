package edenz.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Nishshanka on 6/20/17.
 */

public class Line {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Path path;
    private Paint paint;
    private boolean drowned;

    public Line()
    {

    }

    public void startLine(float x, float y)
    {
        startX = x;
        startY = y;
        path = new Path();
        path.moveTo(startX,startY);
        setDrowned(false);
    }

    public void endLine(float x, float y)
    {
        endX = x;
        endY = y;
        path.lineTo(endX,endY);
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public void draw(Canvas canvas, Paint p)
    {
        canvas.drawPath(path, p);
    }

    public boolean isDrowned() {
        return drowned;
    }

    public void setDrowned(boolean drowned) {
        this.drowned = drowned;
    }
}
