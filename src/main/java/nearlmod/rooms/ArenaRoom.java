package nearlmod.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.arenaevents.CandleKnightBattle;
import nearlmod.arenaevents.CorruptedWitheredBattle;
import nearlmod.arenaevents.LeftHandBattle;
import nearlmod.events.LaughAllYouWantEvent;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArenaRoom extends AbstractRoom {
    protected static final Logger logger = LogManager.getLogger(ArenaRoom.class.getName());
    public static final String ID = "nearlmod:ArenaRoom";
    public static int enterTimes;

    public ArenaRoom() {
        mapSymbol = "A";
        mapImg = new Texture("images/ui/arena.png");
        mapImgOutline = new Texture("images/ui/arenaoutline.png");
        rewards.clear();
    }

    @Override
    public void onPlayerEntry() {
        phase = RoomPhase.EVENT;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        //TODO 播放bgm：长夜临光活动主题曲
        enterTimes++;
        logger.info("enterTimes = " + enterTimes);
        event = new LaughAllYouWantEvent(); // 防crash
        if (enterTimes == 1) event = new CorruptedWitheredBattle();
        else if (enterTimes == 2) event = new LeftHandBattle();
        else if (enterTimes == 3) event = new CandleKnightBattle();
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
            int arenaCount = Math.round((float)availableRoomCount * 0.07F);
            logger.info(" ARENA (7%): " + arenaCount);
            for (int i = 1; i <= arenaCount; i++)
                roomList.add(new ArenaRoom());
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

//    @SpirePatch(clz = AbstractScene.class, method = "renderEventRoom")
//    public static class BackgroundPatch {
//        @SpirePrefixPatch
//        public static SpireReturn<?> Prefix(AbstractScene __instance, SpriteBatch sb) {
//            if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom) {
//                sb.draw(new Texture("images/ui/arenascene.png"), 0.0F, 0.0F, 0.0F, 0.0F, 1920, 1080, Settings.xScale, Settings.xScale, 0.0F, 0, 0, 3840, 2160, false, false);
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
//                sb.draw(new Texture("images/ui/arenascene.png"), 0.0F, 0.0F, 0.0F, 0.0F, 1920, 1080, Settings.xScale, Settings.xScale, 0.0F, 0, 0, 3840, 2160, false, false);
//                return SpireReturn.Return();
//            }
//            return SpireReturn.Continue();
//        }
//    }

    // TODO nextRoomTransition可能需要patch
}
