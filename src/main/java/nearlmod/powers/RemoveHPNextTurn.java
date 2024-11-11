package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.RemoveAllTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RemoveHPNextTurn extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:RemoveHPNextTurn";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractPlayer p;

    public RemoveHPNextTurn(AbstractPlayer player) {
        name = NAME;
        ID = POWER_ID;
        this.owner = player;
        this.p = player;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/gainlightnextturn power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/gainlightnextturn power 32.png"), 0, 0, 32, 32);
        type = PowerType.DEBUFF;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new RemoveAllTemporaryHPAction(p, p));
        addToBot(new RemoveSpecificPowerAction(p, p, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new RemoveHPNextTurn(p);
    }
}
