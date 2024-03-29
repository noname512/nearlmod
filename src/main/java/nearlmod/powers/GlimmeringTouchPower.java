package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Viviana;
import nearlmod.vfx.GlimmeringTouchEffect;

public class GlimmeringTouchPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:GlimmeringTouchPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int damage;
    public int lightAmt;

    public GlimmeringTouchPower(AbstractCreature owner, int amount, int damage, int lightAmt) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/glimmeringtouch power 84.png"), 0, 0, 84, 84);// TODO
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/glimmeringtouch power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        this.damage = damage;
        this.lightAmt = lightAmt;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[4] + damage + DESCRIPTIONS[2] + lightAmt + DESCRIPTIONS[3];
        else
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + damage + DESCRIPTIONS[2] + lightAmt + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurn() {
        amount--;
        if (amount == 0) {
            AbstractDungeon.effectList.add(new GlimmeringTouchEffect());
            addToTop(new PureDamageAllEnemiesAction(owner, damage, Viviana.ORB_ID + AbstractFriendCard.damageSuffix, AbstractGameAction.AttackEffect.FIRE, DamageInfo.DamageType.THORNS));
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToTop(new ApplyPowerAction(owner, owner, new LightPower(owner, lightAmt)));
        }
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new GlimmeringTouchPower(owner, amount, damage, lightAmt);
    }
}
