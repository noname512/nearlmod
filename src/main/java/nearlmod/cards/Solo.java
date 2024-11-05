package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class Solo extends AbstractNearlCard {
    public static final String ID = "nearlmod:Solo";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/solo.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 9;
    private static final int HP_LOSE = 5;
    private static final int EXTRA_DMG = 15;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int UPGRADE_PLUS_EX_DMG = 6;

    public Solo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = HP_LOSE;
        secondMagicNumber = baseSecondMagicNumber = EXTRA_DMG;
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Horn.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        int resHp = AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player);
        return NLMOD.checkOrb(Horn.ORB_ID) && resHp > magicNumber;
    }

    @Override
    public void applyPowers() {
        int tmp = baseDamage;
        baseDamage = baseSecondMagicNumber;
        super.applyPowers();
        secondMagicNumber = damage;
        isSecondMagicNumberModified = isDamageModified;
        baseDamage = tmp;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int tmp = baseDamage;
        baseDamage = baseSecondMagicNumber;
        super.calculateCardDamage(mo);
        secondMagicNumber = damage;
        isSecondMagicNumberModified = isDamageModified;
        baseDamage = tmp;
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            addToBot(new LoseHPAction(p, p, magicNumber));
            addToBot(new DamageAction(m, new DamageInfo(p, damage + secondMagicNumber)));
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Solo();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_EX_DMG);
        }
    }
}
