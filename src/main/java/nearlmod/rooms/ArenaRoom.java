package nearlmod.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.monsters.CandleKnight;
import nearlmod.monsters.CorruptKnight;

import java.util.ArrayList;

import nearlmod.monsters.WitheredKnight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArenaRoom extends AbstractRoom {
    protected static final Logger logger = LogManager.getLogger(ArenaRoom.class.getName());
    public static final String ID = "nearlmod:ArenaRoom";
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static int enterTimes;

    public ArenaRoom() {
        phase = RoomPhase.COMBAT;
        mapSymbol = "A";
        mapImg = new Texture("images/ui/arena.png");
        mapImgOutline = new Texture("images/ui/arenaoutline.png");
        rewards.clear();
    }

    @Override
    public void onPlayerEntry() {
        //TODO 播放bgm：长夜临光活动主题曲
        enterTimes++;
        // 防crash用
        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AcidSlime_S(0.0F, 0.0F, 0));
        switch (enterTimes) {
            case 1:
                AbstractDungeon.lastCombatMetricKey = "nearlmod:Corrupted & Withered";
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] { new CorruptKnight(-200.0F, 0.0F), new WitheredKnight(80.0F, 0.0F) });
                break;
            case 2:
                AbstractDungeon.lastCombatMetricKey = "nearlmod:Left-hand";
                //TODO 左手
                break;
            case 3:
                AbstractDungeon.lastCombatMetricKey = CandleKnight.ID;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new CandleKnight());
                break;
            case 4:
                AbstractDungeon.lastCombatMetricKey = "nearlmod:The Last Kheshig";
                //TODO 拓拉
                break;
            case 5:
                AbstractDungeon.lastCombatMetricKey = "nearlmod:The Blood Knight";
                //TODO 血骑士
                break;
            default:
                AbstractDungeon.lastCombatMetricKey = "nearlmod:Armorless Union";
                //TODO 无胄盟小队
        }
        AbstractDungeon.getCurrRoom().monsters.init();
        waitTimer = 0.1F;
        AbstractDungeon.player.preBattlePrep();
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
}
