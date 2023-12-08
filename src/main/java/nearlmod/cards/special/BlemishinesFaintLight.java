package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.powers.FriendShelterPower;

public class BlemishinesFaintLight extends AbstractNearlCard {
    public static final String ID = "nearlmod:Blemishine'sFaintLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/majestylight.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    public BlemishinesFaintLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FriendShelterPower(p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlemishinesFaintLight();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
