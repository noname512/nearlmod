package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import nearlmod.relics.Lighthouse;
import nearlmod.relics.UpgradedCoreCaster;
import nearlmod.stances.AtkStance;

public class LightPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:LightPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static int amountForBattle;

    public LightPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("rhinemod/images/powers/light power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("rhinemod/images/powers/light power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
        AbstractPlayer p = AbstractDungeon.player;
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            p.state.setAnimation(1, "Gain_Light", false);
//            p.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void updateDescription() {
        AbstractPlayer p = (AbstractPlayer)this.owner;
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        else description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
    }

    public static int getAmount() {
        int amount = 0;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(LightPower.POWER_ID))
            amount = p.getPower(LightPower.POWER_ID).amount;
        if (p.hasRelic(UpgradedCoreCaster.ID)) {
            amount += UpgradedCoreCaster.EXTRA_VAL;
            p.getRelic(UpgradedCoreCaster.ID).flash();
        }
        return amount;
    }

    public static void onExhaustLight(boolean isUse) {
        AbstractPlayer p = AbstractDungeon.player;
        if (isUse) {
            if (p.hasPower(BladeOfBlazingSunPower.POWER_ID) && p.stance.ID.equals(AtkStance.STANCE_ID)) {
                if (p.hasPower(LightPower.POWER_ID) || p.hasRelic(UpgradedCoreCaster.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, p.getPower(BladeOfBlazingSunPower.POWER_ID).amount)));
                    p.getPower(BladeOfBlazingSunPower.POWER_ID).flash();
                }
            }
            if (p.hasRelic(Lighthouse.ID) && Lighthouse.isFirstTime && (p.hasPower(LightPower.POWER_ID) || p.hasRelic(UpgradedCoreCaster.ID))) {
                Lighthouse.isFirstTime = false;
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
                p.getRelic(Lighthouse.ID).flash();
            }
        }
        if (!p.hasPower(LightPower.POWER_ID)) return;
        AbstractPower power = p.getPower(LightPower.POWER_ID);
        if (isUse) {
            amountForBattle += power.amount;
            if (p.hasPower(PoemsLooksPower.POWER_ID))
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadowPower(p, power.amount)));
            if (p.hasPower(PegasusFormPower.POWER_ID)) {
                int val = p.getPower(PegasusFormPower.POWER_ID).amount;
                val = power.amount * val / 3;
                if (val > 0)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GainLightNextTurnPower(p, val)));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, power));
    }

    public void useLight(AbstractPlayer p, AbstractCreature m) {
        int realAmount = amount;
        onExhaustLight(true);
        if (AbstractDungeon.player.hasRelic(UpgradedCoreCaster.ID))
            realAmount += UpgradedCoreCaster.EXTRA_VAL;
        if (m != null) {
            addToBot(new DamageAction(m, new DamageInfo(p, realAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.LIGHTNING));
        } else {
            addToBot(new GainBlockAction(p, p, realAmount));
        }
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (newStance.ID.equals(AtkStance.STANCE_ID)) description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        else description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new LightPower(owner, amount);
    }
}
