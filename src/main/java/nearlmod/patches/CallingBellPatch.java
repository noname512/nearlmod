package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CallingBell;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

import java.util.logging.Logger;

@SpirePatch(clz = CallingBell.class, method = "update")
public class CallingBellPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(CallingBell __instance, @ByRef boolean[] ___cardsReceived) {
        if (!___cardsReceived[0] && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            Logger.getLogger(CallingBellPatch.class.getName()).info("patched!");
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON)));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON)));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.RARE)));
            AbstractDungeon.combatRewardScreen.positionRewards();
            ___cardsReceived[0] = true;
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(CallingBell.class, "cardsReceived");
            return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
        }
    }
}
