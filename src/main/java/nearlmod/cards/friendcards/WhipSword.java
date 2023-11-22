package nearlmod.cards.friendcards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.orbs.AbstractFriend;
import nearlmod.patches.AbstractCardEnum;

public class WhipSword extends AbstractFriendCard {
    public static final String ID = "nearlmod:WhipSword";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/whipsword.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 3;
    private static final int EXTRA_DMG = 8;

    public WhipSword() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Whislash");
        magicNumber = baseMagicNumber = EXTRA_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        float tmp = secondMagicNumber;
        for (AbstractPower power : p.powers)
            tmp = power.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
        for (AbstractPower power : p.powers)
            tmp = power.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
        int dmg = MathUtils.floor(tmp);
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            addToBot(new DamageAction(ms, new DamageInfo(p, calculateSingleDamage(ms, dmg))));

        DamageInfo info;
        if (upgraded) {
            for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters) {
                info = new DamageInfo(p, calculateSingleDamage(ms, magicNumber));
                info.name = belongFriend + AbstractFriendCard.damageSuffix;
                addToBot(new DamageAction(ms, info));
            }
        }

        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend) {
                dmg = secondMagicNumber + ((AbstractFriend)orb).trustAmount;
                for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters) {
                    info = new DamageInfo(p, calculateSingleDamage(ms, dmg));
                    info.name = orb.ID + AbstractFriendCard.damageSuffix;
                    addToBot(new DamageAction(ms, info));
                }
            }
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
