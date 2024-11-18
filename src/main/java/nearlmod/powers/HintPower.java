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

    int content;

    public HintPower(AbstractCreature owner, int content) {
        this(owner);
        this.content = content;
    }

    public HintPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        this.loadRegion("curiosity");
        priority = 0;
    }
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[content];
    }

    @Override
    public AbstractPower makeCopy() {
        return new HintPower(owner);
    }
}
