package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.orbs.AbstractFriend;
import nearlmod.patches.AbstractCardEnum;

public class WhipSword extends AbstractFriendCard {
    public static final String ID = "nearlmod:WhipSword";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/whipsword.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 3;
    private static final int EXTRA_DMG = 8;

    public WhipSword() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Whislash");
        magicNumber = baseMagicNumber = EXTRA_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, secondMagicNumber, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));

        if (upgraded) {
            addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        }

        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend) {
                addToBot(new PureDamageAllEnemiesAction(p, secondMagicNumber + ((AbstractFriend)orb).getTrustAmount(), orb.ID + damageSuffix));
            }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhipSword();
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
