package nearlmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import nearlmod.NLMOD;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class FriendCardRenderPatch {
    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderPatch {
        @SpireInsertPatch(rloc = 0, localvars = {"renderColor"})
        public static void Insert(AbstractCard __instance, SpriteBatch sb, Color renderColor) {
            if (__instance instanceof AbstractFriendCard && NLMOD.friendTipMode) {
                sb.setColor(renderColor);
                sb.draw(NLMOD.specialImg.get(((AbstractFriendCard)__instance).belongFriend),
                        __instance.current_x - 256.0F, __instance.current_y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F,
                        __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle);
            }
        }
    }
}
