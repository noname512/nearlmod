package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import nearlmod.actions.GainCostAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.util.CostReserves;
import static java.lang.Math.min;

public class CostPatch {
    @SpirePatch2(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class CardCostPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> bePlayable(AbstractCard __instance) {
            int found = CostReserves.reserveCount();
            if (__instance instanceof AbstractFriendCard) {
                return SpireReturn.Return(EnergyPanel.totalCount + found >= __instance.cost);
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(EnergyPanel.class, "totalCount");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "useCard")
    public static class UseCost {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> spendCost(AbstractPlayer __instance, AbstractCard c) {
            int found = CostReserves.reserveCount();
            if (c instanceof AbstractFriendCard) {
                if (found > 0 && c.costForTurn > 0) {
                    int delt = min(found, c.costForTurn);
                    CostReserves.addReserves(-delt);
                    if (c.costForTurn - delt > 0) {
                        __instance.energy.use(c.costForTurn - delt);
                    }
                    return SpireReturn.Return(0);
                }
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "costForTurn");
                return LineFinder.findAllInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
