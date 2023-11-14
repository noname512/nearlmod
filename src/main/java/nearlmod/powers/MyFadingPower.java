package nearlmod.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class MyFadingPower extends AbstractPower {
    public static final String POWER_ID = "nearlmod:Fading";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public MyFadingPower(AbstractCreature owner, int turns) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.type = PowerType.DEBUFF;
        this.updateDescription();
        this.loadRegion("fading");
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[2];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }

    }

    public void atStartOfTurn() {
        if (this.amount == 1 && !this.owner.isDying) {
            addToBot(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "Buffer"));
            addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, 99999999, DamageInfo.DamageType.HP_LOSS)));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, "nearlmod:Fading", 1));
            this.updateDescription();
        }

    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Fading");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
