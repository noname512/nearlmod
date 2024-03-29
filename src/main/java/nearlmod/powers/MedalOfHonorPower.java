package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MedalOfHonorPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:MedalOfHonorPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int totalAmount;
    public int exhaustedAmount;

    public MedalOfHonorPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/medalofhonor power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("resources/nearlmod/images/powers/medalofhonor power 48.png"), 0, 0, 48, 48);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    public MedalOfHonorPower(AbstractCreature owner) {
        this(owner, 1);
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        else
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.STATUS) {
            card.exhaust = true;
            action.exhaustCard = true;
        }
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (card.type == AbstractCard.CardType.STATUS) {
            if (totalAmount == 0) {
                flash();
                addToBot(new DrawCardAction(amount));
            } else {
                exhaustedAmount++;
                if (exhaustedAmount >= totalAmount) {
                    flash();
                    addToBot(new DrawCardAction(amount * exhaustedAmount));
                    totalAmount = 0;
                    exhaustedAmount = 0;
                }
            }
        }
    }

    public void notifyExhaustAmount(int amount) {
        totalAmount = amount;
        exhaustedAmount = 0;
    }

    @Override
    public AbstractPower makeCopy() {
        return new MedalOfHonorPower(owner);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "hasRelic")
    public static class MedalOfHonorPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractPlayer __instance, String targetID) {
            if (targetID.equals("Medical Kit") && __instance.hasPower("nearlmod:MedalOfHonorPower"))
                return SpireReturn.Return(true);
            return SpireReturn.Continue();
        }
    }
}
