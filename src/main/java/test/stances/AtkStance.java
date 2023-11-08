package nearlmod.stances;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.NLMOD;
import nearlmod.characters.Nearl;

public class AtkStance extends AbstractStance {
    public static final String STANCE_ID = "nearlmod:AtkStance";
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);;
    public static final String NAME = stanceString.NAME;
    public static final String[] DESCRIPTION = stanceString.DESCRIPTION;
    private static int atkInc = 0;

    public AtkStance() {
        this.ID = "nearlmod:AtkStance";
        this.name = NAME;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTION[0] + atkInc + DESCRIPTION[1];
    }

    @Override
    public void onEnterStance() {
        atkInc++;
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, atkInc), atkInc));
        updateDescription();
    }

    @Override
    public void onExitStance() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, -atkInc), -atkInc));
    }
}
