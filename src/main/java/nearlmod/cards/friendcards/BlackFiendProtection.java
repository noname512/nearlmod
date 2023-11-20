package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.WeakenAllAction;
import nearlmod.orbs.Shining;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.BFPPower;

public class BlackFiendProtection extends AbstractFriendCard {
    public static final String ID = "nearlmod:BlackFiendProtection";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flashfade.png";
    private static final int COST = 2;

    public BlackFiendProtection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Shining");
        bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Shining.uniqueUsed = true;
        addToBot(new ApplyPowerAction(p, p, new BFPPower(p), -1));
        if (upgraded) addToBot(new WeakenAllAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackFiendProtection();
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
