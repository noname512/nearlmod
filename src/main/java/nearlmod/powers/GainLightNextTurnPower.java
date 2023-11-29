package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GainLightNextTurnPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:GainLightNextTurnPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractPlayer p;

    public GainLightNextTurnPower(AbstractPlayer player, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = player;
        this.p = player;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, amount)));
        addToBot(new RemoveSpecificPowerAction(p, p, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new GainLightNextTurnPower(p, amount);
    }
}
