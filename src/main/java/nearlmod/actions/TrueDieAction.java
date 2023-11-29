package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nearlmod.monsters.CandleKnight;

import java.util.ArrayList;

public class TrueDieAction extends AbstractGameAction {
    private CandleKnight ms;
    public TrueDieAction(CandleKnight ms) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.ms = ms;
    }

    @Override
    public void update() {
        ms.trueDie();
        isDone = true;
    }
}
