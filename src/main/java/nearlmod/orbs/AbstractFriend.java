package nearlmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;

public abstract class AbstractFriend extends CustomOrb {
    public boolean upgraded;
    protected static float MY_X_OFFSET = 50.0F * Settings.scale;
    protected static float MY_Y_OFFSET = 20.0F * Settings.scale;
    public AbstractFriend(String ID, String NAME, int basePassiveAmount, int baseEvokeAmount, String passiveDescription, String evokeDescription, String imgPath) {
        super(ID, NAME, basePassiveAmount, baseEvokeAmount, passiveDescription, evokeDescription, imgPath);

        showEvokeValue = false;
        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
    }

    public void applyStrength(int amount) {
        passiveAmount += amount;
    }

    public void upgrade() {
        upgraded = true;
        applyStrength(2);
    }

    @Override
    public void onEvoke() {}

    @Override
    public void applyFocus() {}

    @Override
    public void setSlot(int slotNum, int maxOrbs) {
        float dist = 200.0F * Settings.scale + (float)maxOrbs * 10.0F * Settings.scale;
        float angle = 100.0F + (float)maxOrbs * 12.0F;
        float offsetAngle = angle / 2.0F;
        angle *= (float)slotNum / ((float)maxOrbs - 1.0F);
        angle += 90.0F - offsetAngle;
        this.tX = dist * MathUtils.cosDeg(angle) + AbstractDungeon.player.drawX;
        this.tY = dist * MathUtils.sinDeg(angle) + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F;
        if (maxOrbs == 1) {
            this.tX = AbstractDungeon.player.drawX;
            this.tY = 200.0F * Settings.scale + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F;
        }

        this.hb.move(this.tX, this.tY);
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + MY_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + MY_Y_OFFSET, this.c, this.fontScale);
    }

//    @Override
//    public void updateAnimation() {}

    @Override
    public void playChannelSFX() {}
}
