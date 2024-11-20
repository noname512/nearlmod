package nearlmod.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;

public class IceCone extends AbstractNearlCard {
    public static final String ID = "nearlmod:IceCone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/icecone.png";
    private static final int COST = 0;

    public IceCone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.STATUS, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        for (AbstractCard c:AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (!(c instanceof IceCone)) {
                cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
                return false;
            }
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard c: p.hand.group) {
            c.costForTurn ++;
            c.isCostModifiedForTurn = (c.costForTurn != c.cost);
        }
    }

    @Override
    public void upgrade() {
    }
}
