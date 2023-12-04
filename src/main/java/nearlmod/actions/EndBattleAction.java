package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EndBattleAction extends AbstractGameAction {

    public EndBattleAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (!isDone)
        {
            isDone = true;
            AbstractDungeon.getCurrRoom().endBattle();
        }
    }
}
