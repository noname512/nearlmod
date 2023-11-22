package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NightmarePower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.BombardmentStudiesPower;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class BombardmentStudies extends AbstractFriendCard {
    public static final String ID = "nearlmod:BombardmentStudies";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flameheart.png";
    private static final int COST = 1;
    private static final int ADDITION_VAL = 1;
    private static final int UPGRADE_PLUS_VAL = 1;

    public BombardmentStudies() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Ashlock");
        secondMagicNumber = baseSecondMagicNumber = ADDITION_VAL;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = new FocusedBombardment();
        if (upgraded)
            card.upgrade();
        addToBot(new ApplyPowerAction(p, p, new BombardmentStudiesPower(p, secondMagicNumber)));
        addToBot(new ApplyPowerAction(p, p, new NightmarePower(p, 1, card)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BombardmentStudies();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_VAL);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
