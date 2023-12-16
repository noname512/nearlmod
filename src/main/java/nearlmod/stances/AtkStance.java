package nearlmod.stances;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.SpecialApplyPowerAction;

public class AtkStance extends AbstractStance {
    public static final String STANCE_ID = "nearlmod:AtkStance";
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    public static final String NAME = stanceString.NAME;
    public static final String[] DESCRIPTION = stanceString.DESCRIPTION;
    public static int atkInc = 0;
    public static int incNum = 1;
    public static boolean keepVal = false;

    public AtkStance() {
        this.ID = "nearlmod:AtkStance";
        this.name = NAME;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTION[0] + atkInc + DESCRIPTION[1] + incNum + DESCRIPTION[2];
    }
    
    public static void upgradeIncNum(int updateNum) {
        incNum += updateNum;
        AbstractDungeon.player.stance.updateDescription();
    }

    @Override
    public void onEnterStance() {
        atkInc += incNum;
        AbstractPlayer p = AbstractDungeon.player;
        if (atkInc != 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, atkInc), atkInc));
        }
        updateDescription();
    }

    @Override
    public void onExitStance() {
        AbstractPlayer p = AbstractDungeon.player;
        if (atkInc == 0) {
            keepVal = false;
            return;
        }
        if (keepVal) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, atkInc)));
            keepVal = false;
        } else {
            AbstractDungeon.actionManager.addToTop(new SpecialApplyPowerAction(p, p, new StrengthPower(p, -atkInc), -atkInc));
        }
    }
}
