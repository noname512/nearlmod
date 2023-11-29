package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HintPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:Hint";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int HintVal;

    public HintPower(AbstractCreature owner, int HintVal) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        this.loadRegion("curiosity");
        priority = 0;
        this.HintVal = HintVal;
    }
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[HintVal];
    }

    @Override
    public AbstractPower makeCopy() {
        return new HintPower(owner, HintVal);
    }
}
