package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;

public class LiteratureStorm extends AbstractFriendCard {
    public static final String ID = "nearlmod:LiteratureStorm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/literaturestorm.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 9;
    private static final int DAMAGE_UPG = 3;
    private static final int STRENGTH_GET = 4;
    private static final int STRENGTH_EXTRA_GET = 2;

    public LiteratureStorm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Gummy");
        magicNumber = baseMagicNumber = DAMAGE_AMT;
        secondMagicNumber = baseSecondMagicNumber = STRENGTH_GET;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, secondMagicNumber)));
    }
    @Override
    public AbstractCard makeCopy() {
        return new LiteratureStorm();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(DAMAGE_UPG);
            upgradeSecondMagicNumber(STRENGTH_EXTRA_GET);
        }
    }
}
