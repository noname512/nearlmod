package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class OnDamagedPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer _inst, DamageInfo info) {
            if (_inst.hasPower(WreathedInThornsPower.POWER_ID) && TempHPField.tempHp.get(_inst) > 0 &&
                    info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != _inst) {
                int trust = -1;
                for (AbstractOrb o : AbstractDungeon.player.orbs)
                    if (o instanceof Penance) {
                        trust = ((Penance) o).getTrustAmount();
                        break;
                    }
                if (trust != -1) {
                    _inst.getPower(WreathedInThornsPower.POWER_ID).flash();
                    DamageInfo newInfo = new DamageInfo(null, _inst.getPower(WreathedInThornsPower.POWER_ID).amount + trust, DamageInfo.DamageType.THORNS);
                    newInfo.name = Penance.ORB_ID + AbstractFriendCard.damageSuffix;
                    AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner, newInfo, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new WreathedInThornsPower(owner, amount);
    }
}
