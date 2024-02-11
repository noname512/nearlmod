package nearlmod.cards.friendcards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.actions.UseShadowAction;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;

public class WhipSword extends AbstractFriendCard {
    public static final String ID = "nearlmod:WhipSword";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String EXTRA_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    public static final String IMG_PATH = "images/cards/whipsword.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 4;
    private static final int EXTRA_DMG = 8;

    public WhipSword() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Whislash");
        magicNumber = baseMagicNumber = EXTRA_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_DMG;
    }

    public int calcShadow() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower("nearlmod:Shadow")) {
            return p.getPower("nearlmod:Shadow").amount;
        }
        return 0;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, secondMagicNumber, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));

        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend) {
                int friendDamage = secondMagicNumber + ((AbstractFriend) orb).getTrustAmount();
                if (orb instanceof Viviana) {
                    friendDamage += calcShadow();
                    addToBot(new UseShadowAction(p));
                }
                addToBot(new PureDamageAllEnemiesAction(p, friendDamage, orb.ID + damageSuffix));

            }

        if (upgraded) {
            addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        }

        if (upgraded) {
            rawDescription = UPGRADE_DESCRIPTION;
        }
        else {
            rawDescription = DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        damage = 0;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend) {
                damage += secondMagicNumber + ((AbstractFriend)orb).getTrustAmount();
                if (orb instanceof Viviana) {
                    damage += calcShadow();
                }
            }
        float tmp = secondMagicNumber;
        for (AbstractRelic relic : p.relics)
            tmp = relic.atDamageModify(tmp, this);
        for (AbstractPower power : p.powers)
            tmp = power.atDamageGive(tmp, damageTypeForTurn);
        for (AbstractPower power : p.powers)
            tmp = power.atDamageFinalGive(tmp, damageTypeForTurn);
        if (tmp < 0) {
            tmp = 0;
        }
        damage += MathUtils.floor(tmp);
        isDamageModified = true;
        if (upgraded) {
            damage += magicNumber;
        }
        if (upgraded) {
            rawDescription = UPGRADE_DESCRIPTION + EXTRA_DESCRIPTION;
        }
        else {
            rawDescription = DESCRIPTION + EXTRA_DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (mo == null) return;
        damage = 0;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend) {
                int friendDamage = secondMagicNumber + ((AbstractFriend)orb).getTrustAmount();
                if (orb instanceof Viviana) {
                    friendDamage += calcShadow();
                }
                damage += staticCalcDmg(mo, friendDamage, damageTypeForTurn, true);
            }
        DamageInfo t = new DamageInfo(p, secondMagicNumber);
        t.applyPowers(p, mo);
        damage += t.output;
        if (upgraded) {
            damage += magicNumber;
        }
        isDamageModified = true;
        if (upgraded) {
            rawDescription = UPGRADE_DESCRIPTION + EXTRA_DESCRIPTION;
        }
        else {
            rawDescription = DESCRIPTION + EXTRA_DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhipSword();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
