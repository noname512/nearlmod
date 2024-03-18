package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class FocusedBombardment extends AbstractFriendCard {
    public static final String ID = "nearlmod:FocusedBombardment";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/focusedbombardment.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 6;
    private static final int ATTACK_TIMES = 4;
    private static final int UPGRADE_PLUS_TIMES = 1;

    public FocusedBombardment() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Ashlock");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_TIMES;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        for (int i = 1; i <= secondMagicNumber; i++)
            addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p.getPower("nearlmod:BombardmentStudiesPower") != null) {
            int amount = p.getPower("nearlmod:BombardmentStudiesPower").amount;
            magicNumber += amount;
            secondMagicNumber = baseSecondMagicNumber + amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
        isSecondMagicNumberModified = (secondMagicNumber != baseSecondMagicNumber);
    }
    @Override
    public AbstractCard makeCopy() {
        return new FocusedBombardment();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_TIMES);
        }
    }
}
