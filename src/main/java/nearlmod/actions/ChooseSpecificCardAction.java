package nearlmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class ChooseSpecificCardAction extends AbstractGameAction {
    private final ArrayList<AbstractCard> cards;
    public ChooseSpecificCardAction(ArrayList<AbstractCard> cards) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        this.cards = cards;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (cards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(cards, CardRewardScreen.TEXT[1], false);
            tickDuration();
            return;
        }
        if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
            AbstractCard card = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
            if (AbstractDungeon.player.hasPower("MasterRealityPower"))
                card.upgrade();
            BaseMod.MAX_HAND_SIZE++;
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.cardRewardScreen.discoveryCard = null;
            isDone = true;
        }
    }
}
