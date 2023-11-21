package nearlmod.patches;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import nearlmod.characters.Nearl;
import nearlmod.orbs.AbstractFriend;

public class PlayerEscapePatch {
    @SpirePatch(clz = SmokeBomb.class, method = "use")
    public static class SmokeBombPatch {
        @SpirePostfixPatch
        public static void setFlipOnSmokeBombUsed(SmokeBomb __instance, AbstractCreature target) {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractOrb orb : p.orbs)
                if (orb instanceof AbstractFriend)
                    ((AbstractFriend) orb).flipHorizontal = p.flipHorizontal;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "updateEscapeAnimation")
    public static class EscapeAnimationPatch {
        @SpirePrefixPatch
        public static void setOrbsAnimation(AbstractPlayer __instance) {
            if (__instance instanceof Nearl)
                if (__instance.escapeTimer != 0.0F) {
                    for (int i = 0; i < __instance.orbs.size(); i++)
                        __instance.orbs.get(i).setSlot(i, __instance.maxOrbs);
                }
        }
    }
}
