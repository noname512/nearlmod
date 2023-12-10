package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.friendcards.AbstractFriendCard;

import java.util.ArrayList;

public class DayBreakPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:DayBreakPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DayBreakPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/daybreak power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/daybreak power 48.png"), 0, 0, 48, 48);
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

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class DayBreakNoBlockPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance, AbstractMonster mo, boolean ___isMultiDamage) {
            if (__instance instanceof AbstractFriendCard || !AbstractDungeon.player.hasPower(DayBreakPower.POWER_ID)) return;
            if (!___isMultiDamage && mo != null) {
                if (mo.currentBlock <= 0 && __instance.damage > 0) {
                    __instance.damage += 3;
                    __instance.isDamageModified = true;
                }
            } else {
                ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
                for (int i = 0; i < m.size(); i++)
                    if (m.get(i).currentBlock <= 0 && __instance.multiDamage[i] > 0) {
                        __instance.multiDamage[i] += 3;
                        __instance.isDamageModified = true;
                    }
                __instance.damage = __instance.multiDamage[0];
            }
        }
    }
}
