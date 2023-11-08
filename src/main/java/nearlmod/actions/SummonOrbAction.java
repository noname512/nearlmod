package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.core.Settings;

public class SummonOrbAction extends AbstractGameAction {

    private AbstractOrb summon;

    public SummonOrbAction(AbstractOrb summonOrb) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        summon = summonOrb;
    }
    @Override
    public void update() {
        if (summon == null) {
            isDone = true;
            return;
        }

        AbstractDungeon.actionManager.addToTop(new ChannelAction(summon, false));
        AbstractDungeon.player.increaseMaxOrbSlots(1, false);
        isDone = true;
    }
}
