package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Whislash;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class WayToHome extends AbstractNearlCard {
    public static final String ID = "nearlmod:WayToHome";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/waytohome.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int POWER_GAIN = 5;

    public WayToHome() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = POWER_GAIN;
        tags.add(NearlTags.IS_SUMMON_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonFriendAction(new Whislash()));
        addToBot(new DrawCardAction(p, 1));
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WayToHome();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
