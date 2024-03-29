package nearlmod.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Blemishine;
import nearlmod.patches.NearlTags;

public class Beginning extends AbstractNearlCard {
    public static final String ID = "nearlmod:Beginning";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/beginning.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public Beginning() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);

        previewList = Nearl.getFriendCard(Blemishine.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonFriendAction(new Blemishine()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Beginning();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
