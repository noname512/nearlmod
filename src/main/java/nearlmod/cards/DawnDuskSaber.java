package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonOrbAction;
import nearlmod.actions.WeakenAllAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Shining;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.BFPPower;

public class DawnDuskSaber extends AbstractNearlCard {
    public static final String ID = "nearlmod:DawnDuskSaber";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/dawndusksaber.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int ATTACK_DMG = 3;

    public DawnDuskSaber() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        damage = baseDamage = ATTACK_DMG;
        tags.add(NearlTags.IS_SUMMON_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonOrbAction(new Shining()));
        if (p.hasPower("nearlmod:BFPPower"))
            ((BFPPower) p.getPower("nearlmod:BFPPower")).cardPlayed++;
        DamageInfo info = new DamageInfo(p, damage);
        info.name = AbstractFriendCard.damageSuffix;
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters) {
            calculateCardDamage(ms);
            info.base = damage;
            addToBot(new DamageAction(ms, info));
        }
        if (upgraded) addToBot(new WeakenAllAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DawnDuskSaber();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
