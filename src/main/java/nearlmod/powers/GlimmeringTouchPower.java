package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Viviana;

import java.util.ArrayList;

public class GlimmeringTouchPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:GlimmeringTouchPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int damage;

    public GlimmeringTouchPower(AbstractCreature owner, int amount, int damage) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/light power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        this.damage = damage;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = amount + DESCRIPTIONS[0] + damage + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        amount--;
        if (amount == 0) {
            DamageInfo info = new DamageInfo(owner, damage);
            info.name = Viviana.ORB_ID + AbstractFriendCard.damageSuffix;
            for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters) {
                addToBot(new DamageAction(ms, info, AbstractGameAction.AttackEffect.FIRE));
            }
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ApplyPowerAction(owner, owner, new LightPower(owner, 10)));
        }
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new GlimmeringTouchPower(owner, amount, damage);
    }
}
