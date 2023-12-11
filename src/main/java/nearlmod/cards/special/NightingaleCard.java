package nearlmod.cards.special;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;

public class NightingaleCard extends AbstractNearlCard {
    public static final String ID = "nearlmod:NightingaleCard";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nightingalecard.png";
    private static final int COST = 0;
    private static final int TEMP_HP = 6;
    private static final int BLOCK_AMT = 6;
    private static final int UPGRADE_PLUS_HP = 1;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    public NightingaleCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        magicNumber = baseMagicNumber = TEMP_HP;
        block = baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AddTemporaryHPAction(p, p, magicNumber));
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NightingaleCard();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HP);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
