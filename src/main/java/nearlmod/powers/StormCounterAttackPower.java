package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.PureDamageAllEnemiesAction;

public class StormCounterAttackPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:StormCounterAttackPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractPlayer p;

    public StormCounterAttackPower(AbstractPlayer player, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = player;
        this.p = player;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/stormcounterattack power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/stormcounterattack power 48.png"), 0, 0, 48, 48);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        if (p.currentBlock > 0) {
            int dmg = p.currentBlock * amount;
            addToBot(new PureDamageAllEnemiesAction(p, dmg, "", AbstractGameAction.AttackEffect.NONE, DamageInfo.DamageType.THORNS));
        }
        addToBot(new RemoveSpecificPowerAction(p, p, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new StormCounterAttackPower(p, amount);
    }
}
