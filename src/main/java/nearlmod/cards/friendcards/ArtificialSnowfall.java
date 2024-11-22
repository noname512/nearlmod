package nearlmod.cards.friendcards;

import com.badlogic.gdx.math.MathUtils;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ColdPower;
import nearlmod.relics.HandOfConqueror;

public class ArtificialSnowfall extends AbstractFriendCard {
    public static final String ID = "nearlmod:ArtificialSnowfall";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/artificialsnowfall.png";
    private static final int COST = 1;
    private static final int BASIC_DAMAGE = 9;
    private static final int UPG_BASIC = 2;
    private float damage_scale = 3.0F;

    public ArtificialSnowfall() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Aurora");
        magicNumber = baseMagicNumber = BASIC_DAMAGE;
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
        baseSecondMagicNumber = MathUtils.floor(baseMagicNumber * damage_scale);
        super.applyPowers();
        float damage = magicNumber;
        if (AbstractDungeon.player.hasRelic(HandOfConqueror.ID)) {
            int amount = AbstractDungeon.player.getRelic(HandOfConqueror.ID).counter;
            damage = (1 + amount * 0.01F) * damage;
        }
        magicNumber = MathUtils.floor(damage);
        secondMagicNumber = MathUtils.floor(damage * damage_scale);
        isSecondMagicNumberModified = (baseSecondMagicNumber != secondMagicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseSecondMagicNumber = MathUtils.floor(baseMagicNumber * damage_scale);
        super.applyPowers();
        float damage = magicNumber;
        if (AbstractDungeon.player.hasRelic(HandOfConqueror.ID)) {
            int amount = AbstractDungeon.player.getRelic(HandOfConqueror.ID).counter;
            damage = (1 + amount * 0.01F) * damage;
        }
        for (AbstractPower power : mo.powers)
            damage = power.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
        if (mo.hasPower("nearlmod:Duel")) {
            damage = MathUtils.floor(damage * (1 - 0.01F * mo.getPower("nearlmod:Duel").amount));
        }
        for (AbstractPower power : mo.powers)
            damage = power.atDamageFinalReceive(damage, DamageInfo.DamageType.NORMAL);
        if (damage < 0.0F) {
            damage = 0.0F;
        }
        magicNumber = MathUtils.floor(damage);
        secondMagicNumber = MathUtils.floor(damage * damage_scale);
        isSecondMagicNumberModified = (baseSecondMagicNumber != secondMagicNumber);
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
    public AbstractCard makeStatEquivalentCopy() {
        ArtificialSnowfall c = (ArtificialSnowfall)super.makeStatEquivalentCopy();
        c.damage_scale = damage_scale;
        return c;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            damage_scale = 3.3F;
            upgradeMagicNumber(UPG_BASIC);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
