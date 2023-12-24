package nearlmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import nearlmod.actions.RemoveLastFriendAction;

public class CoalescingFearEffect extends AbstractGameEffect {
    private final float sx;
    private final float sy;
    private final float tx;
    private final float ty;
    private final float scale;
    private float x;
    private float y;
    private float ang;
    private float alpha;
    private float currentScale;
    private float stayDuration;
    public static final Texture img = new Texture("images/vfx/coalescingfear.png");
    public static final TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(img, 0, 0, img.getWidth(), img.getHeight());

    public CoalescingFearEffect(float sx, float sy, float tx, float ty, float scale) {
        duration = 1.0F;
        startingDuration = duration;
        stayDuration = 0.8F;
        this.sx = sx;
        this.sy = sy;
        this.tx = tx;
        this.ty = ty;
        this.scale = scale * Settings.scale;
    }

    @Override
    public void update() {
        if (duration < 0.0F) {
            stayDuration -= Gdx.graphics.getDeltaTime();
            x = tx;
            y = ty;
            alpha = 1.0F;
            currentScale = scale;
            ang = 0.0F;
            if (stayDuration < 0.0F) {
                isDone = true;
            }
            return;
        }
        duration -= Gdx.graphics.getDeltaTime();
        alpha = 1 - duration / startingDuration;
        x = tx + (sx - tx) * duration / startingDuration;
        y = ty + (sy - ty) * duration / startingDuration;
        currentScale = scale * (1 - duration / startingDuration);
        ang = 720.0F * (1 - duration / startingDuration);
        while (ang > 360.0F) ang -= 360.0F;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(1.0F, 1.0F, 1.0F, alpha);
        sb.draw(region, x, y, region.packedWidth / 2.0F, region.packedHeight / 2.0F, region.packedWidth, region.packedHeight, currentScale, currentScale, ang);
    }

    @Override
    public void dispose() {}
}
