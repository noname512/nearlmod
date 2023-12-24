package nearlmod.patches;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import nearlmod.arenaevents.*;

public class RunPathPatch {
    @SpireEnum
    public static RunPathElement.PathNodeType ARENA;
    public static final String ID = "nearlmod:RunPathPatch";

    private static UIStrings uiStrings() {
        return CardCrawlGame.languagePack.getUIString(ID);
    }

    @SpirePatch(clz = RunPathElement.class, method = "pathNodeTypeForRoomKey")
    public static class PathNodeTypeForRoomKeyPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RunPathElement __instance, String roomKey) {
            if (roomKey.equals("A")) return SpireReturn.Return(RunPathPatch.ARENA);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = "imageForType")
    public static class ImageForTypePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RunPathElement __instance, RunPathElement.PathNodeType nodeType) {
            if (nodeType == RunPathPatch.ARENA) return SpireReturn.Return(new Texture("images/ui/arenaicon.png"));
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = "stringForType", paramtypez = {RunPathElement.PathNodeType.class})
    public static class StringForTypePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(RunPathElement __instance, RunPathElement.PathNodeType nodeType) {
            if (nodeType == RunPathPatch.ARENA) return SpireReturn.Return(uiStrings().TEXT[0]);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MonsterHelper.class, method = "getEncounterName")
    public static class GetEncounterNamePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(String key) {
            if (key != null) {
                switch (key) {
                    case "Laugh All You Want Battle":
                        return SpireReturn.Return(uiStrings().TEXT[1]);
                    case "The Surrounded Battle":
                        return SpireReturn.Return(uiStrings().TEXT[2]);
                    case CorruptedWitheredBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[3]);
                    case LeftHandBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[4]);
                    case CandleKnightBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[5]);
                    case LastKheshigBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[6]);
                    case BloodKnightBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[7]);
                    case ArmorlessSquadBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[8]);
                    case LazuriteSquadBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[9]);
                    case WanderingKnightBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[10]);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = EventHelper.class, method = "getEventName")
    public static class GetEventNamePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(String key) {
            if (key != null) {
                switch (key) {
                    case CorruptedWitheredBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[11]);
                    case LeftHandBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[12]);
                    case CandleKnightBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[13]);
                    case LastKheshigBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[14]);
                    case BloodKnightBattle.ID:
                        return SpireReturn.Return(uiStrings().TEXT[15]);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
