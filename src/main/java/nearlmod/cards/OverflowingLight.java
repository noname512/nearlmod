package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.special.LightCard;
import nearlmod.patches.AbstractCardEnum;

public class OverflowingLight extends AbstractNearlCard {
    public static final String ID = "nearlmod:OverflowingLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/overflowinglight.png";
    private static final int COST = 0;
    private static final int CARD_AMT = 2;
    private static final int UPGRADE_PLUS_CARD = 1;

    public OverflowingLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = CARD_AMT;
        cardsToPreview = new LightCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(new LightCard(), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new OverflowingLight();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARD);
        }
    }
}
