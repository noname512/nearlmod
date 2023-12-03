package nearlmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import nearlmod.cards.friendcards.AbstractFriendCard;

import java.util.ArrayList;

public abstract class AbstractFriend extends CustomOrb {
    public boolean upgraded;
    protected static float MY_X_OFFSET = 50.0F * Settings.scale;
    protected static float MY_Y_OFFSET = 20.0F * Settings.scale;
    protected int trustAmount;
    private final String[] DESCRIPTION;
    public boolean flipHorizontal;
    public AbstractFriend(String ID, String NAME, String[] DESCRIPTION, String imgPath, int amount) {
        super(ID, NAME, 0, 0, "", "", imgPath);
        trustAmount = amount;
        showEvokeValue = false;
        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
        this.DESCRIPTION = DESCRIPTION;
        flipHorizontal = AbstractDungeon.player.flipHorizontal;
        updateDescription();
    }

    public void applyStrength(int amount) {
        trustAmount += amount;
        updateDescription();
    }

    public void upgrade() {
        upgraded = true;
        applyStrength(2);
    }

    public int getTrustAmount() {return trustAmount;}

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
    public void updateDescription() {
        if (DESCRIPTION == null)
            description = "";
        else
            description = DESCRIPTION[upgraded? 2 : 0] + trustAmount + DESCRIPTION[1];
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.trustAmount), this.cX + MY_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + MY_Y_OFFSET, this.c, this.fontScale);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.c);
            sb.draw(this.img, this.cX - (float)this.img.getWidth() / 2.0F + this.bobEffect.y / 4.0F, this.cY - (float)this.img.getHeight() / 2.0F + this.bobEffect.y / 4.0F, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale, this.scale, 0.0F, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, false);
        }

        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void updateAnimation() {
        this.bobEffect.update();
        this.cX = MathHelper.orbLerpSnap(this.cX, this.tX);
        this.cY = MathHelper.orbLerpSnap(this.cY, this.tY);
        if (this.channelAnimTimer != 0.0F) {
            this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
            if (this.channelAnimTimer < 0.0F) {
                this.channelAnimTimer = 0.0F;
            }
        }

        this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
        this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
    }

    @Override
    public void playChannelSFX() {}

    protected static AbstractFriendCard getRandomCard(ArrayList<AbstractFriendCard> cards, boolean upgraded) {
        int random = AbstractDungeon.cardRng.random(0, cards.size() - 1);
        AbstractFriendCard c = cards.get(random);
        if (upgraded)
            c.upgrade();
        return c;
    }
}
