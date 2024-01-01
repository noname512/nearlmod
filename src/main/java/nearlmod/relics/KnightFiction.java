package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.HashMap;
import java.util.HashSet;

public class KnightFiction extends CustomRelic {

    public static final String ID = "nearlmod:KnightFiction";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/knightfiction.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/knightfiction_p.png");
    private final HashMap<AbstractCard.CardType, Integer> playedCount = new HashMap<>();
    public static HashSet<AbstractCard.CardType> freeToPlayType = new HashSet<>();
    public KnightFiction() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        playedCount.clear();
        freeToPlayType.clear();
        stopPulse();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        playedCount.put(c.type, playedCount.getOrDefault(c.type, 0) + 1);
        int cnt = playedCount.get(c.type);
        if (cnt == 3) {
            beginLongPulse();
            freeToPlayType.add(c.type);
        } else if (cnt == 4) {
            freeToPlayType.remove(c.type);
            if (freeToPlayType.isEmpty()) stopPulse();
            playedCount.put(c.type, 0);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KnightFiction();
    }

    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class KnightFictionPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasRelic(KnightFiction.ID) && KnightFiction.freeToPlayType.contains(__instance.type))
                return SpireReturn.Return(true);
            return SpireReturn.Continue();
        }
    }
}
