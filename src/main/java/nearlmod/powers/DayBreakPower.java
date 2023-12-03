package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class DayBreakPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:DayBreakPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DayBreakPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DayBreakPower(owner);
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class DayBreakHasBlockPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            if (info.owner != null && info.owner.hasPower("nearlmod:DayBreakPower"))
                if (info.name == null || !info.name.endsWith(AbstractFriendCard.damageSuffix))
                    return SpireReturn.Return(damageAmount);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class DayBreakNoBlockPatch {
        @SpireInsertPatch(rloc = 4, localvars = "damageAmount")
        public static void Insert(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (__instance.currentBlock == 0)
                if (info.owner != null && info.owner.hasPower("nearlmod:DayBreakPower"))
                    if (info.name == null || !info.name.endsWith(AbstractFriendCard.damageSuffix))
                        damageAmount[0] += 3;
        }
    }
}
