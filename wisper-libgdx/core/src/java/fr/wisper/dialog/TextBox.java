package fr.wisper.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer;

public abstract class TextBox extends Actor implements Poolable {

    protected NinePatch background;
    protected Timer timer;
    protected Actor followed;
    protected boolean alive = false, follow = false;

    protected String text;
    protected BitmapFont font;

    protected float padX = 10, padY = 20;
    protected float readSpeed = 0.1f;

    protected float yStretch;
    protected float wrapWidth;


    public TextBox(BitmapFont font) {
        this.font = font;
        timer = new Timer();
    }

    public void init(String text, float x, float y, BitmapFont font){
        setUp(text, x, y);
        animate();
    }

    protected void setUp(String text, float x, float y) {
        alive  = true;
        this.text = text;

        float sbWidth = font.getBounds(text).width + padX*2,
                sbHeight = font.getBounds(text).height + padY*2;

        if (sbWidth < 50)
            sbWidth = 50;

        yStretch = (float) Math.ceil(0.5f+(sbWidth/((float)Gdx.app.getGraphics().getWidth()/2f)));

        if (sbWidth > Gdx.app.getGraphics().getWidth()/2){
            sbWidth =  Gdx.app.getGraphics().getWidth()/2 + padX*2;
            sbHeight = font.getBounds(text).height*yStretch + padY*yStretch;
        }

        setWidth(sbWidth);
        setHeight(sbHeight);

        wrapWidth = getWidth() - padX*2;

        setOrigin(getWidth()/2, getHeight()/2);

        setX(x - getWidth()/2);
        setY(y);

        setLife(1.5f + (float)text.length() * readSpeed);
    }

    public void setLife(float time){
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                alive = false;
            }
        }, time);
    }

    @Override
    public void setColor(Color color){
        background.setColor(color);
    }

    public void setPadding(float padX, float padY){
        this.padX = padX;
        this.padY = padY;
    }

    public void setFont(BitmapFont font){
        this.font = font;

    }
    public void setFollow(Actor actor) {
        follow = true;
        followed = actor;
        checkBounds();
    }
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        checkBounds();
    }
    protected void checkBounds() {

        if (follow){
            setX(followed.getX() - getWidth()/2);
            setY(followed.getY() + followed.getHeight());
        }

        if (getX() + getWidth() > Gdx.app.getGraphics().getWidth())
            setX(getX() - (getWidth() - (Gdx.app.getGraphics().getWidth() - getX())));

        if (getY() + getHeight() > Gdx.app.getGraphics().getHeight())
            setY(getY() - (getHeight() - (Gdx.app.getGraphics().getHeight() - getY())));


        if (getX() < 0)
            setX(0);
        if (getY() < 0)
            setY(0);
    }

    abstract void animate();

    @Override
    public void reset() {
        alive = false;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
        wrapWidth = 0;
        text = "";
        follow = false;
        followed = null;

    }
}
