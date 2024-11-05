package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;

public class TempestCommand extends AbstractFriendCard {
    public static final String ID = "nearlmod:TempestCommand";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/tempestcommand.png";
    private static final int COST = 0;
    private static final int EXTRA_TRUST = 2;

    public TempestCommand() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Horn");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int friendCnt = upgraded? EXTRA_TRUST : 0;
        for (AbstractOrb o : p.orbs)
            if (o instanceof AbstractFriend)
                friendCnt++;
        for (AbstractOrb o : p.orbs)
            if (o instanceof Horn)
                ((Horn) o).applyStrength(friendCnt);
    }

    @Override
    public AbstractCard makeCopy() {
        return new TempestCommand();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
