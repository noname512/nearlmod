package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.TinyHouse;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = TinyHouse.class, method = "onEquip")
public class TinyHousePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<?> Insert(TinyHouse __instance) {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.combatRewardScreen.rewards = new ArrayList<>(AbstractDungeon.getCurrRoom().rewards);
            AbstractDungeon.combatRewardScreen.rewards.removeIf(item -> item.relic instanceof TinyHouse);
            AbstractDungeon.combatRewardScreen.positionRewards();
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "open");
            return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
        }
    }
}
