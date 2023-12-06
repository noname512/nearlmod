package nearlmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MyGiantFireEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;
    private final float brightness;
    private float x;
    private float y;
    private final float vX;
    private final float vY;
    private final boolean flipX = MathUtils.randomBoolean();
    private float delayTimer = MathUtils.random(0.1F);

    public MyGiantFireEffect(float yl, float yr, float vyl, float vyr) {
        setImg();
        startingDuration = 0.8F;
        duration = startingDuration;
        x = MathUtils.random(0.0F, (float) Settings.WIDTH) - (float)img.packedWidth / 2.0F;
        y = MathUtils.random(yl, yr) * Settings.scale - (float)img.packedHeight / 2.0F;
        vX = MathUtils.random(-70.0F, 70.0F) * Settings.scale;
        vY = MathUtils.random(vyl, vyr) * Settings.scale;
        color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        color.g -= 0.3F + MathUtils.random(0.3F);
        color.b -= 0.3F + MathUtils.random(0.3F);
        rotation = MathUtils.random(-10.0F, 10.0F);
        scale = MathUtils.random(0.5F, 3.0F);
        brightness = MathUtils.random(0.2F, 0.6F);
    }

    public void update() {
        if (delayTimer > 0.0F) {
            delayTimer -= Gdx.graphics.getDeltaTime();
        } else {
            x += vX * Gdx.graphics.getDeltaTime();
            y += vY * Gdx.graphics.getDeltaTime();
            scale *= MathUtils.random(0.95F, 1.05F);
            duration -= Gdx.graphics.getDeltaTime();
            if (duration < 0.0F || (y < 0 && vY < 0)) {
                isDone = true;
            } else if (startingDuration - duration < 0.75F) {
                color.a = Interpolation.fade.apply(0.0F, brightness, (startingDuration - duration) / 0.75F);
            } else if (duration < 1.0F) {
                color.a = Interpolation.fade.apply(0.0F, brightness, duration);
            }

        }
    }

    private void setImg() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            img = ImageMaster.FLAME_1;
        } else if (roll == 1) {
            img = ImageMaster.FLAME_2;
        } else {
            img = ImageMaster.FLAME_3;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        if (flipX && !img.isFlipX()) {
            img.flip(true, false);
        } else if (!flipX && img.isFlipX()) {
            img.flip(true, false);
        }

        sb.draw(img, x, y, (float)img.packedWidth / 2.0F, (float)img.packedHeight / 2.0F, (float)img.packedWidth, (float)img.packedHeight, scale * Settings.scale, scale * Settings.scale, rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
