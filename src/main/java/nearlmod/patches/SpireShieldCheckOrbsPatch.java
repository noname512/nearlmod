package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.characters.Nearl;

public class SpireShieldCheckOrbsPatch {
    @SpirePatch(clz = SpireShield.class, method = "takeTurn")
    public static class checkOrbsPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(SpireShield __instance) {
            if (!(AbstractDungeon.player instanceof Nearl))
                return SpireReturn.Continue();
            if (__instance.nextMove == 1) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(__instance, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.35F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, __instance.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, __instance, new StrengthPower(AbstractDungeon.player, -1), -1));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
