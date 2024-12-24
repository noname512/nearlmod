package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import nearlmod.patches.CurseRelicPatch;

public class TangibleTerror extends CustomRelic {

    public static final String ID = "nearlmod:TangibleTerror";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/tangibleterror.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/tangibleterror_p.png");
    public TangibleTerror() {
        super(ID, IMG, IMG_OUTLINE, CurseRelicPatch.CURSE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TangibleTerror();
    }

    @SpirePatch(clz = DamageInfo.class, method = "applyPowers")
    public static class DamageInfoPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public void Insert(DamageInfo _inst, AbstractCreature owner, AbstractCreature target, @ByRef float[] tmp) {
            if (_inst.type == DamageInfo.DamageType.NORMAL && target.hasPower(TangibleTerror.ID)) {
                tmp[0] *= 1.1F;
                if (_inst.base != tmp[0]) {
                    _inst.isModified = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "powers");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
    public static class AbstractMonsterPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public void Insert(AbstractMonster _inst, int dmg, @ByRef float[] tmp) {
            if (AbstractDungeon.player.hasPower(TangibleTerror.ID)) {
                tmp[0] *= 1.1F;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
