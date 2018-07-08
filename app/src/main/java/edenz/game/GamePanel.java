package edenz.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;

import edenz.game.*;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player ball;
    private ArrayList<Fire> fires;
    private ArrayList<Missile> missiles;
    private int maxBorderHeight;
    private boolean newGameCreated;
    private SoundManager soundManager;

    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;

    private Explosion explosion;
    private Pipe pipe;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;
    private Line line;
    private float mx,my;
    private Random rand;
    private boolean explosionTobePlayed;
    private boolean gameJustStarted;
    private boolean gameOver;

    private Fireworks fireworks;
    private Paint fireowrkPaint;

    public GamePanel(Context context)
    {
        super(context);

        soundManager = new SoundManager(context);
        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        ball = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 80, 80, 3, 100, getHeight()- 300);
        pipe = new Pipe(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_green),50,50, WIDTH + 100, 150);
        fires = new ArrayList<Fire>();
        line = null;
        explosionTobePlayed = false;
        gameJustStarted = true;
        gameOver = false;
        fireworks = new Fireworks( getWidth(), getHeight() );

        fireowrkPaint = new Paint();
        fireowrkPaint.setStrokeWidth( 2 / getResources().getDisplayMetrics().density );
        fireowrkPaint.setColor( Color.BLACK );
        fireowrkPaint.setAntiAlias( true );

        rand = new Random(System.nanoTime());
        Bitmap fire = BitmapFactory.decodeResource(getResources(),R.drawable.fire);
        int fireCount = getWidth() / 80;
        for(int i = 0; i < getWidth();)
        {
            fires.add(new Fire(fire,i, getHeight()- 90, 90, 90, 16));
            i += 80;
        }
         missiles = new ArrayList<Missile>();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            line = new Line();
            mx = event.getX();
            my = event.getY();
            line.startLine(mx, my);

            if(!ball.getPlaying() && newGameCreated && reset)
            {
                ball.setPlaying(true);
                ball.setUp(true);
            }
            if(ball.getPlaying())
            {

                if(!started)started = true;
                reset = false;
                ball.setUp(true);
            }
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            line = new Line();
            line.startLine(mx, my);
            line.endLine(event.getX(), event.getY());
        }
        else if(event.getAction()==MotionEvent.ACTION_UP)
        {
            ball.setUp(false);
            if (line != null)
                line.setDrowned(true);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()

    {
        if(ball.getPlaying()) {

           bg.update();
            ball.update();


            //calculate the threshold of height the border can have based on the score
            //max and min border heart are updated, and the border switched direction when either max or
            //min is met

            maxBorderHeight = 30+ball.getTime()/progressDenom;
            //cap max border height so that borders can only take up a total of 1/2 the screen
            if(maxBorderHeight > HEIGHT/4)maxBorderHeight = HEIGHT/4;



            collideLine();

            //add missiles on timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000 - ball.getTime()/4)){


                //first missile always goes down the middle
                if(missiles.size()==0)
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile),WIDTH + 10, getHeight()/2, 80, 30, ball.getTime(), 13));
                }
                else
                {

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            getWidth()+10, (int)(rand.nextDouble()*(getHeight() - (maxBorderHeight * 2))+maxBorderHeight),80,30, ball.getTime(),13));
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }
            //loop through every missile and check collision and remove
            for(int i = 0; i<missiles.size();i++)
            {
                //update missile
                missiles.get(i).update();

                if(collision(missiles.get(i),ball))
                {
                    missiles.remove(i);
                    ball.setPlaying(false);
                    explosionTobePlayed = true;
                    break;
                }
                //remove missile if it is way off the screen
                if(missiles.get(i).getX()<-100)
                {
                    missiles.remove(i);
                    break;
                }
            }

            for (int i = 0; i<fires.size();i++)
            {
                //update missile
                fires.get(i).update();

                if(collision(fires.get(i),ball))
                {
                    ball.setPlaying(false);
                    explosionTobePlayed = true;
                    break;
                }
            }

            if (collision(pipe, ball))
            {
                ball.piped();
                soundManager.playPiped();
                ball.setPlaying(false);
            }



        }
        else{
            //ball.resetDY();
            line = null;
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                if (!ball.isPiped())
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),ball.getX(), ball.getY()-30, 100, 100, 25);
            }

            if (!ball.isPiped()) {

                explosion.update();
                if(explosionTobePlayed) {
                    ball.setScore(0);
                    gameOver = true;
                    soundManager.playExplosion();
                    explosionTobePlayed = false;
                }
            }


            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 10000 && !newGameCreated)
            {
                newGame();
            }

            if(gameJustStarted && resetElapsed > 1000 && !newGameCreated)
            {
                gameOver = false;
                gameJustStarted = false;
                newGame();
            }


        }

    }

    void collideLine()
    {
        if(ball.getX() + ball.getWidth()/2 > getWidth())
            ball.setDX(-4);
        if(ball.getX() - ball.getWidth()/2 < 0)
            ball.setDX(4);

        if(ball.getY() + ball.getHeight()/2 > getHeight())
            ball.setDY(-4);
        if (ball.getY() - ball.getHeight()/2 < 0)
            ball.setDY(4);

        if(line == null || !line.isDrowned())
            return;


        Vector2D first = new Vector2D(line.getStartX(), line.getStartY());
        Vector2D second = new Vector2D(line.getEndX(), line.getEndY());

        Vector2D ballPos = new Vector2D(ball.getX(), ball.getY());
        Vector2D ballVel = new Vector2D(ball.getDX(), ball.getDY());

        Vector2D wall = Vector2D.minus(second, first).scale(-1);
        wall.prep();
        Vector2D normal = wall.normalize();
        Vector2D repPos = Vector2D.minus(ballPos, first);
        double resultDot = normal.dotProduct(repPos);


        if (resultDot < 100 )
        {

            float minx = line.getStartX() - ball.getWidth(), maxx = line.getEndX() + ball.getWidth(), miny = line.getStartY() - ball.getHeight(), maxy = line.getEndY() + ball.getHeight() ;

            if(line.getStartX() > line.getEndX()) {
                minx = line.getEndX()- ball.getWidth();
                maxx = line.getStartX()+ ball.getWidth();
            }
            if(line.getStartY() > line.getEndY()) {
                miny = line.getEndY()- ball.getHeight();
                maxy = line.getStartY()+ ball.getHeight();
            }

            if (ball.getX() > maxx || ball.getX() < minx || ball.getY() < miny || ball.getY() > maxy)
            {
                System.out.println("failed");
                return;
            }

            System.out.println("passed");
            System.out.println("line : " + line.getStartX() + "," + line.getStartY() + "|" + line.getEndX() + "," + line.getEndY() + "\t\t Pos : " + ball.getX() + "," +  ball.getY());

            Vector2D newVel = Vector2D.minus(ballVel, normal.scale(2 * ballVel.dotProduct(normal)));
            ball.setDX(newVel.getX());
            ball.setDY(newVel.getY());
            soundManager.playHit();
            line = null;
        }


    }

    public Vector2D RotateRadians(Vector2D v, float radians)
    {
        float ca = (float) Math.cos(radians);
        float sa = (float) Math.sin(radians);
        return new Vector2D(ca*v.getX() - sa*v.getY(), sa*v.getX() + ca*v.getY());
    }


    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }
    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            bg.draw(canvas);
            drawText(canvas);


            canvas.restoreToCount(savedState);

            pipe.draw(canvas);

            //draw missiles
            for(Missile m: missiles)
            {
                m.draw(canvas);
            }

            //draw explosion
            if(started)
            {
                explosion.draw(canvas);
            }

            for (Fire fire : fires) {
                fire.update();
                fire.draw(canvas);
            }


            if(!dissapear) {
                ball.draw(canvas);
            }

            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(20);
            p.setColor(getResources().getColor(R.color.colorDarkGreen));
            if(line != null)
                line.draw(canvas, p);

            if (ball.isPiped())
                drawFirework(canvas);

        }
    }


    public void newGame()
    {
        dissapear = false;

        missiles.clear();


        soundManager.playBackground();
        //ball.resetDY();
        ball.reset();

        float posX = rand.nextInt(getWidth() / 2) +  50;
        float posY = rand.nextInt(getHeight()/2) + 50;
        ball.setY(getHeight()* 3 / 5);
        ball.setX(getWidth() / 8);
        ball.setPiped(false);

        if(ball.getTime()>best)
        {
            best = ball.getTime();

        }


        newGameCreated = true;
        gameOver = false;


    }

    public void drawFirework(Canvas canvas)
    {
        fireworks.doDraw( canvas , fireowrkPaint);
    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorDarkGreen));
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("TIME: " + (int)(ball.getTime() * 0.1), 30,  30, paint);
        canvas.drawText("SCORE: " + ball.getScore(), WIDTH - 155, 30, paint);

        if(gameOver && !newGameCreated)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(60);
            paint1.setColor(Color.WHITE);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("GAME OVER", WIDTH/2-190, HEIGHT/2 + 40, paint1);
        }

        if(!ball.getPlaying() && newGameCreated && reset)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setColor(Color.WHITE);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2 + 40, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND MOVE TO DRAW LINE", WIDTH/2-50, HEIGHT/2 + 60, paint1);
            canvas.drawText("RELEASE TO END IT", WIDTH/2-50, HEIGHT/2 + 80, paint1);
        }
    }


}