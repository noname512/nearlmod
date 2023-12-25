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
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class BreakThroughFettersAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:SelectScreenText");
    public static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;
    public BreakThroughFettersAction() {
        actionType = ActionType.EXHAUST;
        duration = Settings.ACTION_DUR_FAST;
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
                if (!(c instanceof AbstractFriendCard))
                    cards.group.add(c);
            for (AbstractCard c : p.discardPile.group)
                if (!(c instanceof AbstractFriendCard))
                    cards.group.add(c);
            for (AbstractCard c : p.hand.group)
                if (!(c instanceof AbstractFriendCard))
                    cards.group.add(c);
            if (cards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(cards, cards.group.size(), true, TEXT[1]);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            int cnt = 0;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                {
                    cnt ++;
                }
                else {
                    cnt --;
                }
            }
            if (cnt == 0) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                    {
                        exhaustCard(c);
                        // TODO: 现在可能会把抽上来的牌烧掉，想想咋办
                    }
                }
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