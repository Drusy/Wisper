package fr.wisper.dialog;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class SpeechBubble extends TextBox {

    private boolean usePic = false;
    private Sprite pic;

    private float picOffset;

    public SpeechBubble(NinePatch ninePatch, BitmapFont font) {
        super(font);
        background = ninePatch;
    }

    public void init(String text, float x, float y){
        setUp(text, x, y);
        animate();
    }

    public void init(String text, float x, float y, TextureRegion pic){
        setUp(text, x, y);
        addPic(pic);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        background.draw(batch, getX(), getY(), getWidth(), getHeight());

        if (!usePic){

            font.drawWrapped(batch,
                    text,
                    getX() + padX, getY() + getHeight() - padY, wrapWidth);
        }
        else{
            batch.draw(pic, getX() + padX, getY() + picOffset, pic.getWidth(), pic.getHeight());
            font.drawWrapped(batch,
                    text,
                    getX() + padX*2 + pic.getWidth(), getY() + getHeight() - padY, wrapWidth);

        }
    }

    private void addPic(TextureRegion texture) {

        usePic = true;
        pic = new Sprite(texture);
        pic.setSize(80, 80);

        if (getHeight() < 100)
            setHeight(100);

        picOffset = getHeight() - 80 - padX;

        setWidth(getWidth() + pic.getWidth() + padX);

        animate();
    }

    @Override
    public void reset() {
        super.reset();

        usePic = false;
    }

    @Override
    void animate() {
        addAction(Actions.sequence(Actions.sizeTo(getWidth(), getHeight()*0.75f), Actions.sizeTo(getWidth(), getHeight(), 0.20f, Interpolation.bounceOut)));
    }

    public void dispose() {
        if (pic != null) {
            pic.getTexture().dispose();
        }
    }
}