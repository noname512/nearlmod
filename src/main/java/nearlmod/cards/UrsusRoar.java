package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.NLMOD;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Gummy;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class UrsusRoar extends AbstractNearlCard {
    public static final String ID = "nearlmod:UrsusRoar";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/ursussroar.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 12;
    private static final int GUMMY_DAMAGE_AMT = 8;
    private static final int GUMMY_TRUST = 2;
    private static final int UPGRADE_DAMAGE_AMT = 4;
    private static final int UPGRADE_GUMMY_DAMAGE_AMT = 2;
    private static final int UPGRADE_GUMMY_TRUST = 1;

    public UrsusRoar() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Gummy.ORB_ID;
        damage = baseDamage = DAMAGE_AMT;
        magicNumber = baseMagicNumber = GUMMY_DAMAGE_AMT;
        secondMagicNumber = baseSecondMagicNumber = GUMMY_TRUST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (extraTriggered()) {
            DamageInfo GummyInfo = new DamageInfo(p, magicNumber);
            GummyInfo.name = Gummy.ORB_ID + AbstractFriendCard.damageSuffix;
            addToBot(new DamageAction(m, GummyInfo));
            addToBot(new SummonFriendAction(new Gummy(secondMagicNumber)));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof Gummy) {
                magicNumber += ((Gummy)orb).getTrustAmount();
                break;
            }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers();
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped()) {
                magicNumber = calculateSingleDamage(m, magicNumber, true);
                isMagicNumberModified = (magicNumber != baseMagicNumber);
            }
        }
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Gummy.ORB_ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new UrsusRoar();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE_AMT);
            upgradeMagicNumber(UPGRADE_GUMMY_DAMAGE_AMT);
            upgradeSecondMagicNumber(UPGRADE_GUMMY_TRUST);
            initializeDescription();
        }
    }
}
