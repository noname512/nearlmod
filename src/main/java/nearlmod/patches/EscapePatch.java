package nearlmod.patches;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.monsters.Platinum;
import nearlmod.monsters.Roy;

@SpirePatch(clz = AbstractMonster.class, method = "updateEscapeAnimation")
public class EscapePatch {
    @SpirePrefixPatch
    public static SpireReturn<?> Prefix(AbstractMonster __instance) {
        if (__instance.id.equals(Roy.ID) || __instance.id.equals(Platinum.ID)) {
            if (__instance.escapeTimer != 0.0F) {
                __instance.flipHorizontal = false;
                __instance.escapeTimer -= Gdx.graphics.getDeltaTime();
                __instance.drawX += Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
            }
            if (__instance.escapeTimer < 0.0F) {
                __instance.escaped = true;
                if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                    AbstractDungeon.getCurrRoom().endBattle();
                }
            }
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
