package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.characters.Nearl;
import nearlmod.monsters.LastKheshig;
import nearlmod.orbs.Blemishine;

public class RemoveLastFriendAction extends AbstractGameAction {

    public RemoveLastFriendAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (((Nearl)AbstractDungeon.player).removeLastFriend().equals(Blemishine.ORB_ID))
            LastKheshig.isBlemishineSurvive = false;
        isDone = true;
    }
}
