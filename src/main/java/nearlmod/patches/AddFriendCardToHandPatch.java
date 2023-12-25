package nearlmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StasisPower;

public class AddFriendCardToHandPatch {
    @SpirePatch(clz = StasisPower.class, method = "onDeath")
    public static class StasisPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(StasisPower __instance, AbstractCard ___card) {
            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE)
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(___card, false, true));
            else
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(___card, true));
            return SpireReturn.Return();
        }
    }
}
