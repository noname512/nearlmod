package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.green.Setup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import nearlmod.characters.Nearl;

import java.util.logging.Logger;

public class AscensionModeDescriptionPatch {
    @SpirePatch(clz = CharacterOption.class, method = "incrementAscensionLevel")
    public static class IncAscPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance, int level, int ___maxAscensionLevel) {
            if (level == 15 && level <= ___maxAscensionLevel && CardCrawlGame.chosenCharacter == NearlEnum.NEARL_CLASS)
                CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = CardCrawlGame.languagePack.getUIString("nearlmod:MyAscensionModeDescriptions").TEXT[0];
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "decrementAscensionLevel")
    public static class DecAscPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance, int level) {
            if (level == 15 && CardCrawlGame.chosenCharacter == NearlEnum.NEARL_CLASS)
                CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = CardCrawlGame.languagePack.getUIString("nearlmod:MyAscensionModeDescriptions").TEXT[0];
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "updateHitbox")
    public static class updateHitboxPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance, int ___maxAscensionLevel) {
            if (__instance.hb.clicked && !__instance.selected && __instance.c.getPrefs() != null) {
                int level = CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel;
                if (level == 15 && level <= ___maxAscensionLevel && CardCrawlGame.chosenCharacter == NearlEnum.NEARL_CLASS)
                    CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = CardCrawlGame.languagePack.getUIString("nearlmod:MyAscensionModeDescriptions").TEXT[0];
            }
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "renderAscension")
    public static class RenderAscensionPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderSmartText")) {
                        m.replace(String.format(
                                "if (%s.RenderAscensionPatch.needChange(ascensionLevel)) { $0.renderSmartText($1, $2, %s.RenderAscensionPatch.getText(), $4, $5, $6, $7, $8); } else { $_ = $proceed($$); }",
                                AscensionModeDescriptionPatch.class.getName(),
                                AscensionModeDescriptionPatch.class.getName()));
                    }
                }
            };
        }

        public static boolean needChange(int level) {
            if (CardCrawlGame.chosenCharacter != NearlEnum.NEARL_CLASS) return false;
            return level == 15;
        }

        public static String getText() {
            String ret = CardCrawlGame.languagePack.getUIString("nearlmod:MyAscensionModeDescriptions").TEXT[0];
            CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = ret;
            return ret;
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "setupAscensionMode")
    public static class SetupAscensionModePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(TopPanel __instance, @ByRef String[] ___ascensionString) {
            if (!(AbstractDungeon.player instanceof Nearl)) return SpireReturn.Continue();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < AbstractDungeon.ascensionLevel; i++) {
                if (i == 14) sb.append(CardCrawlGame.languagePack.getUIString("nearlmod:MyAscensionModeDescriptions").TEXT[0]);
                else sb.append(CharacterSelectScreen.A_TEXT[i]);
                if (i != AbstractDungeon.ascensionLevel - 1) sb.append(" NL ");
            }
            ___ascensionString[0] = sb.toString();
            return SpireReturn.Return();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "ascensionLevel");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
