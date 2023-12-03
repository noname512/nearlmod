package nearlmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class AddFriendCardToHandAction extends AbstractGameAction {

    private final AbstractFriendCard card;

    public AddFriendCardToHandAction(AbstractFriendCard card) {
        this(card, false);
    }

    public AddFriendCardToHandAction(AbstractFriendCard card, boolean needUpgrade) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        if (needUpgrade) card.upgrade();
        this.card = card;
    }

    @Override
    public void update() {
        BaseMod.MAX_HAND_SIZE++;
        UnlockTracker.markCardAsSeen(card.cardID);
        AbstractDungeon.player.hand.addToHand(card);
        isDone = true;
    }
}
