package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ColdPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:Cold";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ColdPower(AbstractCreature owner,int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.DEBUFF;
        updateDescription();
        this.loadRegion("curiosity");
        priority = 0;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= 2) {
            amount = 2;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if ((owner instanceof AbstractMonster) /* && ((AbstractMonster)owner.isMultiDmg) */) // TODO: 这咋是private
        {
            return damage * (1.0F - 0.25F * amount);
        }
        return damage;
    }

    @Override
    public AbstractPower makeCopy() {
        return new ColdPower(owner, amount);
    }
}
