package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.BreakThroughFettersAction;
import nearlmod.patches.AbstractCardEnum;

public class BreakThroughFetters extends AbstractNearlCard {
    public static final String ID = "nearlmod:BreakThroughFetters";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/breakthroughfetters.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public BreakThroughFetters() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BreakThroughFettersAction());
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        for (AbstractCard c : p.drawPile.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                return true;
        for (AbstractCard c : p.discardPile.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                return true;
        for (AbstractCard c : p.hand.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                return true;
        this.cantUseMessage = CardCrawlGame.languagePack.getUIString("nearlmod:Can'tUseMessage").TEXT[1];
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BreakThroughFetters();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
