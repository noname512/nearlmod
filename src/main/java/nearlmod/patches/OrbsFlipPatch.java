package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import nearlmod.orbs.AbstractFriend;

public class OrbsFlipPatch {
    public static void setFlip(AbstractPlayer p) {
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend)
                ((AbstractFriend) orb).flipHorizontal = p.flipHorizontal;
    }
    @SpirePatch(clz = AbstractPlayer.class, method = "playCard")
    public static class PlayCardPatch {
        @SpirePostfixPatch
        public static void setFlipOnCardPlayed(AbstractPlayer __instance) {
            OrbsFlipPatch.setFlip(__instance);
        }
    }

    @SpirePatch(clz = SpireSpear.class, method = "die")
    public static class SpearDiePatch {
        @SpirePostfixPatch
        public static void setFlipOnSpireDead(SpireSpear __instance) {
            OrbsFlipPatch.setFlip(AbstractDungeon.player);
        }
    }

    @SpirePatch(clz = SpireShield.class, method = "die")
    public static class ShieldDiePatch {
        @SpirePostfixPatch
        public static void setFlipOnShieldDead(SpireShield __instance) {
            OrbsFlipPatch.setFlip(AbstractDungeon.player);
        }
    }

    @SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode")
    public static class PotionPopUpPatch {
        @SpirePostfixPatch
        public static void setFlipOnPotionPopped(PotionPopUp __instance) {
            OrbsFlipPatch.setFlip(AbstractDungeon.player);
        }
    }
}
