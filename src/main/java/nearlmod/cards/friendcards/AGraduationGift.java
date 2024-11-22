package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.orbs.Gummy;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.CookingPower;

public class AGraduationGift extends AbstractFriendCard {
    public static final String ID = "nearlmod:AGraduationGift";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/agraduationgift.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 17;
    private static final int UPGRADE_DAMAGE = 4;

    public AGraduationGift() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Gummy");
        magicNumber = baseMagicNumber = DAMAGE_AMT;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + AbstractFriendCard.damageSuffix;
        addToBot(new DamageAction(m, info));
        if (extraTriggered(1)) {
            addToBot(new AddFriendCardToHandAction((AbstractFriendCard)makeStatEquivalentCopy()));
        }
    }
    @Override
    public boolean extraTriggered() {
        return extraTriggered(0);
    }
    public boolean extraTriggered(int prev) {
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() <= prev) {
            return false;
        }
        AbstractCard c = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1 - prev);
        if (c.type != CardType.ATTACK) {
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public AbstractCard makeCopy() {
        return new AGraduationGift();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE);
        }
    }
}
