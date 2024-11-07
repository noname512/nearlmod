package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ColdPower;

public class ArtificialSnowfall extends AbstractFriendCard {
    public static final String ID = "nearlmod:ArtificialSnowfall";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/artificialsnowfall.png";
    private static final int COST = 1;
    private static final int BASIC_DAMAGE = 9;
    private static final int TRIGGER_DAMAGE = 27;
    private static final int UPG_BASIC = 2;
    private static final int UPG_TRIGGER = 9;

    public ArtificialSnowfall() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Aurora");
        magicNumber = baseMagicNumber = BASIC_DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = TRIGGER_DAMAGE;
        isSecondMagicNumberUseTrust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if ((m.hasPower(ColdPower.POWER_ID)) && (m.getPower(ColdPower.POWER_ID).amount >= 2)) {
            DamageInfo info = new DamageInfo(p, secondMagicNumber);
            info.name = belongFriend + AbstractFriendCard.damageSuffix;
            addToBot(new DamageAction(m, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        else {
            DamageInfo info = new DamageInfo(p, magicNumber);
            info.name = belongFriend + AbstractFriendCard.damageSuffix;
            addToBot(new DamageAction(m, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot(new ApplyPowerAction(m, p, new ColdPower(m, 1)));
        }
    }

    @Override
    public void applyPowers() {
        int temp = baseMagicNumber;
        baseMagicNumber = baseSecondMagicNumber;
        super.applyPowers();
        secondMagicNumber = magicNumber;
        isSecondMagicNumberModified = isMagicNumberModified;
        baseMagicNumber = temp;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int temp = baseMagicNumber;
        baseMagicNumber = baseSecondMagicNumber;
        super.calculateCardDamage(mo);
        secondMagicNumber = magicNumber;
        isSecondMagicNumberModified = isMagicNumberModified;
        baseMagicNumber = temp;
        super.calculateCardDamage(mo);
    }

    @Override
    public boolean extraTriggered() {
        for (AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if ((!m.isDeadOrEscaped()) && (m.hasPower(ColdPower.POWER_ID)) && (m.getPower(ColdPower.POWER_ID).amount >= 2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArtificialSnowfall();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_BASIC);
            upgradeSecondMagicNumber(UPG_TRIGGER);
        }
    }
}
