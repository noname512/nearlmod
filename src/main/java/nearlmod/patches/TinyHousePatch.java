package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.TinyHouse;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;

@SpirePatch(clz = TinyHouse.class, method = "onEquip")
public class TinyHousePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<?> Insert(TinyHouse __instance) {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
            return SpireReturn.Return();
        return SpireReturn.Continue();
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "open");
            return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
        }
    }
}
