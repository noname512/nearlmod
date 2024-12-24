package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.core.Settings;
import nearlmod.orbs.*;
import nearlmod.relics.LateLight;
import nearlmod.relics.NormalPerson;
import nearlmod.relics.OldRules;

public class SummonFriendAction extends AbstractGameAction {

    private final AbstractFriend summon;

    public SummonFriendAction(AbstractFriend summonOrb) {
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

        if (summon.ID.equals(Blemishine.ORB_ID) && AbstractDungeon.player.hasRelic(LateLight.ID)) {
            AbstractDungeon.player.getRelic(LateLight.ID).flash();
            isDone = true;
            return;
        }

        if (summon.ID.equals(Viviana.ORB_ID) && AbstractDungeon.player.hasRelic(NormalPerson.ID)) {
            AbstractDungeon.player.getRelic(NormalPerson.ID).flash();
            isDone = true;
            return;
        }

        if (summon.ID.equals(Penance.ORB_ID) && AbstractDungeon.player.hasRelic(OldRules.ID)) {
            AbstractDungeon.player.getRelic(OldRules.ID).flash();
            isDone = true;
            return;
        }

        boolean isOrbExist = false;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if ((orb instanceof AbstractFriend) && (orb.ID.equals(summon.ID))) {
                ((AbstractFriend) orb).upgrade();
                ((AbstractFriend) orb).applyStrength(summon.getTrustAmount());
                isOrbExist = true;
                break;
            }
        if (!isOrbExist) {
            summon.uniqueUsed = false;
            addToTop(new ChannelAction(summon, false));
            AbstractDungeon.player.increaseMaxOrbSlots(1, false);
            if (AbstractDungeon.player.hasRelic("nearlmod:RatSwarm")) {
                AbstractDungeon.player.getRelic("nearlmod:RatSwarm").flash();
                addToBot(new GainBlockAction(AbstractDungeon.player, 5));
            }
        }
        for (AbstractCard card: AbstractDungeon.player.hand.group) {
            card.applyPowers();
        }
        isDone = true;
    }
}
