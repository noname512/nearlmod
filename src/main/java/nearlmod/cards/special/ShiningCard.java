package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nearlmod.cards.AbstractNearlCard;

public class ShiningCard extends AbstractNearlCard {
    public static final String ID = "nearlmod:ShiningCard";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/shiningcard.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 8;
    private static final int WEAK_AMT = 1;
    private static final int UPGRADE_PLUS_ATK = 1;
    private static final int UPGRADE_PLUS_WEAK = 1;

    public ShiningCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        exhaust = true;
        magicNumber = baseMagicNumber = WEAK_AMT;
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShiningCard();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            upgradeDamage(UPGRADE_PLUS_ATK);
        }
    }
}
