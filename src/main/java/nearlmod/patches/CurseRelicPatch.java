package nearlmod.patches;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class CurseRelicPatch {
    protected static final Logger logger = LogManager.getLogger(CurseRelicPatch.class.getName());
    @SpireEnum
    public static AbstractRelic.RelicTier CURSE;

    @SpirePatch(clz = Nloth.class, method = "<ctor>")
    public static class NlothPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = "relics")
        public static void Insert(Nloth __instance, ArrayList<AbstractRelic> relics) {
            relics.removeIf(r -> r.tier == CurseRelicPatch.CURSE);
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Collections.class, "shuffle");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getShrine", paramtypez = {Random.class})
    public static class NlothGetShrinePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void Insert(Random rng, ArrayList<String> tmp) {
            for (String e : tmp) {
                if (e.equals("N'loth")) {
                    int realCnt = 0;
                    for (AbstractRelic r : AbstractDungeon.player.relics)
                        if (r.tier != CurseRelicPatch.CURSE)
                            realCnt++;
                    if (realCnt < 2) {
                        tmp.remove(e);
                        CurseRelicPatch.logger.info("Real relic count < 2, remove event N'loth");
                    }
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Random.class, "random");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = SingleRelicViewPopup.class, method = "generateRarityLabel")
    public static class GenerateRarityLabelPatch {
        @SpireInsertPatch(rloc = 0, localvars = {"relic", "rarityLabel"})
        public static SpireReturn<?> Insert(SingleRelicViewPopup __instance, AbstractRelic relic, @ByRef String[] rarityLabel) {
            if (relic.tier == CurseRelicPatch.CURSE) {
                UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:CurseRelic");
                String[] TEXT = uiStrings.TEXT;
                rarityLabel[0] = TEXT[0];
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SingleRelicViewPopup.class, method = "generateFrameImg")
    public static class generateFrameImgPatch {
        @SpireInsertPatch(rloc = 0, localvars = {"relic", "relicFrameImg"})
        public static SpireReturn<?> Insert(SingleRelicViewPopup __instance, AbstractRelic relic, @ByRef Texture[] relicFrameImg) {
            if (relic.isSeen && relic.tier == CurseRelicPatch.CURSE) {
                relicFrameImg[0] = ImageMaster.loadImage("images/ui/relicFrameCurse.png");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
