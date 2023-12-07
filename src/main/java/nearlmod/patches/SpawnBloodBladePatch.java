package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;
import nearlmod.monsters.BloodKnight;

@SpirePatch(clz = SpawnMonsterAction.class, method = "update")
public class SpawnBloodBladePatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"m", "position"})
    public static void Insert(SpawnMonsterAction __instance, AbstractMonster m, @ByRef int[] position) {
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if ((ms instanceof BloodKnight) && ms.drawX < m.drawX)
                position[0]--;
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "addMonster");
            return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
        }
    }
}
