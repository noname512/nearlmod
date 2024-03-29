package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.DawnAction;
import nearlmod.patches.AbstractCardEnum;

public class Dawn extends AbstractNearlCard {
    public static final String ID = "nearlmod:Dawn";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/dawn.png";
    private static final int COST = 3;
    private static final int ATTACK_DMG = 15;
    private static final int UPGRADE_PLUS_DMG = 5;

    public Dawn copiedFrom;

    public Dawn() {
        this(null, 0);
    }

    public Dawn(Dawn from, int upgrades) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ALL_ENEMY);
        damage = baseDamage = ATTACK_DMG;
        isMultiDamage = true;
        exhaust = true;
        isEthereal = true;
        timesUpgraded = upgrades;
        copiedFrom = from;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        addToBot(new DawnAction(new DamageInfo(p, damage, damageTypeForTurn), this));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Dawn(this, timesUpgraded);
    }

    @Override
    public void upgrade() {
        upgradeDamage(UPGRADE_PLUS_DMG);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + timesUpgraded;
        this.initializeTitle();
    }
    @Override
    public boolean canUpgrade() {
        return true;
    }

}
