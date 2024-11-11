package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.PeterheimMiddleSchoolAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class PeterheimMiddleSchool extends AbstractFriendCard {
    public static final String ID = "nearlmod:PeterheimMiddleSchool";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/peterheim.png";
    private static final int COST = 0;
    private static final int SELECT_NUM = 2;
    private static final int UPGRADE_PLUS_NUM = 1;

    public PeterheimMiddleSchool() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Gummy");
        secondMagicNumber = baseSecondMagicNumber = SELECT_NUM;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PeterheimMiddleSchoolAction(secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PeterheimMiddleSchool();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_NUM);
        }
    }
}
