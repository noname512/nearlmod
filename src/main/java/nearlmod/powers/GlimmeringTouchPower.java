package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
//            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            for (AbstractMonster ms : monsters) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(ms, new DamageInfo(owner, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
            }
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        }
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new GlimmeringTouchPower(owner, amount, damage);
    }
}
