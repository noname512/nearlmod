package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Penance;

public class WreathedInThornsPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:WreathedInThornsPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WreathedInThornsPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/wreathedinthorns power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/wreathedinthorns power 48.png"), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != owner && TempHPField.tempHp.get(owner) > 0) {
            int trust = -1;
            for (AbstractOrb o : AbstractDungeon.player.orbs)
                if (o instanceof Penance) {
                    trust = ((Penance) o).getTrustAmount();
                    break;
                }
            if (trust != -1) {
                flash();
                DamageInfo newInfo = new DamageInfo(null, amount + trust, DamageInfo.DamageType.THORNS);
                newInfo.name = Penance.ORB_ID + AbstractFriendCard.damageSuffix;
                addToTop(new DamageAction(info.owner, newInfo, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
        }
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new WreathedInThornsPower(owner, amount);
    }
}
