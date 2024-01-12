package nearlmod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import nearlmod.cards.friendcards.AbstractFriendCard;

import java.util.ArrayList;

public class ChooseSpecificCardAction extends AbstractGameAction {
    private final ArrayList<AbstractCard> cards;
    private final boolean zeroCost;
    private final boolean canSkip;
    public ChooseSpecificCardAction(ArrayList<AbstractCard> cards) {
        this(cards, false);
    }

    public ChooseSpecificCardAction(ArrayList<AbstractCard> cards, boolean zeroCost) {
        this(cards, zeroCost, 1);
    }

    public ChooseSpecificCardAction(ArrayList<AbstractCard> cards, boolean zeroCost, int amount) {
        this(cards, zeroCost, amount, false);
    }
    public ChooseSpecificCardAction(ArrayList<AbstractCard> cards, boolean zeroCost, int amount, boolean canSkip) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        this.cards = cards;
        this.zeroCost = zeroCost;
        this.amount = amount;
        this.canSkip = canSkip;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (cards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(cards, CardRewardScreen.TEXT[1], canSkip);
            tickDuration();
            return;
        }
        if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
            AbstractCard card = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
            if (AbstractDungeon.player.hasPower("MasterRealityPower"))
                card.upgrade();
            if (zeroCost)
                card.setCostForTurn(0);
            if (card instanceof AbstractFriendCard)
                BaseMod.MAX_HAND_SIZE += amount;
            int resSpace = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
            for (int i = 0; i < resSpace && i < amount; i++) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card.makeStatEquivalentCopy(), MathUtils.random(Settings.WIDTH * 0.4F, Settings.WIDTH * 0.6F), MathUtils.random(Settings.HEIGHT * 0.4F, Settings.HEIGHT * 0.6F)));
            }
            for (int i = resSpace; i < amount; i++) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card.makeStatEquivalentCopy(), MathUtils.random(Settings.WIDTH * 0.4F, Settings.WIDTH * 0.6F), MathUtils.random(Settings.HEIGHT * 0.4F, Settings.HEIGHT * 0.6F)));
            }
            AbstractDungeon.cardRewardScreen.discoveryCard = null;
            isDone = true;
        }
        else {
            isDone = true;
        }
    }
}
