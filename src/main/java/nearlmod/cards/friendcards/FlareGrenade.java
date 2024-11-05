package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;

public class FlareGrenade extends AbstractFriendCard {
    public static final String ID = "nearlmod:FlareGrenade";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/flaregrenade.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 5;
    private static final int LIGHT_GAIN = 3;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int UPGRADE_PLUS_LIGHT = 1;

    public FlareGrenade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Horn");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = LIGHT_GAIN;
        isSecondMagicNumberUseTrust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        int targetCnt = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (!mo.isDeadOrEscaped())
                targetCnt++;
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, secondMagicNumber * targetCnt)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlareGrenade();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }
}
