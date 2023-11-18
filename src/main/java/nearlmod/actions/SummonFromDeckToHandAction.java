package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nearlmod.patches.NearlTags;

import static basemod.BaseMod.logger;

public class SummonFromDeckToHandAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:SummonFromDeckToHandAction");
    private static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;
    private final boolean needRetain;
    public SummonFromDeckToHandAction(int amount, boolean needRetain) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
        this.amount = amount;
        this.needRetain = needRetain;
        this.p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : p.drawPile.group)
                if (card.hasTag(NearlTags.IS_SUMMON_CARD))
                    tmp.addToRandomSpot(card);

            if (tmp.isEmpty()) {
                isDone = true;
                return;
            }
            if (tmp.size() == 1) {
                moveCardToHand(tmp.getTopCard());
                isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
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
        if (p.hand.size() >= 10) {
            p.drawPile.moveToDiscardPile(card);
            p.createHandIsFullDialog();
        } else {
            if (needRetain) card.retain = true;
            p.drawPile.moveToHand(card);
        }
    }
}
