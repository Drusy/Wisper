package fr.wisper.entities;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;
import fr.wisper.animations.wisper.WisperAnimation;
import fr.wisper.utils.Config;

import java.util.*;

public class AnimatedWisper extends Wisper {
    private List<WisperAnimation> animations = new ArrayList<WisperAnimation>();
    private Random random = new Random();
    private Timer animationTimer = new Timer();
    private TimerTask animationTimerTask;

    public AnimatedWisper(String particleFile) {
        super(particleFile);

        initRightLeftAnimation();
        initTopBottomAnimation();
    }

    public void animate(final TweenManager tweenManager) {
        scheduleAnimation(tweenManager, Config.WISPER_TIME_FIRST_ANIMATIONS);
    }

    private void scheduleAnimation(final TweenManager tweenManager, final long delay) {
        animationTimerTask = new TimerTask() {
            @Override
            public void run() {
                // Start animation
                animations.get(random.nextInt(animations.size())).animate(tweenManager);

                // Schedule next Animation
                scheduleAnimation(tweenManager, Config.WISPER_TIME_BETWEEN_ANIMATIONS);
            }
        };
        animationTimer.schedule(animationTimerTask, delay);
    }

    private void initRightLeftAnimation() {
        animations.add(new WisperAnimation() {
            @Override
            public void animate(final TweenManager tweenManager) {
                final int baseX = (int)getX();
                final int baseY = (int)getY();
                final double duration = 2;
                final int offset = 100;
                final TweenEquation equation = Cubic.INOUT;

                final TweenCallback resetPositionCallback = new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        moveToWithDuration(baseX, baseY, tweenManager, duration, equation, null);
                    }
                };

                TweenCallback moveRightCallBack = new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        moveToWithDuration(baseX + offset, baseY, tweenManager, duration, equation, resetPositionCallback);
                    }
                };

                // First move left, then right callback, then reset
                moveToWithDuration(baseX - offset, baseY, tweenManager, duration, equation, moveRightCallBack);
            }
        });
    }

    public void initTopBottomAnimation() {

        animations.add(new WisperAnimation() {
            @Override
            public void animate(final TweenManager tweenManager) {
                final int baseX = (int)getX();
                final int baseY = (int)getY();
                final double duration = 2;
                final int offset = 100;
                final TweenEquation equation = Cubic.INOUT;

                final TweenCallback resetPositionCallback = new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        moveToWithDuration(baseX, baseY, tweenManager, duration, equation, null);
                    }
                };

                TweenCallback moveRightCallBack = new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        moveToWithDuration(baseX, baseY + offset, tweenManager, duration, equation, resetPositionCallback);
                    }
                };

                // First move left, then right callback, then reset
                moveToWithDuration(baseX, baseY - offset, tweenManager, duration, equation, moveRightCallBack);
            }
        });

    }
}
