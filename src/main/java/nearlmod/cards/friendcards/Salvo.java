package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;

public class Salvo extends AbstractFriendCard {
    public static final String ID = "nearlmod:Salvo";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/tempestcommand.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 14;
    private static final int ATTACK_TIMES = 5;
    private static final int HP_LOSE = 20;
    private static final int UPGRADE_DMG = 3;

    public Salvo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Horn");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        baseSecondMagicNumber = secondMagicNumber = HP_LOSE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + damageSuffix;
        for (int i = 0; i < ATTACK_TIMES; i++) {
            addToBot(new DamageAction(m, info));
        }
        addToBot(new LoseHPAction(p, p, secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Salvo();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DMG);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
