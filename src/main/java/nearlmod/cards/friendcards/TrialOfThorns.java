package nearlmod.cards.friendcards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Horn;
import nearlmod.orbs.Penance;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class TrialOfThorns extends AbstractFriendCard {
    public static final String ID = "nearlmod:TrialOfThorns";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/trialofthorns.png";
    private static final int COST = 3;
    private static final int TEMP_HP = 18;
    private static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_HP = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    public TrialOfThorns() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Penance");
        secondMagicNumber = baseSecondMagicNumber = TEMP_HP;
        magicNumber = baseMagicNumber = ATTACK_DMG;
        this.bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        this.bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;

        tags.add(NearlTags.IS_UNIQUE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        setUniqueUsed(Penance.ORB_ID);
        addToBot(new AddTemporaryHPAction(p, p, secondMagicNumber));
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + damageSuffix;
        addToBot(new DamageAction(m, info));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TrialOfThorns();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_HP);
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
