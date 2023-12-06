package nearlmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class GlimmeringTouchEffect extends AbstractGameEffect {
    private float timer = 0.0F;
    private final float fallDuration;

    public GlimmeringTouchEffect() {
        duration = 2.5F;
        startingDuration = duration;
        fallDuration = 0.8F;
    }

    @Override
    public void update() {
        if (duration == startingDuration) {
            CardCrawlGame.sound.play("GHOST_FLAMES");
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.FIREBRICK));
        }

        duration -= Gdx.graphics.getDeltaTime();
        timer -= Gdx.graphics.getDeltaTime();
        if (timer < 0.0F) {
            if (duration > startingDuration - fallDuration) {
                for (int i = 0; i < 8; i++)
                    AbstractDungeon.effectsQueue.add(new MyGiantFireEffect(Settings.HEIGHT + 100.0F, Settings.HEIGHT + 300.0F, -1200F, -2000F));
            } else {
                for (int i = 0; i < 8; i++)
                    AbstractDungeon.effectsQueue.add(new MyGiantFireEffect(-400.0F, -200.0F, 500F, 800F));
            }
            timer = 0.05F;
        }

        if (duration < 0.0F) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {}
}
