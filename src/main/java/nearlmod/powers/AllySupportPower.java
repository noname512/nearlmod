package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.orbs.Fartooth;

public class AllySupportPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:AllySupportPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AllySupportPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/allysupport power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/allysupport power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.name != null && info.name.startsWith(Fartooth.ORB_ID)) return;
        for (AbstractOrb orb : ((AbstractPlayer)this.owner).orbs)
            if (orb instanceof Fartooth) {
                addToBot(new DamageAction(target, new DamageInfo(null, amount + ((Fartooth) orb).trustAmount, DamageInfo.DamageType.THORNS)));
                break;
            }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new AllySupportPower(owner, amount);
    }
}
