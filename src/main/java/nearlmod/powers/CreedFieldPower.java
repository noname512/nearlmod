package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CreedFieldPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:CreedFieldPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int dmgDec;

    public CreedFieldPower(AbstractCreature owner, int amount, int dmgDec) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.dmgDec = dmgDec;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/sanctuary power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/sanctuary power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = amount + DESCRIPTIONS[0] + dmgDec + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        amount--;
        updateDescription();
        if (amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS) {
            if (damageAmount > dmgDec)
                return damageAmount - dmgDec;
            else
                return 0;
        }
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new CreedFieldPower(owner, amount, dmgDec);
    }
}
