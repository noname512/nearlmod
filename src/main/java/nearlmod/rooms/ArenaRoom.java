package nearlmod.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.Legend;
import com.megacrit.cardcrawl.map.LegendItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import nearlmod.arenaevents.*;
import nearlmod.characters.Nearl;
import nearlmod.events.LaughAllYouWantEvent;

import java.util.ArrayList;

import nearlmod.patches.CharacterSettingPatch;
import nearlmod.patches.NearlEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArenaRoom extends AbstractRoom {
    protected static final Logger logger = LogManager.getLogger(ArenaRoom.class.getName());
    public static final String ID = "nearlmod:ArenaRoom";
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final Texture MAP_IMG = new Texture("resources/nearlmod/images/ui/arena.png");
    public static int enterTimes;

    public ArenaRoom() {
        mapSymbol = "A";
        mapImg = MAP_IMG;
        mapImgOutline = new Texture("resources/nearlmod/images/ui/arenaoutline.png");
    }

    @Override
    public void onPlayerEntry() {
        phase = RoomPhase.EVENT;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        enterTimes++;
        logger.info("enterTimes = " + enterTimes);
        event = new LaughAllYouWantEvent(); // 防crash
        if (CharacterSettingPatch.curTeam == 1) {
            if (enterTimes == 1) event = new MephistoBattle();
            else if (enterTimes == 2) event = new FamigliaCleanerBattle();
            else if (enterTimes == 3) event = new CandleKnightBattle();
            else if (enterTimes == 4) event = new TheWillofSamiBattle();
            else if (enterTimes == 5) event = new CorruptedWitheredBattle_SP();
            else {
                int rand = AbstractDungeon.eventRng.random(0, 4);
                int monsterLevel = enterTimes - 1;
                if (rand <= 1) event = new LazuriteSquadBattle(monsterLevel);
                else if (rand <= 3) event = new ArmorlessSquadBattle(monsterLevel);
                else event = new WanderingKnightBattle(monsterLevel);
            }
        }
        else {
            if (enterTimes == 1) event = new CorruptedWitheredBattle();
            else if (enterTimes == 2) event = new LeftHandBattle();
            else if (enterTimes == 3) event = new CandleKnightBattle();
            else if (enterTimes == 4) event = new LastKheshigBattle();
            else if (enterTimes == 5) event = new BloodKnightBattle();
            else {
                int rand = AbstractDungeon.eventRng.random(0, 4);
                int monsterLevel = enterTimes - 1;
                if (rand <= 1) event = new LazuriteSquadBattle(monsterLevel);
                else if (rand <= 3) event = new ArmorlessSquadBattle(monsterLevel);
                else event = new WanderingKnightBattle(monsterLevel);
            }
        }
        event.onEnterRoom();
    }
    public void update() {
        super.update();
        if (this.event == null) return;
        if (!AbstractDungeon.isScreenUp) {
            this.event.update();
        }

        if (this.event.waitTimer == 0.0F && !this.event.hasFocus && this.phase != RoomPhase.COMBAT) {
            this.phase = RoomPhase.COMPLETE;
            this.event.reopen();
        }

    }

    public void render(SpriteBatch sb) {
        if (this.event != null) {
            this.event.render(sb);
        }
        super.render(sb);
    }

    public void renderAboveTopPanel(SpriteBatch sb) {
        super.renderAboveTopPanel(sb);
        if (this.event != null) {
            this.event.renderAboveTopPanel(sb);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "generateRoomTypes")
    public static class MapGeneratePatch {
        @SpirePostfixPatch
        public static void Postfix(ArrayList<AbstractRoom> roomList, int availableRoomCount) {
            if (!(AbstractDungeon.player instanceof Nearl)) return;
            int arenaCount = Math.round((float)availableRoomCount * 0.07F);
            logger.info(" ARENA (7%): " + arenaCount);
            for (int i = 1; i <= arenaCount; i++)
                roomList.add(new ArenaRoom());
        }
    }

    @SpirePatch(clz = Legend.class, method = "<ctor>")
    public static class LogoRenderPatch {
        @SpirePostfixPatch
        public static void Postfix(Legend __instance) {
            __instance.items.add(new LegendItem(ArenaRoom.TEXT[0], ArenaRoom.MAP_IMG, ArenaRoom.TEXT[1], ArenaRoom.TEXT[2], 6));
            ImageMaster.MAP_LEGEND = new Texture("resources/nearlmod/images/ui/legend.png");
        }
    }

    @SpirePatch(clz = Legend.class, method = "isIconHovered")
    public static class IconHoveredPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(Legend __instance, String nodeHovered) {
            if (nodeHovered.equals("A")) {
                if (__instance.items.get(6).hb.hovered)
                    return SpireReturn.Return(true);
                else
                    return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderPatch {
        @SpireInsertPatch(rloc = 36)
        public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom) {
                AbstractDungeon.getCurrRoom().renderEventTexts(sb);
            }
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class ProceedPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(Instanceof i) throws CannotCompileException {
                    try {
                        if (i.getType().getName().equals(Mushrooms.class.getName()))
                            i.replace(String.format(" $_ = $proceed($$) || %s.check($1); ", ArenaRoom.ProceedPatch.class.getName()));
                    } catch (NotFoundException ignored) {}
                }
            };
        }

        public static boolean check(Object event) {
            return event instanceof AbstractArenaEvent;
        }

        @SpireInstrumentPatch
        public static ExprEditor Instrument1() {
            return new ExprEditor() {
                public void edit(Instanceof i) throws CannotCompileException {
                    try {
                        if (i.getType().getName().equals(EventRoom.class.getName()))
                            i.replace(String.format(" $_ = $proceed($$) || %s.check1($1); ", ArenaRoom.ProceedPatch.class.getName()));
                    } catch (NotFoundException ignored) {}
                }
            };
        }

        public static boolean check1(Object room) {
            return room instanceof ArenaRoom;
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "render")
    public static class RenderRewardScreenPatch {
        @SpireInsertPatch(rloc = 2)
        public static SpireReturn<?> Insert(CombatRewardScreen __instance, SpriteBatch sb, float ___tipY) {
            if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom) {
                if (enterTimes <= 5) {
                    FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, TEXT[enterTimes + 2], Settings.WIDTH / 2.0F, ___tipY, Color.LIGHT_GRAY);
                    for (AbstractGameEffect e : __instance.effects)
                        e.render(sb);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }

//    @SpirePatch(clz = AbstractScene.class, method = "renderEventRoom")
//    public static class BackgroundPatch {
//        @SpirePrefixPatch
//        public static SpireReturn<?> Prefix(AbstractScene __instance, SpriteBatch sb) {
//            if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom) {
//                sb.draw(new Texture("resources/nearlmod/images/ui/arenascene.png"), 0.0F, 0.0F, 0.0F, 0.0F, 1920, 1080, Settings.xScale, Settings.xScale, 0.0F, 0, 0, 3840, 2160, false, false);
//                return SpireReturn.Return();
//            }
//            return SpireReturn.Continue();
//        }
//    }
//
//    @SpirePatch(clz = TheBottomScene.class, method = "renderCombatRoomBg")
//    public static class BottomBackgroundPatch {
//        @SpirePrefixPatch
//        public static SpireReturn<?> Prefix(TheBottomScene __instance, SpriteBatch sb) {
//            if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom) {
//                sb.draw(new Texture("resources/nearlmod/images/ui/arenascene.png"), 0.0F, 0.0F, 0.0F, 0.0F, 1920, 1080, Settings.xScale, Settings.xScale, 0.0F, 0, 0, 3840, 2160, false, false);
//                return SpireReturn.Return();
//            }
//            return SpireReturn.Continue();
//        }
//    }

    // TODO nextRoomTransition可能需要patch
}
