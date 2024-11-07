package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.Aurora;
import nearlmod.patches.AbstractCardEnum;

public class FrigidRespite extends AbstractFriendCard {
    public static final String ID = "nearlmod:FrigidRespite";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/frigidrespite.png";
    private static final int COST = 0;
    private static final int BLOCK_GAIN = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public FrigidRespite() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Aurora");
        secondMagicNumber = baseSecondMagicNumber = BLOCK_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb o : p.orbs)
            if (o instanceof Aurora)
                ((Aurora) o).startRespite(secondMagicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FrigidRespite();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_BLOCK);
        }
    }
}
