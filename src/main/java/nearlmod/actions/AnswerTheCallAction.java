package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.monsters.CandleKnight;
import nearlmod.orbs.Gummy;

public class AnswerTheCallAction extends AbstractGameAction {
    public AnswerTheCallAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if ((c instanceof AbstractFriendCard) && (((AbstractFriendCard)c).belongFriend.equals(Gummy.ORB_ID) && (c.type == AbstractCard.CardType.ATTACK))) {
                AbstractDungeon.player.hand.addToHand(c);
                c.setCostForTurn(0);
            }
        }
        isDone = true;
    }
}
