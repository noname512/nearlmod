package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.special.LightCard;

public class InfiniteLightBladePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:InfiniteLightBladePower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public InfiniteLightBladePower(AbstractCreature owner) {
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
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new MakeTempCardInHandAction(new LightCard(), 1));
    }

    @Override
    public AbstractPower makeCopy() {
        return new InfiniteLightBladePower(owner);
    }
}
