package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;

public class RadianceConverging extends AbstractNearlCard {
    public static final String ID = "nearlmod:RadianceConverging";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/radianceconverging.png";
    private static final int COST = 2;
    private static final int LIGHT_AMT = 10;
    private static final int LIGHT_EX = 6;
    private static final int UPGRADE_PLUS_EX = 4;

    public RadianceConverging() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = LIGHT_AMT;
        secondMagicNumber = baseSecondMagicNumber = LIGHT_EX;
        tags.add(NearlTags.IS_GAIN_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber + AbstractDungeon.actionManager.cardsPlayedThisTurn.size() * secondMagicNumber)));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int amount = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        if (amount == 1)
            rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + amount + EXTENDED_DESCRIPTION[1];
        else
            rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + amount + EXTENDED_DESCRIPTION[2];
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new RadianceConverging();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_EX);
        }
    }
}
