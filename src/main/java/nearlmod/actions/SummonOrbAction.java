package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.core.Settings;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Nightingale;

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

        boolean isOrbExist = false;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb.ID.equals(summon.ID)) {
                ((AbstractFriend) orb).upgrade();
                isOrbExist = true;
                break;
            }
        if (!isOrbExist) {
            AbstractDungeon.actionManager.addToTop(new ChannelAction(summon, false));
            AbstractDungeon.player.increaseMaxOrbSlots(1, false);
        }
        isDone = true;
    }
}
