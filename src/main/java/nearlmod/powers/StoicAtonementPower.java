package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Penance;

public class StoicAtonementPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:StoicAtonementPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public StoicAtonementPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/stoicatonement power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/stoicatonement power 48.png"), 0, 0, 48, 48);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new StoicAtonementPower(owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!Nearl.attackCardPlayedThisTurn) {
            addToTop(new ApplyPowerAction(owner, owner, new Sheltered(owner)));
            for (AbstractOrb o : AbstractDungeon.player.orbs)
                if (o instanceof Penance) {
                    amount += ((Penance) o).getTrustAmount();
                    break;
                }
            addToTop(new PureDamageAllEnemiesAction(owner, amount, Penance.ORB_ID + AbstractFriendCard.damageSuffix));
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
