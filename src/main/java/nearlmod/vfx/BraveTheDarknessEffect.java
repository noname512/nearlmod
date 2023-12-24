package nearlmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BraveTheDarknessEffect extends AbstractGameEffect {
    private final float tx;
    private final float ty;
    private boolean played;
    public final float stage1;
    public final float stage2;
    public final float stage3;

    public BraveTheDarknessEffect(float tx, float ty) {
        stage1 = 1.5F;
        stage2 = 1.0F;
        stage3 = 0.5F;
        duration = stage1 + stage2 + stage3;
        this.tx = tx;
        this.ty = ty;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        float x, y;
        if (duration > stage2 + stage3) {
            x = 0.0F;
            y = 0.0F;
        } else if (duration > stage3) {
            x = tx;
            y = ty;
            if (duration < stage3 + 0.7F && !played) {
                played = true;
                CardCrawlGame.sound.play("BRAVE_THE_DARKNESS");
            }
        } else {
            x = tx * duration / stage3;
            y = ty * duration / stage3;
        }
        AbstractDungeon.player.animX = x;
        AbstractDungeon.player.animY = y;
        if (duration < 0.0F) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {}

    @Override
    public void dispose() {}
}
