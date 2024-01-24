package nearlmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class AllFromDeckToHandAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:SelectScreenText");
    private static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;
    public AllFromDeckToHandAction(int amount) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
        this.amount = amount;
        this.p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : p.drawPile.group)
                tmp.addToRandomSpot(card);

            if (tmp.isEmpty()) {
                isDone = true;
                return;
            }
            if (tmp.size() == 1) {
                moveCardToHand(tmp.getTopCard());
                isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, amount == 1? TEXT[3] : TEXT[0] + amount + TEXT[2], false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                moveCardToHand(c);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            isDone = true;
        }
    }

    private void moveCardToHand(AbstractCard card) {
        card.unhover();
        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            p.drawPile.moveToDiscardPile(card);
            p.createHandIsFullDialog();
        } else {
            p.drawPile.moveToHand(card);
        }
    }
}
