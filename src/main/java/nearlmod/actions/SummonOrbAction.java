package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.core.Settings;
import nearlmod.orbs.*;
import nearlmod.relics.LateLight;
import nearlmod.relics.NormalPerson;

public class SummonOrbAction extends AbstractGameAction {

    private final AbstractFriend summon;

    public SummonOrbAction(AbstractFriend summonOrb) {
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

        if (summon.ID.equals(Blemishine.ORB_ID) && AbstractDungeon.player.hasBlight(LateLight.ID)) {
            AbstractDungeon.player.getBlight(LateLight.ID).flash();
            isDone = true;
            return;
        }

        if (summon.ID.equals(Viviana.ORB_ID) && AbstractDungeon.player.hasBlight(NormalPerson.ID)) {
            AbstractDungeon.player.getBlight(NormalPerson.ID).flash();
            isDone = true;
            return;
        }

        boolean isOrbExist = false;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb.ID.equals(summon.ID)) {
                ((AbstractFriend) orb).upgrade();
                ((AbstractFriend) orb).applyStrength(summon.getTrustAmount());
                isOrbExist = true;
                break;
            }
        if (!isOrbExist) {
            switch (summon.ID) {
                case Blemishine.ORB_ID:
                    Blemishine.uniqueUsed = false;
                    break;
                case Nightingale.ORB_ID:
                    Nightingale.uniqueUsed = false;
                    break;
                case Shining.ORB_ID:
                    Shining.uniqueUsed = false;
                    break;
                case Viviana.ORB_ID:
                    Viviana.uniqueUsed = false;
                    break;
                case Whislash.ORB_ID:
                    Whislash.uniqueUsed = false;
                    break;
            }
            addToTop(new ChannelAction(summon, false));
            AbstractDungeon.player.increaseMaxOrbSlots(1, false);
        }
        isDone = true;
    }
}
