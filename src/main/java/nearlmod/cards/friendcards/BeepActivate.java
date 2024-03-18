package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import nearlmod.patches.AbstractCardEnum;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class BeepActivate extends AbstractFriendCard {
    public static final String ID = "nearlmod:BeepActivate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/beepactivate.png";
    private static final int COST = 0;
    private static final int BLOCK_GAIN = 2;
    private static final int BLOCK_NUM = 2;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    public BeepActivate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:JusticeKnight");
        secondMagicNumber = baseSecondMagicNumber = BLOCK_GAIN;
        magicNumber = baseMagicNumber = BLOCK_NUM;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, secondMagicNumber)));
        addToBot(new GainBlockAction(p, p, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BeepActivate();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_BLOCK);
        }
    }
}
