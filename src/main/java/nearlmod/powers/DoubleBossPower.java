package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.monsters.CorruptKnight;

public class DoubleBossPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:DoubleBoss";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean FriendDead = false;
    public DoubleBossPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        this.loadRegion("curiosity");
        priority = 100;
    }

    public void onSpecificTrigger() {
        FriendDead = true;
        updateDescription();
    }
    @Override
    public void updateDescription() {
        if (owner instanceof CorruptKnight) {
            if (!FriendDead) {
                description = DESCRIPTIONS[0];
            }
            else {
                description = DESCRIPTIONS[1];
            }
        }
        else {
            if (!FriendDead) {
                description = DESCRIPTIONS[2];
            }
            else {
                description = DESCRIPTIONS[1];
            }
        }
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (FriendDead) {
            return damage * 1.8F;
        }
        else {
            return damage;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DoubleBossPower(owner);
    }
}
