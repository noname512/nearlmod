package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class CorrosionDamagePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:CorrosionDamagePower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean inBreak;

    public CorrosionDamagePower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/corrosiondamage power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/corrosiondamage power 32.png"), 0, 0, 32, 32);
        inBreak = false;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        if (inBreak) return;
        amount += stackAmount;
        if (amount >= 1000) {
            flash();
            inBreak = true;
            owner.damage(new DamageInfo(null, 16, DamageInfo.DamageType.THORNS));
            addToTop(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -2)));
            amount = 0;
        }
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (inBreak)
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        if (inBreak) description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        else description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CorrosionDamagePower(owner, amount);
    }
}
