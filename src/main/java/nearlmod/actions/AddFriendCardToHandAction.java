package nearlmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class AddFriendCardToHandAction extends AbstractGameAction {

    private AbstractFriendCard card;

    public AddFriendCardToHandAction(AbstractFriendCard card) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.card = card;
    }

    @Override
    public void update() {
        BaseMod.MAX_HAND_SIZE++;
        AbstractDungeon.player.hand.addToHand(card);
        isDone = true;
    }
}
