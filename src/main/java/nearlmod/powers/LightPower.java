package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
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
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        AbstractPlayer p = (AbstractPlayer)this.owner;
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        else description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
    }

    public void useLight(AbstractPlayer p, AbstractCreature m) {
        if (p.getPower("nearlmod:Poem'sLooks") != null) {
            addToBot(new ApplyPowerAction(p, p, new ShadowPower(p, amount)));
        }
        amountForBattle += amount;
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new DamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.LIGHTNING));
        } else {
            addToBot(new AddTemporaryHPAction(p, p, amount));
        }
        addToBot(new RemoveSpecificPowerAction(p, p, this));
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
