package nearlmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.patches.NearlTags;

public class HoneyGingerbreadPower extends AbstractPower {
    public static final String POWER_ID = "nearlmod:HoneyGingerbreadPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HoneyGingerbreadPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/honeygingerbread power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/honeygingerbread power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.hasTag(NearlTags.IS_FOOD)) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new DeliciousPower(owner, amount)));
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
