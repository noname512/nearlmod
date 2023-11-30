package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;

public class BlazingSunsObeisance extends AbstractNearlCard {
    public static final String ID = "nearlmod:BlazingSun'sObeisance";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    public static final String IMG_PATH = "images/cards/blazingsunsobeisance.png";
    private String baseDescription;
    private static final int COST = 2;
    private static final int BASE_DMG = 19;

    public BlazingSunsObeisance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ALL_ENEMY);
        damage = baseDamage = BASE_DMG;
        magicNumber = baseMagicNumber = 0;
        baseDescription = DESCRIPTION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        addToBot(new PureDamageAllEnemiesAction(p, damage + magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        rawDescription = baseDescription;
        initializeDescription();
    }

    private void preUpd() {
        if (upgraded)
            baseMagicNumber = (LightPower.amountForBattle + 1) / 2;
        else
            baseMagicNumber = (LightPower.amountForBattle + 2) / 3;
        baseDamage += magicNumber;
    }

    private void postUpd(int formerDamage) {
        baseDamage -= magicNumber;
        magicNumber = damage - formerDamage;
        isMagicNumberModified = (magicNumber != baseMagicNumber);
        isDamageModified = (damage != baseDamage);
        damage = formerDamage;
        rawDescription = baseDescription + EXTENDED_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int formerDamage = damage;
        preUpd();
        super.applyPowers();
        postUpd(formerDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int formerDamage = damage;
        preUpd();
        super.calculateCardDamage(mo);
        postUpd(formerDamage);
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = baseDescription;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlazingSunsObeisance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            baseDescription = rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
