package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.patches.NearlTags;

public class Dishes extends AbstractNearlCard {
    public static final String ID = "nearlmod:Dishes";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/dishes.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 3;
    private static final int UPGRADE_PLUS_BLOCK = 1;
    private static final int CARD_DRAW = 1;

    public Dishes() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = CARD_DRAW;
        tags.add(NearlTags.IS_FOOD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new DrawCardAction(CARD_DRAW));
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Dishes();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
