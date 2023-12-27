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

public class PocketVaultAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:SelectScreenText");
    public static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;
    public PocketVaultAction(int amount) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            isDone = true;
            return;
        }
        if (duration == Settings.ACTION_DUR_FAST) {
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            cards.group.addAll(p.drawPile.group);
            cards.group.addAll(p.discardPile.group);
            if (cards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(cards, amount, true, TEXT[0] + amount + TEXT[2]);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                moveCardToHand(card);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }
        tickDuration();
    }

    private void moveCardToHand(AbstractCard card) {
        card.unhover();
        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            p.createHandIsFullDialog();
            for (AbstractCard c : p.drawPile.group)
                if (c.uuid.equals(card.uuid)) {
                    p.drawPile.moveToDiscardPile(card);
                    return;
                }
        } else {
            for (AbstractCard c : p.drawPile.group)
                if (c.uuid.equals(card.uuid)) {
                    p.drawPile.moveToHand(card);
                    return;
                }
            p.discardPile.moveToHand(card);
        }
    }
}