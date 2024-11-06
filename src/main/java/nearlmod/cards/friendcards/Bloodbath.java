package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.BloodbathPower;

public class Bloodbath extends AbstractFriendCard {
    public static final String ID = "nearlmod:Bloodbath";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/bloodbath.png";
    private static final int COST = 0;
    private static final int POWER_GAIN = 4;
    private static final int UPGRADE_PLUS_POWER = 2;

    public Bloodbath() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Horn");
        secondMagicNumber = POWER_GAIN;

        this.bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        this.bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;
        tags.add(NearlTags.IS_UNIQUE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Horn.uniqueUsed = true;
        addToBot(new ApplyPowerAction(p, p, new BloodbathPower(p, secondMagicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bloodbath();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_POWER);
            initializeDescription();
        }
    }
}
