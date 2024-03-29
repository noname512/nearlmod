package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LoseThornsPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:LoseThornsPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LoseThornsPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/losethorns power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/losethorns power 32.png"), 0, 0, 32, 32);
        type = PowerType.DEBUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        if (owner.hasPower("Thorns")) {
            if (!owner.hasPower("Artifact")) {
                if (owner.getPower("Thorns").amount <= this.amount) {
                    addToBot(new RemoveSpecificPowerAction(owner, owner, "Thorns"));
                } else {
                    owner.getPower("Thorns").stackPower(-amount);
                }
            } else {
                owner.getPower("Artifact").onSpecificTrigger();
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new LoseThornsPower(owner, amount);
    }
}
