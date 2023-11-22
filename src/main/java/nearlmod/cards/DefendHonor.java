package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;

public class DefendHonor extends AbstractNearlCard {
    public static final String ID = "nearlmod:DefendHonor";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 1;
    private static final int EXTRA_DMG = 3;
    private String actualDescription;

    public DefendHonor() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = 0;
        actualDescription = DESCRIPTION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        rawDescription = actualDescription;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage += mo.getIntentDmg();
        super.calculateCardDamage(mo);
        baseDamage -= mo.getIntentDmg();
        isDamageModified = (damage != baseDamage);
        rawDescription = actualDescription + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new DefendHonor();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(EXTRA_DMG);
            actualDescription = rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
