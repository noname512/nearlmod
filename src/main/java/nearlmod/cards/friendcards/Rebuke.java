package nearlmod.cards.friendcards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.orbs.Whislash;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class Rebuke extends AbstractFriendCard {
    public static final String ID = "nearlmod:Rebuke";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/rebuke.png";
    private static final int COST = 1;
    private static final int CHANGE_CNT = 1;
    private static final int UPGRADE_COST = 0;

    public Rebuke() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Whislash");
        secondMagicNumber = baseSecondMagicNumber = CHANGE_CNT;
        bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;
        tags.add(NearlTags.IS_UNIQUE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Whislash.uniqueUsed = true;
        AtkStance.incNum++;
        DefStance.incNum++;
        if (p.stance.ID.equals(AtkStance.STANCE_ID))
            addToBot(new ChangeStanceAction(new DefStance()));
        else
            addToBot(new ChangeStanceAction(new AtkStance()));

        BaseMod.MAX_HAND_SIZE --;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Rebuke();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
