package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.CookingPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

import static nearlmod.patches.NearlTags.IS_FOOD;
import static nearlmod.patches.NearlTags.IS_UNIQUE_CARD;

public class Cooking extends AbstractFriendCard {
    public static final String ID = "nearlmod:Cooking";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/cooking.png";
    private static final int COST = 2;
    private static final int DISHES_GET = 2;
    private static final int UPGRADE_DISHSES = 1;

    public Cooking() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Gummy");
        secondMagicNumber = baseSecondMagicNumber = DISHES_GET;
        bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;
        tags.add(IS_UNIQUE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CookingPower(p, secondMagicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Cooking();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_DISHSES);
        }
    }
}
