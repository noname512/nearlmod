package nearlmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ExhumeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Gummy;

import java.util.ArrayList;

public class AnswerTheCallAction extends AbstractGameAction {
    public AnswerTheCallAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        ArrayList<AbstractFriendCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if ((c instanceof AbstractFriendCard) && (((AbstractFriendCard)c).belongFriend.equals(Gummy.ORB_ID) && (c.type == AbstractCard.CardType.ATTACK))) {
                list.add((AbstractFriendCard)c);
            }
        }
        for (AbstractCard c : list) {
            BaseMod.MAX_HAND_SIZE ++;
            c.unfadeOut();
            AbstractDungeon.player.hand.addToHand(c);
            c.setCostForTurn(0);
            AbstractDungeon.player.exhaustPile.removeCard(c);
            c.unhover();
            c.fadingOut = false;
        }
        isDone = true;
    }
}
