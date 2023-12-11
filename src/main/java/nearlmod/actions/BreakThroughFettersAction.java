package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class BreakThroughFettersAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:SelectScreenText");
    public static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;
    private final int lightUse;
    public BreakThroughFettersAction(int amount, int lightUse) {
        actionType = ActionType.EXHAUST;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.lightUse = lightUse;
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
            for (AbstractCard c : p.drawPile.group)
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                    cards.group.add(c);
            for (AbstractCard c : p.discardPile.group)
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                    cards.group.add(c);
            for (AbstractCard c : p.hand.group)
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                    cards.group.add(c);
            if (cards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(cards, amount, true, TEXT[0] + amount + TEXT[1]);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            addToTop(new ReducePowerAction(p, p, "nearlmod:LightPower", lightUse * AbstractDungeon.gridSelectScreen.selectedCards.size()));
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                exhaustCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }
        tickDuration();
    }

    private void exhaustCard(AbstractCard card) {
        for (AbstractCard c : p.drawPile.group)
            if (c.uuid.equals(card.uuid)) {
                addToTop(new ExhaustSpecificCardAction(c, p.drawPile));
                return;
            }
        for (AbstractCard c : p.discardPile.group)
            if (c.uuid.equals(card.uuid)) {
                addToTop(new ExhaustSpecificCardAction(c, p.discardPile));
                return;
            }
        for (AbstractCard c : p.hand.group)
            if (c.uuid.equals(card.uuid)) {
                addToTop(new ExhaustSpecificCardAction(c, p.hand));
                return;
            }
    }
}