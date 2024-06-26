package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CreedFieldPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:CreedFieldPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int DAMAGE_DEC = 6;

    public CreedFieldPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/creedfield power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/creedfield power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        priority = 100;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DAMAGE_DEC + DESCRIPTIONS[3];
        else
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DAMAGE_DEC + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurn() {
        amount--;
        updateDescription();
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType damageType) {
        if (damage > DAMAGE_DEC)
            return damage - DAMAGE_DEC;
        else
            return 0;
    }

    @Override
    public AbstractPower makeCopy() {
        return new CreedFieldPower(owner, amount);
    }
}
