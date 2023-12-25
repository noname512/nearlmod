package nearlmod.actions;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.powers.MedalOfHonorPower;

import java.util.ArrayList;

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
                if (!(c instanceof AbstractFriendCard)) {
                    cards.group.add(c);
                    c.stopGlowing();
                }
            if (cards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(cards, cards.group.size(), true, TEXT[1]);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (AbstractDungeon.player.hasPower(MedalOfHonorPower.POWER_ID)) {
                MedalOfHonorPower power = (MedalOfHonorPower) AbstractDungeon.player.getPower(MedalOfHonorPower.POWER_ID);
                int cnt = 0;
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                    if (c.type == AbstractCard.CardType.STATUS)
                        cnt++;
                power.notifyExhaustAmount(cnt);
            }
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                if (c.type != AbstractCard.CardType.STATUS)
                    exhaustCard(c);
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                if (c.type == AbstractCard.CardType.STATUS)
                    exhaustCard(c);
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

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class AddCardPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen __instance, String ___tipMsg) {
            if (___tipMsg.equals(TEXT[1])) {
                int cnt = 0;
                for (AbstractCard c : __instance.selectedCards)
                    if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                        cnt++;
                    else
                        cnt--;
                __instance.confirmButton.isDisabled = cnt != 0;
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "numCards");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class RemoveCardPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen __instance, String ___tipMsg, AbstractCard ___hoveredCard) {
            if (___tipMsg.equals(TEXT[1])) {
                int cnt = 0;
                for (AbstractCard c : __instance.selectedCards)
                    if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                        cnt++;
                    else
                        cnt--;
                if (___hoveredCard.type == AbstractCard.CardType.CURSE || ___hoveredCard.type == AbstractCard.CardType.STATUS)
                    cnt--;
                else
                    cnt++;
                __instance.confirmButton.isDisabled = cnt != 0;
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "remove");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}