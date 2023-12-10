package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.AbstractFriend;

public class AttackAnimatePatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class UseCard {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("useFastAttackAnimation")) {
                        m.replace(String.format(
                                "if (%s.isFriendCard(c)) { %s.animateFriend(c); } else if (%s.needExtraAnimate()) { $_ = $proceed($$); }",
                                AttackAnimatePatch.UseCard.class.getName(),
                                AttackAnimatePatch.UseCard.class.getName(),
                                AttackAnimatePatch.UseCard.class.getName()));
                    }
                }
            };
        }

        public static boolean isFriendCard(AbstractCard c) {
            return c instanceof AbstractFriendCard;
        }

        public static boolean needExtraAnimate() {
            return Settings.FAST_MODE;
        }

        public static void animateFriend(AbstractCard c) {
            for (AbstractOrb orb : AbstractDungeon.player.orbs)
                if (orb instanceof AbstractFriend && orb.ID.equals(((AbstractFriendCard)c).belongFriend))
                    ((AbstractFriend)orb).fastAttackAnimation();
        }
    }
}
