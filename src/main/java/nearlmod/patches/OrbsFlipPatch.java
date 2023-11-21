package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.AbstractFriend;

@SpirePatch(clz = AbstractPlayer.class, method = "playCard")
public class OrbsFlipPatch {
    @SpirePostfixPatch
    public static void setFlip(AbstractPlayer __instance) {
        for (AbstractOrb orb : __instance.orbs)
            if (orb instanceof AbstractFriend)
                ((AbstractFriend) orb).flipHorizontal = __instance.flipHorizontal;
    }
}
