package nearlmod.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import com.megacrit.cardcrawl.screens.compendium.RelicViewScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import nearlmod.relics.LateLight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

public class CurseRelicPatch {
    protected static final Logger logger = LogManager.getLogger(CurseRelicPatch.class.getName());
    public static ArrayList<AbstractRelic> curseList = new ArrayList<>();
    public static boolean isCurseListHovered = false;
    public static AbstractRelic myHoveredRelic = null;
    public static AbstractRelic myClickStartedRelic = null;
    public static Hitbox myHitbox = null;

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
                relicFrameImg[0] = ImageMaster.loadImage("resources/nearlmod/images/ui/relicFrameCurse.png");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = RelicLibrary.class, method = "addToTierList")
    public static class addToTierListPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractRelic relic) {
            if (relic.tier == CurseRelicPatch.CURSE) {
                curseList.add(relic);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = RelicLibrary.class, method = "resetForReload")
    public static class resetForReloadPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            curseList.clear();
        }
    }

    @SpirePatch(clz = RelicLibrary.class, method = "sortLists")
    public static class sortListsPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            Collections.sort(curseList);
            if (Settings.isDev) {
                logger.info(curseList);
            }
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "sortOnOpen")
    public static class sortOnOpenPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            curseList = RelicLibrary.sortByStatus(curseList, false);
            myHitbox = null;
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "update")
    public static class updateRelicViewScreenPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(RelicViewScreen __instance) {
            isCurseListHovered = false;
            myHoveredRelic = null;
            if (!CardCrawlGame.relicPopup.isOpen) {
                for (AbstractRelic r : curseList) {
                    r.hb.move(r.currentX, r.currentY);
                    r.update();
                    if (r.hb.hovered) {
                        myHoveredRelic = r;
                        isCurseListHovered = true;
                    }
                }
            }
            if (Settings.isControllerMode && myHitbox != null) {
                Gdx.input.setCursorPosition((int)myHitbox.cX, (int)(Settings.HEIGHT - myHitbox.cY));
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(RelicViewScreen.class, "updateList");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "update")
    public static class relicPopupPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(RelicViewScreen __instance) {
            if (myHoveredRelic != null) {
                CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
                if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
                    myClickStartedRelic = myHoveredRelic;
                }

                if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed()) {
                    CInputActionSet.select.unpress();
                    if (myHoveredRelic == myClickStartedRelic) {
                        CardCrawlGame.relicPopup.open(myHoveredRelic, curseList);
                        myClickStartedRelic = null;
                    }
                }
            } else {
                myClickStartedRelic = null;
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(MenuCancelButton.class, "update");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "updateControllerInput")
    public static class updateControllerInputPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"targetY"})
        public static void Insert(RelicViewScreen __instance, @ByRef float[] targetY) {
            int index = 0;
            boolean anyHovered = false;
            float scrollLowerBound = (float)Settings.HEIGHT - 100.0F * Settings.scale;
            float scrollUpperBound = 3000.0F * Settings.scale;
            for (AbstractRelic r : curseList) {
                if (r.hb.hovered) {
                    anyHovered = true;
                    break;
                }
                index++;
            }
            if (anyHovered) {
                if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
                    index -= 10;
                    if (index < 0) {
                        index = RelicLibrary.shopList.size() - 1;
                        Gdx.input.setCursorPosition((int)RelicLibrary.shopList.get(index).hb.cX, Settings.HEIGHT - (int)RelicLibrary.bossList.get(index).hb.cY);
                        myHitbox = RelicLibrary.shopList.get(index).hb;
                    } else {
                        Gdx.input.setCursorPosition((int)curseList.get(index).hb.cX, Settings.HEIGHT - (int)curseList.get(index).hb.cY);
                        myHitbox = curseList.get(index).hb;
                    }
                } else if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
                    index += 10;
                    if (index <= curseList.size() - 1) {
                        Gdx.input.setCursorPosition((int)curseList.get(index).hb.cX, Settings.HEIGHT - (int)curseList.get(index).hb.cY);
                        myHitbox = curseList.get(index).hb;
                    }
                } else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
                    --index;
                    if (index < 0) {
                        index = curseList.size() - 1;
                    }

                    Gdx.input.setCursorPosition((int)curseList.get(index).hb.cX, Settings.HEIGHT - (int)curseList.get(index).hb.cY);
                    myHitbox = curseList.get(index).hb;
                } else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    ++index;
                    if (index > curseList.size() - 1) {
                        index = 0;
                    }

                    Gdx.input.setCursorPosition((int)curseList.get(index).hb.cX, Settings.HEIGHT - (int)curseList.get(index).hb.cY);
                    myHitbox = curseList.get(index).hb;
                }
                if (myHitbox != null) {
                    if ((float)Gdx.input.getY() > (float)Settings.HEIGHT * 0.7F) {
                        targetY[0] += Settings.SCROLL_SPEED;
                        if (targetY[0] > scrollUpperBound) {
                            targetY[0] = scrollUpperBound;
                        }
                    } else if ((float)Gdx.input.getY() < (float)Settings.HEIGHT * 0.3F) {
                        targetY[0] -= Settings.SCROLL_SPEED;
                        if (targetY[0] < scrollLowerBound) {
                            targetY[0] = scrollLowerBound;
                        }
                    }
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(Gdx.class, "input");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "updateControllerInput")
    public static class updateControllerInputPatch2 {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RelicViewScreen __instance) {
            if (Settings.isControllerMode && (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed())) {
                int index = 0;
                for (AbstractRelic r : RelicLibrary.shopList) {
                    if (r.hb.hovered) {
                        if (index + 10 >= RelicLibrary.shopList.size()) {
                            index %= 10;
                            if (index > curseList.size() - 1) {
                                index = curseList.size() - 1;
                            }

                            Gdx.input.setCursorPosition((int) curseList.get(index).hb.cX, Settings.HEIGHT - (int) curseList.get(index).hb.cY);
                            myHitbox = curseList.get(index).hb;
                            return SpireReturn.Return();
                        }
                    }
                    index++;
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "render")
    public static class renderRelicViewScreenPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"row", "scrollY"})
        public static void Insert(RelicViewScreen __instance, SpriteBatch sb, @ByRef int[] row, float scrollY) {
            UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:CurseRelic");
            String[] TEXT = uiStrings.TEXT;
            String msg = TEXT[2];
            String desc = TEXT[3];
            float START_X = 600.0F * Settings.scale;
            float SPACE = 80.0F * Settings.scale;
            int col = 0;
            row[0] += 2;
            FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, msg, START_X - 50.0F * Settings.scale, scrollY + 4.0F * Settings.scale - SPACE * (float)row[0], 99999.0F, 0.0F, Settings.GOLD_COLOR);
            FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, desc, START_X - 50.0F * Settings.scale + FontHelper.getSmartWidth(FontHelper.buttonLabelFont, msg, 99999.0F, 0.0F), scrollY - 0.0F * Settings.scale - SPACE * (float)row[0], 99999.0F, 0.0F, Settings.CREAM_COLOR);
            row[0]++;

            for (AbstractRelic r : curseList) {
                if (col == 10) {
                    col = 0;
                    row[0]++;
                }
                r.currentX = START_X + SPACE * (float)col;
                r.currentY = scrollY - SPACE * (float)row[0];

                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
                } else {
                    r.render(sb, false, Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
                }
                col++;
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(RelicViewScreen.class, "button");
                return LineFinder.findAllInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = RunHistoryScreen.class, method = "reloadRelics")
    public static class reloadRelicsPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"relicRarityCounts", "bldr"})
        public static void Insert(RunHistoryScreen __instance, RunData rundata, Hashtable<AbstractRelic.RelicTier, Integer> relicRarityCounts, StringBuilder bldr) {
            AbstractRelic.RelicTier rarity = CurseRelicPatch.CURSE;
            if (relicRarityCounts.containsKey(rarity)) {
                if (bldr.length() > 0) {
                    bldr.append(", ");
                }
                UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:CurseRelic");
                String[] TEXT = uiStrings.TEXT;
                bldr.append(String.format(RunHistoryScreen.TEXT[20], relicRarityCounts.get(rarity), TEXT[1]));
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(RunHistoryScreen.class, "relicCountByRarityString");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
