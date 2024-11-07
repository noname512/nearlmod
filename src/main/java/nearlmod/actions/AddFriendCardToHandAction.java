package nearlmod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class AddFriendCardToHandAction extends AbstractGameAction {

    private final AbstractFriendCard card;

    public AddFriendCardToHandAction(AbstractFriendCard card) {
        this(card, false);
    }

    public AddFriendCardToHandAction(AbstractFriendCard card, boolean needUpgrade) {
        this(card, needUpgrade, 1);
    }

    public AddFriendCardToHandAction(AbstractFriendCard card, boolean needUpgrade, int amount) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        if (needUpgrade || AbstractDungeon.player.hasPower("MasterRealityPower")) card.upgrade();
        this.card = card;
        this.amount = amount;
    }

    @Override
    public void update() {
        BaseMod.MAX_HAND_SIZE += amount;
        for (int i = 0; i < amount; i++) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card.makeStatEquivalentCopy(), MathUtils.random((float) Settings.WIDTH * 0.2F, (float) Settings.WIDTH * 0.8F), MathUtils.random((float) Settings.HEIGHT * 0.3F, (float) Settings.HEIGHT * 0.7F)));
        }
        isDone = true;
    }
}
