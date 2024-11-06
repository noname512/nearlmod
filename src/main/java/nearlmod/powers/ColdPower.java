package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Field;

public class ColdPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:Cold";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ColdPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.DEBUFF;
        updateDescription();
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/coldpower 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/coldpower 48.png"), 0, 0, 48, 48);
        priority = 0;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= 2) {
            amount = 2;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount * 25) + DESCRIPTIONS[1];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (owner instanceof AbstractMonster) {
            try {
                Field field = AbstractMonster.class.getDeclaredField("isMultiDmg");
                field.setAccessible(true);
                if (field.getBoolean(owner)) {
                    return damage * (1.0F - 0.25F * amount);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return damage;
    }

    @Override
    public AbstractPower makeCopy() {
        return new ColdPower(owner, amount);
    }
}
