package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import nearlmod.cards.special.LightCard;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.GainLightNextTurnPower;
import nearlmod.powers.LightPower;

public class SheathedBlade extends AbstractNearlCard {
    public static final String ID = "nearlmod:SheathedBlade";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/majestylight.png";
    private static final int COST = 0;

    public SheathedBlade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        cardsToPreview = new LightCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(LightPower.POWER_ID)) {
            AbstractPower power = p.getPower(LightPower.POWER_ID);
            int saveAmt = power.amount / 2;
            power.stackPower(-saveAmt);
            addToBot(new ApplyPowerAction(p, p, new GainLightNextTurnPower(p, saveAmt)));
        }
        addToBot(new MakeTempCardInHandAction(new LightCard()));
        if (upgraded) {
            AbstractCard card = new SwitchType();
            card.upgrade();
            card.exhaust = true;
            card.isEthereal = true;
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SheathedBlade();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
