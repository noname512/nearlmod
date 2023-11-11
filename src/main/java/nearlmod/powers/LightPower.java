package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;

public class LightPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:LightPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

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

    void useLight(AbstractPlayer p, AbstractCreature m) {
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.LIGHTNING));
        } else {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.amount));
        }
        this.amount = 0;
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, this));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) { // use light before play card
        if (card.hasTag(NearlTags.IS_USE_LIGHT_BEFORE)) {
            useLight((AbstractPlayer)this.owner, m);
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.hasTag(NearlTags.IS_USE_LIGHT_AFTER)) {
            useLight((AbstractPlayer)this.owner, action.target);
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
