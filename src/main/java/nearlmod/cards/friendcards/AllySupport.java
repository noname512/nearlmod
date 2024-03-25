package nearlmod.cards.friendcards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.AllySupportPower;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class AllySupport extends AbstractFriendCard {
    public static final String ID = "nearlmod:AllySupport";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/allysupport.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 2;
    private static final int UPGRADE_PLUS_DMG = 1;

    public AllySupport() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Fartooth");
        secondMagicNumber = baseSecondMagicNumber = ATTACK_DMG;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AllySupportPower(p, secondMagicNumber)));
        BaseMod.MAX_HAND_SIZE --;
    }

    @Override
    public AbstractCard makeCopy() {
        return new AllySupport();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
