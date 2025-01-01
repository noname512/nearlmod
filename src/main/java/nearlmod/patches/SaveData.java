package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import nearlmod.arenaevents.CorruptedWitheredBattle_SP;
import nearlmod.arenaevents.LeftHandBattle;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.SwallowLight;
import nearlmod.cards.WayToChampion;
import nearlmod.cards.special.Beginning;
import nearlmod.cards.special.BlemishinesFaintLight;
import nearlmod.cards.special.PersonalCharmSp;
import nearlmod.rooms.ArenaRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class SaveData {
    private static final Logger logger = LogManager.getLogger(SaveData.class);
    private static int arenaEnterTimes;
    private static int curTeam;
    private static String specialRewardCard;
    @SpirePatch(clz = SaveFile.class, method = "<ctor>", paramtypez = {SaveFile.SaveType.class})
    public static class SaveTheSaveData {
        @SpirePostfixPatch
        public static void Postfix(SaveFile __instance, SaveFile.SaveType type) {
            SaveData.arenaEnterTimes = ArenaRoom.enterTimes;
            SaveData.curTeam = CharacterSettingPatch.curTeam;
            SaveData.specialRewardCard = "";
            if (type == SaveFile.SaveType.POST_COMBAT) {
                if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom) SaveData.arenaEnterTimes--;
                if (AbstractDungeon.getCurrRoom().event != null && AbstractDungeon.getCurrRoom().event.noCardsInRewards) {
                    for (RewardItem item : AbstractDungeon.getCurrRoom().rewards)
                        if (item.type == RewardItem.RewardType.CARD) {
                            if (item.cards.size() == 1 && item.cards.get(0) instanceof AbstractNearlCard)
                                SaveData.specialRewardCard = item.cards.get(0).cardID;
                            else
                                SaveData.specialRewardCard = "nearlmod:LeftHandBattle";
                        }
                }
                if (AbstractDungeon.getCurrRoom() instanceof ArenaRoom && AbstractDungeon.getCurrRoom().event instanceof CorruptedWitheredBattle_SP) {
                    SaveData.specialRewardCard = "nearlmod:CorruptedWitheredBattle_SP";
                }
            }
            SaveData.logger.info("Saved arena enter times: " + SaveData.arenaEnterTimes);
        }
    }

    @SpirePatch(clz = SaveAndContinue.class, method = "save", paramtypez = {SaveFile.class})
    public static class SaveDataToFile {
        @SpireInsertPatch(locator = Locator.class, localvars = {"params"})
        public static void Insert(SaveFile save, HashMap<Object, Object> params) {
            params.put("ARENA_ENTER_TIMES", SaveData.arenaEnterTimes);
            params.put("CUR_TEAM", SaveData.curTeam);
            params.put("SPECIAL_REWARD_CARD", SaveData.specialRewardCard);
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
        public static void Insert(String path, Gson gson, String savestr) {
            try {
                NearlData data = gson.fromJson(savestr, NearlData.class);
                SaveData.arenaEnterTimes = data.ARENA_ENTER_TIMES;
                SaveData.curTeam = data.CUR_TEAM;
                SaveData.specialRewardCard = data.SPECIAL_REWARD_CARD;
                SaveData.logger.info("Loaded nearlmod save data successfully, arena enter times = " + SaveData.arenaEnterTimes + ", special reward card = " + SaveData.specialRewardCard);
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
        public static void Postfix(AbstractDungeon __instance, SaveFile file) {
            CharacterSettingPatch.curTeam = SaveData.curTeam;
            ArenaRoom.enterTimes = SaveData.arenaEnterTimes;
            SaveData.logger.info("Save loaded.");
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "loadPostCombat")
    public static class LoadPostCombatPatch {
        @SpirePrefixPatch
        public static void Prefix(CardCrawlGame __instance, SaveFile saveFile) {
            if (saveFile.post_combat && !saveFile.smoked && !SaveData.specialRewardCard.isEmpty()) {
                AbstractCard c;
                switch (SaveData.specialRewardCard) {
                    case Beginning.ID:
                        AbstractNearlCard.addSpecificCardsToReward(new Beginning());
                        break;
                    case SwallowLight.ID:
                        c = new SwallowLight();
                        if (AbstractDungeon.ascensionLevel < 12) c.upgrade();
                        AbstractNearlCard.addSpecificCardsToReward(c);
                        break;
                    case BlemishinesFaintLight.ID:
                        AbstractNearlCard.addSpecificCardsToReward(new BlemishinesFaintLight());
                        break;
                    case WayToChampion.ID:
                        c = new WayToChampion();
                        if (AbstractDungeon.ascensionLevel < 12) c.upgrade();
                        AbstractNearlCard.addSpecificCardsToReward(c);
                        break;
                    case PersonalCharmSp.ID:
                        AbstractNearlCard.addSpecificCardsToReward(new PersonalCharmSp());
                        break;
                    case "nearlmod:LeftHandBattle":
                        AbstractNearlCard.addSpecificCardsToReward(LeftHandBattle.getCardsWithRarity(AbstractCard.CardRarity.UNCOMMON));
                        break;
                    case "nearlmod:CorruptedWitheredBattle_SP":
                        AbstractNearlCard.addSpecificCardsToReward(new Beginning());
                        AbstractNearlCard.addSpecificCardsToReward(new BlemishinesFaintLight());
                        break;
                }
                SaveData.logger.info("Special reward loaded.");
            }
        }
    }
}
