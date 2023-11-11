package nearlmod.stances;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.stances.AbstractStance;

public class DefStance extends AbstractStance {
    public static final String STANCE_ID = "nearlmod:DefStance";
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    public static final String NAME = stanceString.NAME;
    public static final String[] DESCRIPTION = stanceString.DESCRIPTION;
    public static int defInc = 0;
    public static int incNum = 1;

    public DefStance() {
        this.ID = "nearlmod:DefStance";
        this.name = NAME;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTION[0] + defInc + DESCRIPTION[1] + incNum + DESCRIPTION[2];
    }

    public static void upgradeIncNum(int updateNum) {
        incNum += updateNum;
    }

    @Override
    public void onEnterStance() {
        defInc+=incNum;
        AbstractPlayer p = AbstractDungeon.player;
        if (defInc != 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, defInc), defInc));
        }
        updateDescription();
    }

    @Override
    public void onExitStance() {
        AbstractPlayer p = AbstractDungeon.player;
        if (defInc != 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, -defInc), -defInc));
        }
    }
}
