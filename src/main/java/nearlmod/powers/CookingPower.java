package nearlmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import nearlmod.cards.special.Dishes;
import nearlmod.stances.DefStance;

public class CookingPower extends AbstractPower {
    public static final String POWER_ID = "nearlmod:CookingPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CookingPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/cooking power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/cooking power 32.png"), 0, 0, 32, 32);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (newStance.ID.equals(DefStance.STANCE_ID)) {
            flash();
            addToBot(new MakeTempCardInDrawPileAction(new Dishes(), amount, true, true));
        }
    }
}
