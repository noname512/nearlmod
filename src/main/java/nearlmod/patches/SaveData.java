package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import nearlmod.rooms.ArenaRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class SaveData {
    private static final Logger logger = LogManager.getLogger(SaveData.class);
    private static int arenaEnterTimes;
    @SpirePatch(clz = SaveFile.class, method = "<ctor>", paramtypez = {SaveFile.SaveType.class})
    public static class SaveTheSaveData {
        @SpirePostfixPatch
        public static void saveAllTheSaveData(SaveFile __instance, SaveFile.SaveType type) {
            SaveData.arenaEnterTimes = ArenaRoom.enterTimes;
            SaveData.logger.info("Saved arena enter times: " + SaveData.arenaEnterTimes);
        }
    }

    @SpirePatch(clz = SaveAndContinue.class, method = "save", paramtypez = {SaveFile.class})
    public static class SaveDataToFile {
        @SpireInsertPatch(locator = Locator.class, localvars = {"params"})
        public static void addCustomSaveData(SaveFile save, HashMap<Object, Object> params) {
            params.put("ARENA_ENTER_TIMES", SaveData.arenaEnterTimes);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(GsonBuilder.class, "create");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = SaveAndContinue.class, method = "loadSaveFile", paramtypez = {String.class})
    public static class LoadDataFromFile {
        @SpireInsertPatch(locator = Locator.class, localvars = {"gson", "savestr"})
        public static void loadCustomSaveData(String path, Gson gson, String savestr) {
            try {
                NearlData data = gson.fromJson(savestr, NearlData.class);
                SaveData.arenaEnterTimes = data.ARENA_ENTER_TIMES;
                SaveData.logger.info("Loaded nearlmod save data successfully, arena enter times = " + SaveData.arenaEnterTimes);
            } catch (Exception e) {
                SaveData.logger.error("Fail to load nearlmod save data.");
                e.printStackTrace();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "loadSave")
    public static class loadSavePatch {
        @SpirePostfixPatch
        public static void loadSave(AbstractDungeon __instance, SaveFile file) {
            ArenaRoom.enterTimes = SaveData.arenaEnterTimes;
            SaveData.logger.info("Save loaded.");
        }
    }
}
