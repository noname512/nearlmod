package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nearlmod.orbs.AbstractFriend;

import java.util.Collections;

public class ChannelOrbPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
    public static class CheckOrbNumPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractPlayer __instance, AbstractOrb orbToSet) {
            int index = -1;
            for (int i = 0; i < __instance.orbs.size(); i++)
                if (!(__instance.orbs.get(i) instanceof AbstractFriend)) {
                    index = i;
                    break;
                }
            if (index == -1) {
                AbstractDungeon.effectList.add(new ThoughtBubble(__instance.dialogX, __instance.dialogY, 3.0F, AbstractPlayer.MSG[4], true));
                return SpireReturn.Return();
            }
            if (orbToSet instanceof AbstractFriend) {
                if (!(__instance.orbs.get(index) instanceof EmptyOrbSlot)) {
                    for (int i = __instance.orbs.size() - 1; i > index; i--) {
                        Collections.swap(__instance.orbs, i, i - 1);
                    }
                    __instance.orbs.set(index, new EmptyOrbSlot());
                    for (int i = 0; i < __instance.orbs.size(); i++) {
                        __instance.orbs.get(i).setSlot(i, __instance.maxOrbs);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AnimateOrbAction.class, method = "update")
    public static class AnimateOrbPatch {
        @SpireInsertPatch(rloc = 0, localvars = {"orbCount"})
        public static SpireReturn<?> Insert(AnimateOrbAction __instance, int orbCount) {
            for (int i = 0; i < AbstractDungeon.player.orbs.size() && orbCount > 0; i++)
                if (!(AbstractDungeon.player.orbs.get(i) instanceof AbstractFriend)) {
                    AbstractDungeon.player.triggerEvokeAnimation(i);
                    orbCount--;
                }
            __instance.isDone = true;
            return SpireReturn.Return();
        }
    }
}
