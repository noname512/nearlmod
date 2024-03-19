package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FriendShelterPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:FriendShelterPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FriendShelterPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/friendshelter power 84.png"), 0, 0, 84, 84); // TODO
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/friendshelter power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        priority = 120;
        updateDescription();
    }
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    public AbstractPower makeCopy() {
        return new FriendShelterPower(owner);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DamagePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer _inst, DamageInfo info) {
            if (info.output > 0 && _inst.hasPower(POWER_ID)) {
                _inst.getPower(POWER_ID).flash();
                info.output = 0;
            }
        }
    }
}
