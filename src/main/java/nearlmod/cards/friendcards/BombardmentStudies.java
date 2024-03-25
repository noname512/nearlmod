package nearlmod.cards.friendcards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.BombardmentStudiesPower;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class BombardmentStudies extends AbstractFriendCard {
    public static final String ID = "nearlmod:BombardmentStudies";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/bombardmentstudies.png";
    private static final int COST = 1;
    private static final int ADDITION_VAL = 1;
    private static final int UPGRADE_PLUS_VAL = 1;

    public BombardmentStudies() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Ashlock");
        secondMagicNumber = baseSecondMagicNumber = ADDITION_VAL;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
        cardsToPreview = new FocusedBombardment();
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bombCnt = 0;
        int bombPlusCnt = 0;
        if (upgraded) bombPlusCnt = 1;
        else bombCnt = 1;
        if (p.hasPower(BombardmentStudiesPower.POWER_ID)) {
            BombardmentStudiesPower power = (BombardmentStudiesPower) p.getPower(BombardmentStudiesPower.POWER_ID);
            AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, BombardmentStudiesPower.NAME));
            power.stackPower(secondMagicNumber);
            power.bombCnt += bombCnt;
            power.bombPlusCnt += bombPlusCnt;
            power.updateDescription();
            AbstractDungeon.onModifyPower();
        } else {
            addToBot(new ApplyPowerAction(p, p, new BombardmentStudiesPower(p, secondMagicNumber, bombCnt, bombPlusCnt)));
        }
        BaseMod.MAX_HAND_SIZE --;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BombardmentStudies();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_VAL);
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
