package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.patches.NearlTags;

public class SuperWeakPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:SuperWeakPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean justApplied = false;

    public SuperWeakPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.DEBUFF;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/superweak power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/superweak power 32.png"), 0, 0, 32, 32);
        updateDescription();
        this.priority = 120;
        this.justApplied = true;
    }
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "nearlmod:SuperWeak"));
            } else {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, "nearlmod:SuperWeak", 1));
            }

        }
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        AbstractCard card = AbstractDungeon.player.cardInUse;
        if (type == DamageInfo.DamageType.NORMAL && !card.hasTag(NearlTags.IS_FRIEND_CARD)) {
            return damage * 0.5F;
        } else {
            return damage;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SuperWeakPower(owner, amount);
    }
}
