package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.SummonFriendAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class TempestPlatoon extends AbstractNearlCard {
    public static final String ID = "nearlmod:TempestPlatoon";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/tempestplatoon.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int TRUST_GAIN = 1;
    private static final int EXTRA_TRUST = 2;

    public TempestPlatoon() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = TRUST_GAIN;
        secondMagicNumber = baseSecondMagicNumber = EXTRA_TRUST;
        tags.add(NearlTags.IS_SUMMON_CARD);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Horn.ORB_ID;

        previewList = Nearl.getFriendCard(Horn.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonFriendAction(new Horn(magicNumber + (upgraded? secondMagicNumber : 0))));
        for (AbstractOrb o : p.orbs)
            if (o instanceof AbstractFriend && !(o instanceof Horn))
                ((AbstractFriend) o).applyStrength(magicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new TempestPlatoon();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
