package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.characters.Nearl;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.StoicAtonementPower;

public class StoicAtonement extends AbstractFriendCard {
    public static final String ID = "nearlmod:StoicAtonement";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/stoicatonement.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 12;
    private static final int UPGRADE_PLUS_DMG = 5;

    public StoicAtonement() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Penance");
        secondMagicNumber = baseSecondMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StoicAtonementPower(p, secondMagicNumber)));
        Nearl.penanceCardPlayedThisTurn = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new StoicAtonement();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
