package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DuelPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:Duel";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DuelPower(AbstractCreature owner,int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
        this.loadRegion("curiosity"); // TODO: 换个图案
        priority = 120;
    }
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DuelPower(owner, amount);
    }
}
