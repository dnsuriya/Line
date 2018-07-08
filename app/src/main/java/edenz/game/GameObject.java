package edenz.game;
import android.graphics.Rect;

enum GameObjectType{BALL, PIPE, FIRE, MISSILE, EXPLOSION}

public abstract class GameObject {
    protected float x;
    protected float y;
    protected float dy;
    protected float dx;
    protected float width;
    protected float height;
    protected GameObjectType type;
    public void setX(float x)
    {
        this.x = x;
    }
    public void setY(float y)
    {
        this.y = y;
    }
    public float getX()
    {
        return x;
    }
    public float getY()
    {
        return y;
    }
    public float getDX() {return dx;}
    public float getDY(){return dy;}
    public void setDX(float x) {dx = x;}
    public void setDY(float y){dy = y;}
    public float getHeight()
    {
        return height;
    }
    public float getWidth()
    {
        return width;
    }

    public Rect getRectangle()
    {
        Rect rect = null;
        if (type == GameObjectType.MISSILE){
            int factW = (int)width / 4;
            int factH = (int)height / 4;
            rect = new Rect((int)x + factW, (int)y + factH, (int)x+(int)width - (2 * factW), (int)y+(int)height- (2 * factH));}
        else if (type == GameObjectType.PIPE)
        {
            int factW = (int)width / 4;
            int factH = (int)height / 4;
            rect = new Rect((int)x, (int)y, (int)x+(int)width/4, (int)y+(int)height);
        }
        else
            rect = new Rect((int)x, (int)y, (int)x+(int)width, (int)y+(int)height);

        return rect;
    }

    public GameObjectType getType() {
        return type;
    }
}