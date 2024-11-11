package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.RemoveAllTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BloodbathPower extends AbstractPower implements CloneablePowerInterface, OnLoseTempHpPower {
    public static final String POWER_ID = "nearlmod:BloodbathPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BloodbathPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/bloodbath 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/bloodbath 48.png"), 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BloodbathPower(owner, amount);
    }

    @Override
    public int onLoseTempHp(DamageInfo damageInfo, int damage) {
        int temporaryHealth = TempHPField.tempHp.get(owner);
        if (damage >= temporaryHealth) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
            addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount)));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return damage;
    }

    @SpirePatch(clz = RemoveAllTemporaryHPAction.class, method = "update")
    public static class RemoveAllTemporaryHPActionPatch {
        @SpirePrefixPatch
        public static void Prefix(RemoveAllTemporaryHPAction _inst) {
            int hp = TempHPField.tempHp.get(_inst.target);
            for (AbstractPower po : _inst.target.powers)
                if (po instanceof BloodbathPower)
                    ((BloodbathPower) po).onLoseTempHp(new DamageInfo(null, hp, DamageInfo.DamageType.HP_LOSS), hp);
        }
    }
}
