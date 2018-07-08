package edenz.game;

import java.lang.Math;

public class Vector2D {

    private float dX;
    private float dY;

    // Constructor methods ....

    public Vector2D() {
        setX(0.0f);
        setY(0.0f);
    }

    public Vector2D( float dX, float dY ) {
        this.setX(dX);
        this.setY(dY);
    }

    public Vector2D(Vector2D v)
    {
        this.setX(v.getX());
        this.setY(v.getY());
    }

    // Convert vector to a string ...

    public String toString() {
        return "Vector2D(" + getX() + ", " + getY() + ")";
    }

    // Compute magnitude of vector ....

    public float length() {
        return (float)Math.sqrt ( getX() * getX() + getY() * getY());
    }

    // Sum of two vectors ....

    public Vector2D add( Vector2D v1 ) {
        Vector2D v2 = new Vector2D( this.getX() + v1.getX(), this.getY() + v1.getY());
        return v2;
    }

    // Subtract vector v1 from v .....

    public Vector2D sub( Vector2D v1 ) {
        Vector2D v2 = new Vector2D( this.getX() - v1.getX(), this.getY() - v1.getY());
        return v2;
    }

    public  void prep()
    {
        float temp = dX;
        dX = -dY;
        dY = temp;
    }

    // Scale vector by a constant ...

    public Vector2D scale( float scaleFactor ) {
        Vector2D v2 = new Vector2D( this.getX() *scaleFactor, this.getY() *scaleFactor );
        return v2;
    }

    // Normalize a vectors length....

    public Vector2D normalize() {
        Vector2D v2 = new Vector2D();

        float length = (float) Math.sqrt( this.getX() * this.getX() + this.getY() * this.getY());
        if (length != 0) {
            v2.setX(this.getX() /length);
            v2.setY(this.getY() /length);
        }

        return v2;
    }

    // Dot product of two vectors .....

    public float dotProduct ( Vector2D v1 ) {
        return this.getX() * v1.getX() + this.getY() * v1.getY();
    }

    public static Vector2D minus(Vector2D v1, Vector2D v2)
    {
        return new Vector2D(v1.getX() - v2.getX(),v1.getY() - v2.getY());
    }

    public static float distance(Vector2D v1, Vector2D v2)
    {
        return (float)Math.sqrt ( (v1.getX() - v2.getX()) * (v1.getX() - v2.getX() ) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY() ) );
    }


    public float getX() {
        return dX;
    }

    public void setX(float dX) {
        this.dX = dX;
    }

    public float getY() {
        return dY;
    }

    public void setY(float dY) {
        this.dY = dY;
    }
}